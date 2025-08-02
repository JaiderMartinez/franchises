package co.com.nequi.r2dbc.repository;

import org.springframework.stereotype.Repository;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.r2dbc.dao.BranchDao;
import co.com.nequi.r2dbc.mapper.BranchEntityMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PostgresqlBranchRepositoryImpl implements BranchRepository {

    private final BranchDao branchDao;
    private final BranchEntityMapper branchMapper;

    @Override
    public Mono<Boolean> existsByFranchiseIdAndName(Long franchiseId, String branchName) {
        return branchDao.existsByFranchiseIdAndName(franchiseId, branchName);
    }

    @Override
    public Mono<Boolean> existsByFranchiseIdAndId(Long franchiseId, Long branchId) {
        return branchDao.existsByFranchiseIdAndId(franchiseId, branchId);
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        return branchDao.save(branchMapper.toEntity(branch))
                .map(branchMapper::toModel);
    }

}
