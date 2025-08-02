package co.com.nequi.model.franchise.gateways;

import co.com.nequi.model.franchise.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> get(Long id);
    Mono<Boolean> existsFranchise(String franchiseName);
    Mono<Boolean> existsFranchiseById(Long franchiseId);
    Mono<Franchise> save(Franchise franchise);
}
