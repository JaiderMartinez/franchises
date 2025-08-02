package co.com.nequi.api;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.com.nequi.api.config.ProductTestConfig;
import co.com.nequi.api.dto.request.ProductRequest;
import co.com.nequi.api.dto.response.ProductResponse;
import co.com.nequi.api.mappper.ProductMapper;
import co.com.nequi.model.product.Product;
import co.com.nequi.usecase.product.ProductUseCase;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ProductApiController.class)
@ContextConfiguration(classes = { ProductApiController.class, ProductTestConfig.class })
class ProductApiControllerTest {

    private static final String BASE_PATH = "/franchises/{franchiseId}/branches/{branchId}/products";
    @Autowired
    private ProductUseCase productUseCase;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void createProduct_withValidRequest_shouldReturnProductResponseAndStatusCreated() {
        Long franchiseId = 1L;
        Long branchId = 100L;
        ProductRequest request = new ProductRequest("Coca-Cola");
        Product product = new Product(request.name(), branchId);
        ProductResponse response = new ProductResponse(1L, product.name());

        when(productMapper.toModel(request, branchId)).thenReturn(product);
        when(productUseCase.createProduct(franchiseId, product)).thenReturn(Mono.just(product));
        when(productMapper.toResponse(product)).thenReturn(response);

        webTestClient.post()
                .uri(BASE_PATH, franchiseId, branchId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(CREATED)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ProductResponse.class)
                .isEqualTo(response);
    }

    @Test
    void partialUpdateProduct_withValidRequest_shouldReturnProductResponseAndStatusOk() {
        Long franchiseId = 1L;
        Long branchId = 100L;
        Long productId = 200L;
        ProductRequest request = new ProductRequest("Coca-Cola Zero");
        Product updatedProduct = new Product(productId, request.name(), branchId);
        ProductResponse response = new ProductResponse(productId, updatedProduct.name());

        when(productMapper.toModel(request, branchId)).thenReturn(updatedProduct);
        when(productUseCase.partialUpdateProduct(franchiseId, updatedProduct)).thenReturn(Mono.just(updatedProduct));
        when(productMapper.toResponse(updatedProduct)).thenReturn(response);

        webTestClient.patch()
                .uri(BASE_PATH + "/{productId}", franchiseId, branchId, productId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ProductResponse.class)
                .isEqualTo(response);
    }
}
