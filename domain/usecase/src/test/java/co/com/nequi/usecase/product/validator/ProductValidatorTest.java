package co.com.nequi.usecase.product.validator;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.nequi.model.exception.ErrorCode;
import co.com.nequi.model.exception.FranchiseException;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.gateways.ProductRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ProductValidatorTest {

    private static Product product;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductValidator productValidator;

    @BeforeAll
    static void setup() {
        product = new Product(5L, "Coca-Cola", 99L);
    }

    @Test
    void validateProductNameUniqueWithinBranch_whenNameIsUnique_shouldComplete() {
        when(productRepository.existsByBranchIdAndName(product.branchId(), product.name()))
                .thenReturn(Mono.just(false));

        StepVerifier.create(productValidator.validateProductNameUniqueWithinBranch(product))
                .verifyComplete();

        verify(productRepository).existsByBranchIdAndName(product.branchId(), product.name());
    }

    @Test
    void validateProductNameUniqueWithinBranch_whenNameExists_shouldReturnConflictError() {
        when(productRepository.existsByBranchIdAndName(product.branchId(), product.name()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(productValidator.validateProductNameUniqueWithinBranch(product))
                .expectErrorSatisfies(error -> {
                    assert error instanceof FranchiseException;
                    FranchiseException ex = (FranchiseException) error;
                    assert ex.getErrorCode().equals(ErrorCode.P409000);
                })
                .verify();

        verify(productRepository).existsByBranchIdAndName(product.branchId(), product.name());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", "   ", "\t", "\n" })
    void validateProductNameUniqueWithinBranch_whenNameIsBlankOrNull_shouldReturnBadRequestError(String input) {
        Product testProduct = new Product(5L, input, 99L);

        StepVerifier.create(productValidator.validateProductNameUniqueWithinBranch(testProduct))
                .expectErrorMatches(error ->
                        error instanceof FranchiseException &&
                                ((FranchiseException) error).getErrorCode().equals(ErrorCode.P400000)
                )
                .verify();

        verify(productRepository, never()).existsByBranchIdAndName(any(), any());
    }

    @Test
    void validateBranchExists_whenBranchExists_shouldReturnError() {
        Long branchId = 99L, productId = 5L;
        when(productRepository.existsByBranchIdAndId(branchId, productId))
                .thenReturn(Mono.just(true));

        StepVerifier.create(productValidator.validateBranchExists(branchId, productId))
                .expectErrorMatches(error ->
                        error instanceof FranchiseException &&
                                ((FranchiseException) error).getErrorCode().equals(ErrorCode.P404000)
                )
                .verify();

        verify(productRepository).existsByBranchIdAndId(branchId, productId);
    }

    @Test
    void validateBranchExists_whenBranchDoesNotExist_shouldComplete() {
        Long branchId = 99L, productId = 5L;
        when(productRepository.existsByBranchIdAndId(branchId, productId))
                .thenReturn(Mono.just(false));

        StepVerifier.create(productValidator.validateBranchExists(branchId, productId))
                .verifyComplete();

        verify(productRepository).existsByBranchIdAndId(branchId, productId);
    }

}

