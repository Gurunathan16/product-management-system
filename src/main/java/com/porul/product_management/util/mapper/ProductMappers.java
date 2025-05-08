package com.porul.product_management.util.mapper;

import com.porul.product_management.product.dto.ProductProjection;
import com.porul.product_management.product.dto.ProductRegistration;
import com.porul.product_management.product.dto.ProductUpdate;
import com.porul.product_management.product.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMappers
{
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "username", ignore = true)
    Product productRegistrationToProduct(ProductRegistration productRegistration);

    ProductProjection productToProductProjection(Product product);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product productUpdateToProduct(ProductUpdate productUpdate, @MappingTarget Product product);
}
