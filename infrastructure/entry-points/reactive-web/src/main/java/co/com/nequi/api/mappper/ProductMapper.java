package co.com.nequi.api.mappper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.com.nequi.api.dto.request.ProductRequest;
import co.com.nequi.api.dto.response.ProductResponse;
import co.com.nequi.api.dto.response.ProductStockResponse;
import co.com.nequi.model.franchise.ProductStock;
import co.com.nequi.model.product.Product;

@Mapper
public interface ProductMapper {

    @Mapping(target = "branchId", source = "branchId")
    Product toModel(ProductRequest request, Long branchId);

    @Mapping(target = "id", source = "productId")
    Product toModel(ProductRequest request, Long branchId, Long productId);

    ProductResponse toResponse(Product product);

    ProductStockResponse toStockResponse(ProductStock product);

}
