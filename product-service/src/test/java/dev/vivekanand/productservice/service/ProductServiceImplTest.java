package dev.vivekanand.productservice.service;

import dev.vivekanand.productservice.dto.ProductRequest;
import dev.vivekanand.productservice.dto.ProductResponse;
import dev.vivekanand.productservice.exception.DuplicateSkuException;
import dev.vivekanand.productservice.exception.ResourceNotFoundException;
import dev.vivekanand.productservice.model.Product;
import dev.vivekanand.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void create_ShouldSaveProduct() {
        ProductRequest request = new ProductRequest("P1", "D1", BigDecimal.TEN, "S1");
        when(repository.existsBySku("S1")).thenReturn(false);
        when(repository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        ProductResponse response = service.create(request);

        assertNotNull(response.id());
        assertEquals("P1", response.name());
        verify(repository).save(any(Product.class));
    }

    @Test
    void create_ShouldThrowException_WhenSkuExists() {
        ProductRequest request = new ProductRequest("P1", "D1", BigDecimal.TEN, "S1");
        when(repository.existsBySku("S1")).thenReturn(true);

        assertThrows(DuplicateSkuException.class, () -> service.create(request));
        verify(repository, never()).save(any());
    }

    @Test
    void getById_ShouldReturnProduct() {
        Product product = Product.builder().id(1L).name("P1").sku("S1").build();
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = service.getById(1L);

        assertEquals(1L, response.id());
        assertEquals("P1", response.name());
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    void list_ShouldReturnPage() {
        Product product = Product.builder().id(1L).name("P1").sku("S1").build();
        Page<Product> page = new PageImpl<>(List.of(product));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ProductResponse> response = service.list(Pageable.unpaged());

        assertEquals(1, response.getContent().size());
        assertEquals("P1", response.getContent().get(0).name());
    }

    @Test
    void update_ShouldUpdateProduct() {
        Product existing = Product.builder().id(1L).name("P1").sku("S1").build();
        ProductRequest request = new ProductRequest("P2", "D2", BigDecimal.TEN, "S2");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsBySku("S2")).thenReturn(false);
        when(repository.save(any(Product.class))).thenReturn(existing);

        ProductResponse response = service.update(1L, request);

        assertEquals("P2", response.name());
        assertEquals("S2", response.sku());
    }

    @Test
    void delete_ShouldCallRepository() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void update_ShouldThrowException_WhenSkuExists() {
        Product existing = Product.builder().id(1L).name("P1").sku("S1").build();
        ProductRequest request = new ProductRequest("P2", "D2", BigDecimal.TEN, "S2");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsBySku("S2")).thenReturn(true);

        assertThrows(DuplicateSkuException.class, () -> service.update(1L, request));
        verify(repository, never()).save(any());
    }

    @Test
    void update_ShouldNotCheckSku_WhenSkuNotChanged() {
        Product existing = Product.builder().id(1L).name("P1").sku("S1").build();
        ProductRequest request = new ProductRequest("P2", "D2", BigDecimal.TEN, "S1");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Product.class))).thenReturn(existing);

        ProductResponse response = service.update(1L, request);

        assertEquals("P2", response.name());
        assertEquals("S1", response.sku());
        verify(repository, never()).existsBySku(any());
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(1L));
    }
}
