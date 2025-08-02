package co.com.nequi.r2dbc.dao;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import co.com.nequi.r2dbc.entities.BranchEntity;
import reactor.core.publisher.Mono;

public interface BranchDao extends R2dbcRepository<BranchEntity, Long> {
    Mono<Boolean> existsByFranchiseIdAndName(Long franchiseId, String name);
    Mono<Boolean> existsByFranchiseIdAndId(Long franchiseId, Long id);
}
