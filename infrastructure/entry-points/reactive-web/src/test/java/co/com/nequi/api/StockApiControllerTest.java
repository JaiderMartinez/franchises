package co.com.nequi.api;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.com.nequi.api.config.ProductTestConfig;
import co.com.nequi.api.dto.response.ProductStockResponse;
import co.com.nequi.api.mappper.ProductMapper;
import co.com.nequi.model.franchise.ProductStock;
import co.com.nequi.usecase.product.ProductUseCase;
import reactor.core.publisher.Flux;

@WebFluxTest(controllers = StockApiController.class)
@ContextConfiguration(classes = { StockApiController.class, ProductTestConfig.class })
class StockApiControllerTest {

    private static final String BASE_PATH = "/franchises/{franchiseId}/branches/products/stocks/top";

    @Autowired
    private ProductUseCase productUseCase;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getProductsWithMaxStock_shouldReturnListOfProductStockResponseAndStatusOk() {
        Long franchiseId = 1L;

        ProductStock product1 = new ProductStock(1L, "Coca-Cola", 101L, "Coca-Cola", 90);
        ProductStock product2 = new ProductStock(2L, "Fanta", 102L, "Fanta", 75);

        ProductStockResponse response1 = new ProductStockResponse(1L, "Coca-Cola", 101L, "Sucursal Norte", 90);
        ProductStockResponse response2 = new ProductStockResponse(2L, "Fanta", 102L, "Sucursal Centro", 75);

        when(productUseCase.getProductsWithMaxStock(franchiseId)).thenReturn(Flux.just(product1, product2));
        when(productMapper.toStockResponse(product1)).thenReturn(response1);
        when(productMapper.toStockResponse(product2)).thenReturn(response2);

        webTestClient.get()
                .uri(BASE_PATH, franchiseId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBodyList(ProductStockResponse.class)
                .isEqualTo(List.of(response1, response2));
    }

    @Test
    void getProductsWithMaxStock_whenNoProducts_shouldReturnEmptyList() {
        Long franchiseId = 2L;
        when(productUseCase.getProductsWithMaxStock(franchiseId)).thenReturn(Flux.empty());

        webTestClient.get()
                .uri(BASE_PATH, franchiseId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBodyList(ProductStockResponse.class)
                .hasSize(0);
    }

}

