package co.com.nequi.model.product.gateways;

import co.com.nequi.model.franchise.ProductStock;
import co.com.nequi.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Boolean> existsByBranchIdAndName(Long branchId, String productName);
    Mono<Boolean> existsByBranchIdAndId(Long branchId, Long productId);
    Mono<Product> save(Product product);
    Mono<Product> get(Long productId);
    Flux<ProductStock> getProductsWithMaxStockByFranchise(Long branchId);
}
