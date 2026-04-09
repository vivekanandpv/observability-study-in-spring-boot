package dev.vivekanand.productservice.repository;

import dev.vivekanand.productservice.config.JpaAuditingConfig;
import dev.vivekanand.productservice.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditingConfig.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void saveAndFind_ShouldWork() {
        Product p = Product.builder()
                .name("Prod1")
                .description("Desc")
                .price(new BigDecimal("10.50"))
                .sku("SKU-1")
                .build();
        Product saved = repository.save(p);

        assertNotNull(saved.getId());
        assertTrue(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void existsBySku_ShouldReturnTrue_WhenDuplicate() {
        Product p1 = Product.builder().name("P1").price(BigDecimal.ONE).sku("SKU-1").build();
        repository.save(p1);

        assertTrue(repository.existsBySku("SKU-1"));
        assertFalse(repository.existsBySku("SKU-2"));
    }
}
