package co.com.nequi.api.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import co.com.nequi.api.mappper.FranchiseMapper;
import co.com.nequi.usecase.franchise.FranchiseUseCase;

@TestConfiguration
public class FranchiseTestConfig {

    @Bean
    public FranchiseUseCase franchiseUseCase() {
        return Mockito.mock(FranchiseUseCase.class);
    }

    @Bean
    public FranchiseMapper franchiseMapper() {
        return Mockito.mock(FranchiseMapper.class);
    }

}
