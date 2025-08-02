package co.com.nequi.api;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.com.nequi.api.config.FranchiseTestConfig;
import co.com.nequi.api.dto.request.FranchiseRequest;
import co.com.nequi.api.dto.response.FranchiseResponse;
import co.com.nequi.api.mappper.FranchiseMapper;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.usecase.franchise.FranchiseUseCase;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = FranchisesApiController.class)
@ContextConfiguration(classes = { FranchisesApiController.class, FranchiseTestConfig.class })
class FranchisesApiControllerTest {

    private static final String BASE_PATH = "/franchises";
    @Autowired
    private FranchiseUseCase franchiseUseCase;
    @Autowired
    private FranchiseMapper franchiseMapper;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void createFranchise_withRequestValid_shouldReturnFranchiseResponseAndStatusCreated() {
        Long id = 1L;
        FranchiseRequest request = new FranchiseRequest("Subway");
        Franchise franchise = new Franchise(id, request.name());
        FranchiseResponse response = new FranchiseResponse(id, franchise.name());

        when(franchiseMapper.toModel(request)).thenReturn(franchise);
        when(franchiseUseCase.createFranchise(franchise)).thenReturn(Mono.just(franchise));
        when(franchiseMapper.toResponse(franchise)).thenReturn(response);

        webTestClient.post()
                .uri(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(CREATED)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(FranchiseResponse.class)
                .isEqualTo(response);
    }

    @Test
    void partialUpdateFranchise_withRequestValid_shouldReturnFranchiseResponseAndStatusOk() {
        Long id = 1L;
        FranchiseRequest request = new FranchiseRequest("Subway Updated");
        Franchise franchise = new Franchise(id, request.name());
        FranchiseResponse response = new FranchiseResponse(id, franchise.name());

        when(franchiseMapper.toModel(request, id)).thenReturn(franchise);
        when(franchiseUseCase.partialUpdateFranchise(franchise)).thenReturn(Mono.just(franchise));
        when(franchiseMapper.toResponse(franchise)).thenReturn(response);

        webTestClient.patch()
                .uri(BASE_PATH + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(FranchiseResponse.class)
                .isEqualTo(response);
    }

}
