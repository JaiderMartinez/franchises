package co.com.nequi.usecase.branch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.usecase.branch.validator.BranchValidator;
import co.com.nequi.usecase.franchise.validator.FranchiseValidator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class BranchUseCaseTest {

    @Mock
    private BranchRepository branchRepository;
    @Mock
    private BranchValidator branchValidator;
    @Mock
    private FranchiseValidator franchiseValidator;
    @InjectMocks
    private BranchUseCase branchUseCase;

    private static Long franchiseId;
    private static Branch branch;

    @BeforeAll
    static void setUp() {
        franchiseId = 10L;
        branch = new Branch(1L, "Main Branch", franchiseId);
    }

    @Test
    void createBranch_successfully() {
        when(franchiseValidator.validateFranchiseExists(franchiseId)).thenReturn(Mono.empty());
        when(branchValidator.validateBranchNameUniqueWithinFranchise(franchiseId, branch.name()))
                .thenReturn(Mono.empty());
        when(branchRepository.save(argThat(saved -> saved.name().equals(branch.name()))))
                .thenReturn(Mono.just(branch));

        StepVerifier.create(branchUseCase.createBranch(franchiseId, branch))
                .expectNext(branch)
                .verifyComplete();

        verify(franchiseValidator).validateFranchiseExists(franchiseId);
        verify(branchValidator).validateBranchNameUniqueWithinFranchise(franchiseId, branch.name());
        verify(branchRepository).save(argThat(saved -> saved.name().equals(branch.name())));
    }

    @Test
    void partialUpdateBranch_successfully() {
        when(franchiseValidator.validateFranchiseExists(franchiseId)).thenReturn(Mono.empty());
        when(branchValidator.validateBranchExists(franchiseId, branch.id())).thenReturn(Mono.empty());
        when(branchValidator.validateBranchNameUniqueWithinFranchise(franchiseId, branch.name())).thenReturn(Mono.empty());
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(branch));

        StepVerifier.create(branchUseCase.partialUpdateBranch(franchiseId, branch))
                .expectNext(branch)
                .verifyComplete();

        verify(franchiseValidator).validateFranchiseExists(franchiseId);
        verify(branchValidator).validateBranchExists(franchiseId, branch.id());
        verify(branchValidator).validateBranchNameUniqueWithinFranchise(franchiseId, branch.name());
        verify(branchRepository).save(argThat(saved -> saved.name().equals(branch.name())));
    }

}
