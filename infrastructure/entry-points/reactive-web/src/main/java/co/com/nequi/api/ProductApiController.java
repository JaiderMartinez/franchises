package co.com.nequi.api;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.nequi.api.dto.request.ProductRequest;
import co.com.nequi.api.dto.response.ProductResponse;
import co.com.nequi.api.mappper.ProductMapper;
import co.com.nequi.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/franchises/{franchiseId}/branches/{branchId}/products")
public class ProductApiController {

    private final ProductUseCase productUseCase;
    private final ProductMapper productMapper;

    @PostMapping(
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public Mono<ResponseEntity<ProductResponse>> createProduct(@PathVariable Long franchiseId,
                                                               @PathVariable Long branchId,
                                                               @RequestBody Mono<ProductRequest> productRequest) {
        return productRequest
                .map(productReq -> productMapper.toModel(productReq, branchId))
                .flatMap(product -> productUseCase.createProduct(franchiseId, product))
                .map(productMapper::toResponse)
                .map(responseDto -> ResponseEntity.status(CREATED).body(responseDto));
    }

    @PatchMapping(value = "/{productId}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public Mono<ResponseEntity<ProductResponse>> partialUpdateProduct(@PathVariable Long franchiseId,
                                                                     @PathVariable Long branchId,
                                                                     @PathVariable Long productId,
                                                                     @RequestBody Mono<ProductRequest> productRequest) {
        return productRequest
                .map(productReq -> productMapper.toModel(productReq, branchId, productId))
                .flatMap(product -> productUseCase.partialUpdateProduct(franchiseId, product))
                .map(productMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @PatchMapping(value = "/{productId}/stocks",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public Mono<ResponseEntity<ProductResponse>> updateStock(@PathVariable Long franchiseId,
                                                             @PathVariable Long branchId,
                                                             @PathVariable Long productId,
                                                             @RequestBody Mono<ProductRequest> productRequest) {
        return productRequest
                .map(productReq -> productMapper.toModel(productReq, branchId, productId))
                .flatMap(product -> productUseCase.updateStock(franchiseId, product))
                .map(productMapper::toResponse)
                .map(ResponseEntity::ok);
    }

}
