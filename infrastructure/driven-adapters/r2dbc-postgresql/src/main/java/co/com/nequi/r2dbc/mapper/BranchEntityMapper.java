package co.com.nequi.r2dbc.mapper;

import org.mapstruct.Mapper;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.r2dbc.entities.BranchEntity;

@Mapper
public interface BranchEntityMapper {
    Branch toModel(BranchEntity branchEntity);
    BranchEntity toEntity(Branch branch);
}
