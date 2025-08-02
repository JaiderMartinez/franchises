package co.com.nequi.r2dbc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.com.nequi.model.franchise.ProductStock;
import co.com.nequi.model.product.Product;
import co.com.nequi.r2dbc.entities.ProductEntity;
import co.com.nequi.r2dbc.entities.TopStockProductRow;

@Mapper
public interface ProductEntityMapper {
    Product toModel(ProductEntity productEntity);
    ProductEntity toEntity(Product product);
    ProductStock toProductStock(TopStockProductRow topStockProductRow);
}
