package co.com.nequi.r2dbc.mapper;

import org.mapstruct.Mapper;

import co.com.nequi.model.product.Product;
import co.com.nequi.r2dbc.entities.ProductEntity;

@Mapper
public interface ProductEntityMapper {
    Product toModel(ProductEntity productEntity);
    ProductEntity toEntity(Product product);
}
