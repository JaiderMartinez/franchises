package co.com.nequi.api.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import co.com.nequi.api.mappper.BranchMapper;
import co.com.nequi.usecase.branch.BranchUseCase;

@TestConfiguration
public class BranchTestConfig {

    @Bean
    public BranchUseCase branchUseCase() {
        return Mockito.mock(BranchUseCase.class);
    }

    @Bean
    public BranchMapper branchMapper() {
        return Mockito.mock(BranchMapper.class);
    }

}
