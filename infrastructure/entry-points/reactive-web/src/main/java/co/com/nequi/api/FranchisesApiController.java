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

import co.com.nequi.api.dto.FranchiseRequest;
import co.com.nequi.api.dto.FranchiseResponse;
import co.com.nequi.api.mappper.FranchiseMapper;
import co.com.nequi.usecase.franchise.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/franchises")
public class FranchisesApiController {

    private final FranchiseUseCase franchiseUseCase;
    private final FranchiseMapper franchiseMapper;

    @PostMapping(
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public Mono<ResponseEntity<FranchiseResponse>> createFranchise(
            @RequestBody Mono<FranchiseRequest> franchiseRequestDto) {
        return franchiseRequestDto
                .map(franchiseMapper::toModel)
                .flatMap(franchiseUseCase::createFranchise)
                .map(franchiseMapper::toResponse)
                .map(responseDto -> ResponseEntity.status(CREATED).body(responseDto));
    }

    @PatchMapping(value = "/{franchiseId}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public Mono<ResponseEntity<FranchiseResponse>> partialUpdateFranchise(@PathVariable Long franchiseId,
                                                             @RequestBody Mono<FranchiseRequest> franchiseRequest) {
        return franchiseRequest
                .map(franchiseReq -> franchiseMapper.toModel(franchiseReq, franchiseId))
                .flatMap(franchiseUseCase::partialUpdateFranchise)
                .map(franchiseMapper::toResponse)
                .map(ResponseEntity::ok);
    }

}
