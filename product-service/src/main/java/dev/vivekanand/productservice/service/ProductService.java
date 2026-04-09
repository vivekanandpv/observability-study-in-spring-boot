package dev.vivekanand.productservice.service;

import dev.vivekanand.productservice.dto.ProductRequest;
import dev.vivekanand.productservice.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse getById(Long id);
    Page<ProductResponse> list(Pageable pageable);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);
}
