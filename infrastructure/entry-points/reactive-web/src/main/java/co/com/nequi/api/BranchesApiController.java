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

import co.com.nequi.api.dto.request.BranchRequest;
import co.com.nequi.api.dto.response.BranchResponse;
import co.com.nequi.api.mappper.BranchMapper;
import co.com.nequi.usecase.branch.BranchUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/franchises/{franchiseId}/branches")
public class BranchesApiController {

    private final BranchUseCase branchUseCase;
    private final BranchMapper branchMapper;

    @PostMapping(
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public Mono<ResponseEntity<BranchResponse>> createBranch(@PathVariable Long franchiseId,
                                                             @RequestBody Mono<BranchRequest> branchRequest) {
        return branchRequest
                .map(branchMapper::toModel)
                .flatMap(branch -> branchUseCase.createBranch(franchiseId, branch))
                .map(branchMapper::toResponse)
                .map(responseDto -> ResponseEntity.status(CREATED).body(responseDto));
    }

    @PatchMapping(value = "/{branchId}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public Mono<ResponseEntity<BranchResponse>> partialUpdateBranch(@PathVariable Long franchiseId,
                                                                    @RequestBody Mono<BranchRequest> branchRequest,
                                                                    @PathVariable Long branchId) {
        return branchRequest
                .map(branchReq -> branchMapper.toModel(branchReq, branchId))
                .flatMap(branch -> branchUseCase.partialUpdateBranch(franchiseId, branch))
                .map(branchMapper::toResponse)
                .map(ResponseEntity::ok);
    }

}
