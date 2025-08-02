package co.com.nequi.usecase.product;

import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.franchise.ProductStock;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.gateways.ProductRepository;
import co.com.nequi.usecase.branch.BranchUseCase;
import co.com.nequi.usecase.branch.validator.BranchValidator;
import co.com.nequi.usecase.franchise.validator.FranchiseValidator;
import co.com.nequi.usecase.product.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final BranchValidator branchValidator;
    private final ProductValidator productValidator;
    private final FranchiseValidator franchiseValidator;

    public Mono<Product> createProduct(Long franchiseId, Product product) {
        return branchValidator.validateBranchExists(franchiseId, product.branchId())
                .then(productValidator.validateProductNameUniqueWithinBranch(product))
                .then(Mono.defer(() -> {
                    Product newProduct = new Product(product.name(), product.branchId());
                    return productRepository.save(newProduct);
                }));
    }

    public Mono<Product> partialUpdateProduct(Long franchiseId, Product product) {
        return branchValidator.validateBranchExists(franchiseId, product.branchId())
                .then(productValidator.validateBranchExists(franchiseId, product.id()))
                .then(productValidator.validateProductNameUniqueWithinBranch(product))
                .then(Mono.fromCallable(() -> new Product(product.id(), product.name(), product.branchId())))
                .flatMap(productRepository::save);
    }

    public Mono<Product> updateStock(Long franchiseId, Product product) {
        return branchValidator.validateBranchExists(franchiseId, product.branchId())
                .then(productValidator.validateBranchExists(franchiseId, product.id()))
                .then(Mono.defer(() -> productRepository.get(product.id())))
                .map(productUpdate -> new Product(productUpdate, product.stock()))
                .flatMap(productRepository::save);
    }

    public Flux<ProductStock> getProductsWithMaxStock(Long franchiseId) {
        return franchiseValidator.validateFranchiseExists(franchiseId)
                .thenMany(productRepository.getProductsWithMaxStockByFranchise(franchiseId));
    }

}
