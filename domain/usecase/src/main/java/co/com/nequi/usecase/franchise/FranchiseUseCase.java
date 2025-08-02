package co.com.nequi.usecase.franchise;

import co.com.nequi.model.exception.ErrorCode;
import co.com.nequi.model.exception.FranchiseException;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.usecase.franchise.validator.FranchiseValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;
    private final FranchiseValidator franchiseValidator;

    public Mono<Franchise> createFranchise(final Franchise franchise) {
        return franchiseValidator.validateUniqueName(franchise.name())
                .then(Mono.just(franchise))
                .flatMap(franchiseRepository::save);
    }

    public Mono<Franchise> partialUpdateFranchise(final Franchise franchise) {
        return franchiseRepository.get(franchise.id())
                .switchIfEmpty(Mono.error(new FranchiseException(ErrorCode.F404000)))
                .flatMap(existingFranchise -> franchiseValidator.validateUniqueName(franchise.name())
                        .then(Mono.just(franchise)))
                .flatMap(franchiseRepository::save);
    }

}
