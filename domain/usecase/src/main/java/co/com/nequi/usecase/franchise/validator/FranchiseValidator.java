package co.com.nequi.usecase.franchise.validator;

import co.com.nequi.model.exception.ErrorCode;
import co.com.nequi.model.exception.FranchiseException;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseValidator {

    private final FranchiseRepository franchiseRepository;

    public Mono<Void> validateUniqueName(String franchiseName) {
        return franchiseRepository.existsFranchise(franchiseName)
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new FranchiseException(ErrorCode.F409000))
                        : Mono.empty());
    }

}
