package co.com.nequi.r2dbc.dao;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import co.com.nequi.r2dbc.entities.ProductEntity;
import co.com.nequi.r2dbc.entities.TopStockProductRow;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductDao extends R2dbcRepository<ProductEntity, Long> {
    Mono<Boolean> existsByBranchIdAndName(Long branchId, String name);
    Mono<Boolean> existsByBranchIdAndId(Long branchId, Long id);
    @Query("""
        SELECT DISTINCT ON (b.id)
            b.id AS branch_id,
            b.name AS branch_name,
            p.id AS product_id,
            p.name AS product_name,
            p.stock
        FROM branches b
        JOIN products p ON b.id = p.branch_id
        WHERE b.franchise_id = :franchiseId
        ORDER BY b.id, p.stock DESC, p.id ASC
    """)
    Flux<TopStockProductRow> findProductsWithMaxStockByFranchise(Long franchiseId);
}
