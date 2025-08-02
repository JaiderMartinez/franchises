package co.com.nequi.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.com.nequi.api.config.BranchTestConfig;
import co.com.nequi.api.dto.request.BranchRequest;
import co.com.nequi.api.dto.response.BranchResponse;
import co.com.nequi.api.mappper.BranchMapper;
import co.com.nequi.model.branch.Branch;
import co.com.nequi.usecase.branch.BranchUseCase;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = BranchesApiController.class)
@ContextConfiguration(classes = { BranchesApiController.class, BranchTestConfig.class })
class BranchesApiControllerTest {

    private static final String BASE_PATH = "/franchises/{franchiseId}/branches";
    @Autowired
    private BranchUseCase branchUseCase;
    @Autowired
    private BranchMapper branchMapper;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void createBranch_withValidRequest_shouldReturnBranchResponseAndStatusCreated() {
        Long franchiseId = 1L;
        BranchRequest request = new BranchRequest("North Branch");
        Branch branch = new Branch(request.name(), franchiseId);
        BranchResponse response = new BranchResponse(1L, branch.name());

        when(branchMapper.toModel(request)).thenReturn(branch);
        when(branchUseCase.createBranch(franchiseId, branch)).thenReturn(Mono.just(branch));
        when(branchMapper.toResponse(branch)).thenReturn(response);

        webTestClient.post()
                .uri(BASE_PATH, franchiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(CREATED)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BranchResponse.class)
                .isEqualTo(response);
    }

    @Test
    void partialUpdateBranch_withValidRequest_shouldReturnBranchResponseAndStatusOk() {
        Long franchiseId = 1L;
        Long branchId = 100L;
        BranchRequest request = new BranchRequest("Updated Branch");
        Branch branch = new Branch(branchId, request.name(), franchiseId);
        BranchResponse response = new BranchResponse(branchId, branch.name());

        when(branchMapper.toModel(request, branchId)).thenReturn(branch);
        when(branchUseCase.partialUpdateBranch(franchiseId, branch)).thenReturn(Mono.just(branch));
        when(branchMapper.toResponse(branch)).thenReturn(response);

        webTestClient.patch()
                .uri(BASE_PATH + "/{branchId}", franchiseId, branchId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BranchResponse.class)
                .isEqualTo(response);
    }
}
