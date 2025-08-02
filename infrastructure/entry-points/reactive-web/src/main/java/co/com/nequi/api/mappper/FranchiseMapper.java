package co.com.nequi.api.mappper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.com.nequi.api.dto.FranchiseRequest;
import co.com.nequi.api.dto.FranchiseResponse;
import co.com.nequi.model.franchise.Franchise;

@Mapper
public interface FranchiseMapper {
    Franchise toModel(FranchiseRequest franchiseRequest);
    @Mapping(target = "id", source = "franchiseId")
    Franchise toModel(FranchiseRequest franchiseRequest, Long franchiseId);
    FranchiseResponse toResponse(Franchise franchise);
}
