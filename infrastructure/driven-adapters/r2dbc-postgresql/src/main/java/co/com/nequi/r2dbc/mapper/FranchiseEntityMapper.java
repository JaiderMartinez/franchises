package co.com.nequi.r2dbc.mapper;

import org.mapstruct.Mapper;

import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.r2dbc.entities.FranchiseEntity;

@Mapper
public interface FranchiseEntityMapper {
    Franchise toModel(FranchiseEntity entity);
    FranchiseEntity toEntity(Franchise franchise);
}
