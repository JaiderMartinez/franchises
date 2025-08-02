package co.com.nequi.usecase.product.validator;

import co.com.nequi.model.exception.ErrorCode;
import co.com.nequi.model.exception.FranchiseException;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductValidator {

    private final ProductRepository productRepository;

    public Mono<Void> validateProductNameUniqueWithinBranch(final Product product) {
        return Mono.justOrEmpty(product.name())
                .filter(name -> !name.isBlank())
                .switchIfEmpty(Mono.error(new FranchiseException(ErrorCode.P400000)))
                .flatMap(name -> productRepository.existsByBranchIdAndName(product.branchId(), name))
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new FranchiseException(ErrorCode.P409000))
                        : Mono.empty());
    }

    public Mono<Void> validateBranchExists(final Long branchId, final Long productId) {
        return productRepository.existsByBranchIdAndId(branchId, productId)
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new FranchiseException(ErrorCode.P404000))
                        : Mono.empty());
    }

}
