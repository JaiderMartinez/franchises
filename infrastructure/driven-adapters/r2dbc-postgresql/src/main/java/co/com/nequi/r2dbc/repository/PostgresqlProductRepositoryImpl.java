package co.com.nequi.r2dbc.repository;

import org.springframework.stereotype.Repository;

import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.gateways.ProductRepository;
import co.com.nequi.r2dbc.dao.ProductDao;
import co.com.nequi.r2dbc.mapper.ProductEntityMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PostgresqlProductRepositoryImpl implements ProductRepository {
    
    private final ProductDao productDao;
    private final ProductEntityMapper productMapper;

    @Override
    public Mono<Boolean> existsByBranchIdAndName(Long branchId, String productName) {
        return productDao.existsByBranchIdAndName(branchId, productName);
    }

    @Override
    public Mono<Boolean> existsByBranchIdAndId(Long branchId, Long productId) {
        return productDao.existsByBranchIdAndId(branchId, productId);
    }

    @Override
    public Mono<Product> save(Product product) {
        return productDao.save(productMapper.toEntity(product))
                .map(productMapper::toModel);
    }

}
