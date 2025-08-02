package co.com.nequi.usecase.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.gateways.ProductRepository;
import co.com.nequi.usecase.branch.validator.BranchValidator;
import co.com.nequi.usecase.product.validator.ProductValidator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    private static Long franchiseId;
    private static Long branchId;
    private static Long productId;
    private static Product product;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private BranchValidator branchValidator;
    @Mock
    private ProductValidator productValidator;
    @InjectMocks
    private ProductUseCase productUseCase;

    @BeforeAll
    static void setUp() {
        franchiseId = 10L;
        branchId = 99L;
        productId = 5L;
        product = new Product(productId, "Coca-Cola", branchId);
    }

    @Test
    void createProduct_successfully() {
        when(branchValidator.validateBranchExists(franchiseId, branchId)).thenReturn(Mono.empty());
        when(productValidator.validateProductNameUniqueWithinBranch(product)).thenReturn(Mono.empty());

        Product newProduct = new Product(product.name(), branchId);
        when(productRepository.save(
                argThat(saved -> saved.name().equals(product.name()) && saved.branchId().equals(branchId))))
                .thenReturn(Mono.just(newProduct));

        StepVerifier.create(productUseCase.createProduct(franchiseId, product))
                .expectNextMatches(p -> p.name().equals("Coca-Cola") && p.branchId().equals(branchId))
                .verifyComplete();

        verify(branchValidator).validateBranchExists(franchiseId, branchId);
        verify(productValidator).validateProductNameUniqueWithinBranch(product);
        verify(productRepository).save(
                argThat(saved -> saved.name().equals("Coca-Cola") && saved.branchId().equals(branchId)));
    }

    @Test
    void partialUpdateProduct_successfully() {
        when(branchValidator.validateBranchExists(franchiseId, branchId)).thenReturn(Mono.empty());
        when(productValidator.validateBranchExists(franchiseId, productId)).thenReturn(Mono.empty());
        when(productValidator.validateProductNameUniqueWithinBranch(product)).thenReturn(Mono.empty());

        Product updatedProduct = new Product(productId, "Coca-Cola", branchId);
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(updatedProduct));

        StepVerifier.create(productUseCase.partialUpdateProduct(franchiseId, product))
                .expectNext(updatedProduct)
                .verifyComplete();

        verify(branchValidator).validateBranchExists(franchiseId, branchId);
        verify(productValidator).validateBranchExists(franchiseId, productId);
        verify(productValidator).validateProductNameUniqueWithinBranch(product);
        verify(productRepository).save(argThat(saved -> saved.name().equals("Coca-Cola") &&
                saved.branchId().equals(branchId) &&
                saved.id().equals(productId)));
    }

}
