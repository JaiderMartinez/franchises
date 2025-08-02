package co.com.nequi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.nequi.api.dto.response.ProductStockResponse;
import co.com.nequi.api.mappper.ProductMapper;
import co.com.nequi.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/franchises/{franchiseId}/branches/products/stocks")
public class StockApiController {

    private final ProductUseCase productUseCase;
    private final ProductMapper productMapper;

    @GetMapping(value = "/top")
    public Flux<ProductStockResponse> getProductsWithMaxStock(@PathVariable Long franchiseId) {
        return productUseCase.getProductsWithMaxStock(franchiseId)
                .map(productMapper::toStockResponse);
    }

}
