package co.com.nequi.usecase.franchise;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.nequi.model.exception.ErrorCode;
import co.com.nequi.model.exception.FranchiseException;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.usecase.franchise.validator.FranchiseValidator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;
    @Mock
    private FranchiseValidator franchiseValidator;
    @InjectMocks
    private FranchiseUseCase franchiseUseCase;

    @Test
    void createFranchise_successfully() {
        Franchise franchise = new Franchise(1L, "Subway");
        when(franchiseValidator.validateUniqueName(franchise.name())).thenReturn(Mono.empty());
        when(franchiseRepository.save(franchise)).thenReturn(Mono.just(franchise));

        Mono<Franchise> result = franchiseUseCase.createFranchise(franchise);

        StepVerifier.create(result)
                .expectNextMatches(f -> f.id().equals(franchise.id()) && f.name().equals(franchise.name()))
                .verifyComplete();

        verify(franchiseValidator).validateUniqueName(franchise.name());
        verify(franchiseRepository).save(franchise);
    }

    @Test
    void createFranchise_withDuplicateName_shouldFail() {
        Franchise franchise = new Franchise(1L, "Subway");
        FranchiseException exception = new FranchiseException(ErrorCode.F409000);
        when(franchiseValidator.validateUniqueName(franchise.name())).thenReturn(Mono.error(exception));

        StepVerifier.create(franchiseUseCase.createFranchise(franchise))
                .expectErrorMatches(throwable -> throwable instanceof FranchiseException &&
                        ((FranchiseException) throwable).getErrorCode().equals(ErrorCode.F409000))
                .verify();

        verify(franchiseValidator).validateUniqueName(franchise.name());
        verifyNoInteractions(franchiseRepository);
    }

    @Test
    void partialUpdateFranchise_successfully() {
        Franchise franchise = new Franchise(1L, "Subway");
        when(franchiseRepository.get(franchise.id())).thenReturn(Mono.just(franchise));
        when(franchiseValidator.validateUniqueName(franchise.name())).thenReturn(Mono.empty());
        when(franchiseRepository.save(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseUseCase.partialUpdateFranchise(franchise))
                .expectNext(franchise)
                .verifyComplete();

        verify(franchiseRepository).get(franchise.id());
        verify(franchiseValidator).validateUniqueName(franchise.name());
        verify(franchiseRepository).save(franchise);
    }

    @Test
    void partialUpdateFranchise_notFound_shouldFail() {
        Franchise franchise = new Franchise(1L, "Subway");
        when(franchiseRepository.get(franchise.id())).thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.partialUpdateFranchise(franchise))
                .expectErrorMatches(throwable -> throwable instanceof FranchiseException &&
                        ((FranchiseException) throwable).getErrorCode().equals(ErrorCode.F404000))
                .verify();

        verify(franchiseRepository).get(1L);
        verifyNoInteractions(franchiseValidator);
        verify(franchiseRepository, never()).save(any());
    }

    @Test
    void partialUpdateFranchise_duplicateName_shouldFail() {
        Franchise franchise = new Franchise(1L, "Subway");
        when(franchiseRepository.get(franchise.id())).thenReturn(Mono.just(franchise));
        FranchiseException exception = new FranchiseException(ErrorCode.F409000);
        when(franchiseValidator.validateUniqueName(franchise.name())).thenReturn(Mono.error(exception));

        StepVerifier.create(franchiseUseCase.partialUpdateFranchise(franchise))
                .expectErrorMatches(throwable -> throwable instanceof FranchiseException &&
                        ((FranchiseException) throwable).getErrorCode().equals(ErrorCode.F409000))
                .verify();

        verify(franchiseRepository).get(franchise.id());
        verify(franchiseValidator).validateUniqueName(franchise.name());
        verify(franchiseRepository, never()).save(any());
    }

}
