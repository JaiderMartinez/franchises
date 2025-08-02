package co.com.nequi.api.mappper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.com.nequi.api.dto.request.BranchRequest;
import co.com.nequi.api.dto.response.BranchResponse;
import co.com.nequi.model.branch.Branch;

@Mapper
public interface BranchMapper {
    Branch toModel(BranchRequest request);
    @Mapping(target = "id", source = "branchId")
    Branch toModel(BranchRequest request, Long branchId);
    BranchResponse toResponse(Branch branch);
}
