package co.com.nequi.model.branch.gateways;

import co.com.nequi.model.branch.Branch;
import reactor.core.publisher.Mono;

public interface BranchRepository {
    Mono<Boolean> existsByFranchiseIdAndName(Long franchiseId, String branchName);
    Mono<Boolean> existsByFranchiseIdAndId(Long franchiseId, Long branchId);
    Mono<Branch> save(Branch branch);
}
