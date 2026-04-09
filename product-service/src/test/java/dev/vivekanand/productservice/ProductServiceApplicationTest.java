package dev.vivekanand.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceApplicationTest {

    @Test
    void contextLoads() {
        // This test ensures the Spring application context starts correctly
    }

    @Test
    void main_ShouldRunApplication() {
        assertDoesNotThrow(() -> ProductServiceApplication.main(new String[]{}));
    }
}
