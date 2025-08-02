package co.com.nequi.r2dbc.dao;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import co.com.nequi.r2dbc.entities.ProductEntity;
import reactor.core.publisher.Mono;

public interface ProductDao extends R2dbcRepository<ProductEntity, Long> {
    Mono<Boolean> existsByBranchIdAndName(Long branchId, String name);
    Mono<Boolean> existsByBranchIdAndId(Long branchId, Long id);
}
