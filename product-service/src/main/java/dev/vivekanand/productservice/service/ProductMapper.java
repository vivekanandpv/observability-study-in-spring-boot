package dev.vivekanand.productservice.service;

import dev.vivekanand.productservice.dto.ProductRequest;
import dev.vivekanand.productservice.dto.ProductResponse;
import dev.vivekanand.productservice.model.Product;

public final class ProductMapper {

    private ProductMapper() {}

    public static Product toEntity(ProductRequest dto) {
        return Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .sku(dto.sku())
                .build();
    }

    public static void updateEntity(Product entity, ProductRequest dto) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setSku(dto.sku());
    }

    public static ProductResponse toResponse(Product entity) {
        return new ProductResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getSku(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
