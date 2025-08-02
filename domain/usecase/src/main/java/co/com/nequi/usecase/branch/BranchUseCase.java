package co.com.nequi.usecase.branch;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.usecase.branch.validator.BranchValidator;
import co.com.nequi.usecase.franchise.validator.FranchiseValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase {

    private final BranchRepository branchRepository;
    private final BranchValidator branchValidator;
    private final FranchiseValidator franchiseValidator;

    public Mono<Branch> createBranch(final Long franchiseId, final Branch branch) {
        return franchiseValidator.validateFranchiseExists(franchiseId)
                .then(branchValidator.validateBranchNameUniqueWithinFranchise(franchiseId, branch.name()))
                .then(Mono.defer(() -> {
                    Branch newBranch = new Branch(branch.name(), franchiseId);
                    return branchRepository.save(newBranch);
                }));
    }

    public Mono<Branch> partialUpdateBranch(final Long franchiseId, final Branch branch) {
        return franchiseValidator.validateFranchiseExists(franchiseId)
                .then(branchValidator.validateBranchExists(franchiseId, branch.id()))
                .then(branchValidator.validateBranchNameUniqueWithinFranchise(franchiseId, branch.name()))
                .then(Mono.fromCallable(() -> new Branch(branch.id(), branch.name(), franchiseId)))
                .flatMap(branchRepository::save);
    }

}
