package co.com.nequi.api.mappper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.com.nequi.api.dto.request.ProductRequest;
import co.com.nequi.api.dto.response.ProductResponse;
import co.com.nequi.model.product.Product;

@Mapper
public interface ProductMapper {
    @Mapping(target = "branchId", source = "branchId")
    Product toModel(ProductRequest request, Long branchId);
    ProductResponse toResponse(Product product);
}
