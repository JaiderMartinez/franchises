package co.com.nequi.usecase.branch.validator;

import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exception.ErrorCode;
import co.com.nequi.model.exception.FranchiseException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchValidator {

    private final BranchRepository branchRepository;

    public Mono<Void> validateBranchNameUniqueWithinFranchise(final Long franchiseId, final String branchName) {
        return Mono.justOrEmpty(branchName)
                .filter(name -> !name.isBlank())
                .switchIfEmpty(Mono.error(new FranchiseException(ErrorCode.B400000)))
                .flatMap(name -> branchRepository.existsByFranchiseIdAndName(franchiseId, name))
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new FranchiseException(ErrorCode.B409000))
                        : Mono.empty());
    }

    public Mono<Void> validateBranchExists(final Long franchiseId, final Long branchId) {
        return branchRepository.existsByFranchiseIdAndId(franchiseId, branchId)
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.empty()
                        : Mono.error(new FranchiseException(ErrorCode.B404000)));
    }

}
