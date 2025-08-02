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
        return Mono.justOrEmpty(franchiseName)
                .filter(name -> !name.isBlank())
                .switchIfEmpty(Mono.error(new FranchiseException(ErrorCode.F400000)))
                .flatMap(franchiseRepository::existsFranchise)
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new FranchiseException(ErrorCode.F409000))
                        : Mono.empty());
    }

}
