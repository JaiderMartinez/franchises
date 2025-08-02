package co.com.nequi.usecase.branch.validator;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exception.ErrorCode;
import co.com.nequi.model.exception.FranchiseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class BranchValidatorTest {

    private static final Long FRANCHISE_ID = 10L;
    private static final Long BRANCH_ID = 20L;
    private static final String BRANCH_NAME = "Central Branch";
    @Mock
    private BranchRepository branchRepository;
    @InjectMocks
    private BranchValidator branchValidator;

    @Test
    void validateBranchNameUniqueWithinFranchise_whenNameIsUnique_shouldComplete() {
        when(branchRepository.existsByFranchiseIdAndName(FRANCHISE_ID, BRANCH_NAME))
                .thenReturn(Mono.just(false));

        StepVerifier.create(branchValidator.validateBranchNameUniqueWithinFranchise(FRANCHISE_ID, BRANCH_NAME))
                .verifyComplete();
    }

    @Test
    void validateBranchNameUniqueWithinFranchise_whenNameExists_shouldReturnConflictError() {
        when(branchRepository.existsByFranchiseIdAndName(FRANCHISE_ID, BRANCH_NAME))
                .thenReturn(Mono.just(true));

        StepVerifier.create(branchValidator.validateBranchNameUniqueWithinFranchise(FRANCHISE_ID, BRANCH_NAME))
                .expectErrorSatisfies(error -> {
                    assert error instanceof FranchiseException;
                    FranchiseException ex = (FranchiseException) error;
                    assert ex.getErrorCode().equals(ErrorCode.B409000);
                })
                .verify();

        verify(branchRepository).existsByFranchiseIdAndName(FRANCHISE_ID, BRANCH_NAME);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void validateBranchNameUniqueWithinFranchise_whenNameIsBlankOrNull_shouldReturnBadRequestError(String input) {
        StepVerifier.create(branchValidator.validateBranchNameUniqueWithinFranchise(FRANCHISE_ID, input))
                .expectErrorMatches(error ->
                        error instanceof FranchiseException
                                && ((FranchiseException) error).getErrorCode().equals(ErrorCode.B400000)
                )
                .verify();

        verify(branchRepository, never()).existsByFranchiseIdAndName(FRANCHISE_ID, input);
    }

    @Test
    void validateBranchExists_whenBranchExists_shouldComplete() {
        when(branchRepository.existsByFranchiseIdAndId(FRANCHISE_ID, BRANCH_ID))
                .thenReturn(Mono.just(true));

        StepVerifier.create(branchValidator.validateBranchExists(FRANCHISE_ID, BRANCH_ID))
                .verifyComplete();

        verify(branchRepository).existsByFranchiseIdAndId(FRANCHISE_ID, BRANCH_ID);
    }

    @Test
    void validateBranchExists_whenBranchDoesNotExist_shouldReturnNotFoundError() {
        when(branchRepository.existsByFranchiseIdAndId(FRANCHISE_ID, BRANCH_ID))
                .thenReturn(Mono.just(false));

        StepVerifier.create(branchValidator.validateBranchExists(FRANCHISE_ID, BRANCH_ID))
                .expectErrorMatches(error ->
                    error instanceof FranchiseException
                    && ((FranchiseException) error).getErrorCode().equals(ErrorCode.B404000)
                )
                .verify();

        verify(branchRepository).existsByFranchiseIdAndId(FRANCHISE_ID, BRANCH_ID);
    }

}
