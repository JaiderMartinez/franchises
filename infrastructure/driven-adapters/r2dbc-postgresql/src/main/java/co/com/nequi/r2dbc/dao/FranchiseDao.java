package co.com.nequi.r2dbc.dao;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import co.com.nequi.r2dbc.entities.FranchiseEntity;
import reactor.core.publisher.Mono;

public interface FranchiseDao extends R2dbcRepository<FranchiseEntity, Long> {
    Mono<Boolean> existsByName(String name);
}
