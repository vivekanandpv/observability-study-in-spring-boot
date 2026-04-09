package dev.vivekanand.productservice.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testNoArgsConstructor() {
        Product product = new Product();
        assertNotNull(product);
        assertNull(product.getId());
    }

    @Test
    void testSettersForAuditingFields() {
        Product product = new Product();
        Instant now = Instant.now();
        
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        
        assertEquals(now, product.getCreatedAt());
        assertEquals(now, product.getUpdatedAt());
    }

    @Test
    void testAllGettersAndSetters() {
        Product product = new Product();
        BigDecimal price = new BigDecimal("99.99");
        
        product.setId(1L);
        product.setName("Test Name");
        product.setDescription("Test Desc");
        product.setPrice(price);
        product.setSku("TEST-SKU");

        assertEquals(1L, product.getId());
        assertEquals("Test Name", product.getName());
        assertEquals("Test Desc", product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals("TEST-SKU", product.getSku());
    }
    @Test
    void testProductBuilder() {
        Instant now = Instant.now();
        BigDecimal price = new BigDecimal("100.00");
        
        Product product = Product.builder()
                .id(1L)
                .name("Name")
                .description("Description")
                .price(price)
                .sku("SKU-1")
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        assertEquals(1L, product.getId());
        assertEquals("Name", product.getName());
        assertEquals("Description", product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals("SKU-1", product.getSku());
        assertEquals(now, product.getCreatedAt());
        assertEquals(now, product.getUpdatedAt());
    }
}
