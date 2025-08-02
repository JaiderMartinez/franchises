package co.com.nequi.r2dbc.repository;

import org.springframework.stereotype.Repository;

import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.r2dbc.dao.FranchiseDao;
import co.com.nequi.r2dbc.mapper.FranchiseEntityMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PostgresqlFranchiseRepositoryImpl implements FranchiseRepository {

    private final FranchiseDao franchiseDao;
    private final FranchiseEntityMapper franchiseEntityMapper;

    @Override
    public Mono<Franchise> get(Long id) {
        return franchiseDao.findById(id)
                .map(franchiseEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsFranchise(String franchiseName) {
        return franchiseDao.existsByName(franchiseName);
    }

    @Override
    public Mono<Boolean> existsFranchiseById(Long franchiseId) {
        return franchiseDao.existsById(franchiseId);
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return franchiseDao.save(franchiseEntityMapper.toEntity(franchise))
                .map(franchiseEntityMapper::toModel);
    }

}
