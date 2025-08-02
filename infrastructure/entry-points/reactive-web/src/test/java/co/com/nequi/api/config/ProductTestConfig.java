package co.com.nequi.api.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import co.com.nequi.api.mappper.ProductMapper;
import co.com.nequi.usecase.product.ProductUseCase;

@TestConfiguration
public class ProductTestConfig {

    @Bean
    public ProductUseCase productUseCase() {
        return Mockito.mock(ProductUseCase.class);
    }

    @Bean
    public ProductMapper productMapper() {
        return Mockito.mock(ProductMapper.class);
    }

}
