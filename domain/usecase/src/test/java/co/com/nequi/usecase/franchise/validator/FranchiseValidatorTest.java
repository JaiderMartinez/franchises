package co.com.nequi.usecase.franchise.validator;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.nequi.model.exception.ErrorCode;
import co.com.nequi.model.exception.FranchiseException;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FranchiseValidatorTest {

    @Mock
    private FranchiseRepository franchiseRepository;
    @InjectMocks
    private FranchiseValidator franchiseValidator;

    @Test
    void validateUniqueName_whenNotExistsFranchise_shouldReturnMonoEmpty() {
        String name = "Subway";
        when(franchiseRepository.existsFranchise(name)).thenReturn(Mono.just(false));

        StepVerifier.create(franchiseValidator.validateUniqueName(name))
                .verifyComplete();

        verify(franchiseRepository).existsFranchise(name);
    }

    @Test
    void validateUniqueName_whenExistsFranchise_shouldThrowException() {
        String name = "Subway";
        when(franchiseRepository.existsFranchise(name)).thenReturn(Mono.just(true));

        StepVerifier.create(franchiseValidator.validateUniqueName(name))
                .expectErrorMatches(throwable ->
                        throwable instanceof FranchiseException &&
                                ((FranchiseException) throwable).getErrorCode().equals(ErrorCode.F409000)
                )
                .verify();

        verify(franchiseRepository).existsFranchise(name);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void createFranchise_whenNameIsInvalid_shouldThrowFranchiseException(String invalidName) {
        StepVerifier.create(franchiseValidator.validateUniqueName(invalidName))
                .expectErrorMatches(throwable ->
                        throwable instanceof FranchiseException
                                && ((FranchiseException) throwable).getErrorCode().equals(ErrorCode.F400000)
                )
                .verify();

        verifyNoInteractions(franchiseRepository);
    }

}
