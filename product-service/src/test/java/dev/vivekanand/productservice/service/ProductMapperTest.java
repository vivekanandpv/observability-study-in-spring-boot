package dev.vivekanand.productservice.service;

import dev.vivekanand.productservice.dto.ProductRequest;
import dev.vivekanand.productservice.dto.ProductResponse;
import dev.vivekanand.productservice.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    void toEntity_ShouldMapCorrectly() {
        ProductRequest request = new ProductRequest("iPhone 15", "Latest model", new BigDecimal("999.99"), "SKU123");
        Product entity = ProductMapper.toEntity(request);

        assertNotNull(entity);
        assertEquals(request.name(), entity.getName());
        assertEquals(request.description(), entity.getDescription());
        assertEquals(request.price(), entity.getPrice());
        assertEquals(request.sku(), entity.getSku());
        assertNull(entity.getId());
    }

    @Test
    void updateEntity_ShouldUpdateCorrectly() {
        Product entity = Product.builder()
                .name("Old Name")
                .description("Old Desc")
                .price(BigDecimal.ZERO)
                .sku("OLD-SKU")
                .build();
        ProductRequest request = new ProductRequest("New Name", "New Desc", BigDecimal.TEN, "NEW-SKU");

        ProductMapper.updateEntity(entity, request);

        assertEquals(request.name(), entity.getName());
        assertEquals(request.description(), entity.getDescription());
        assertEquals(request.price(), entity.getPrice());
        assertEquals(request.sku(), entity.getSku());
    }

    @Test
    void toResponse_ShouldMapCorrectly() {
        Instant now = Instant.now();
        Product entity = Product.builder()
                .id(1L)
                .name("iPhone 15")
                .description("Latest model")
                .price(new BigDecimal("999.99"))
                .sku("SKU123")
                .build();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        ProductResponse response = ProductMapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(entity.getId(), response.id());
        assertEquals(entity.getName(), response.name());
        assertEquals(entity.getDescription(), response.description());
        assertEquals(entity.getPrice(), response.price());
        assertEquals(entity.getSku(), response.sku());
        assertEquals(entity.getCreatedAt(), response.createdAt());
        assertEquals(entity.getUpdatedAt(), response.updatedAt());
    }
}
