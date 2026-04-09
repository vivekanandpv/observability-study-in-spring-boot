package dev.vivekanand.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vivekanand.productservice.dto.ProductRequest;
import dev.vivekanand.productservice.dto.ProductResponse;
import dev.vivekanand.productservice.exception.ResourceNotFoundException;
import dev.vivekanand.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.test.context.SpringBootTest
@org.springframework.test.context.ActiveProfiles("test")
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
@org.springframework.context.annotation.Import(dev.vivekanand.productservice.exception.GlobalExceptionHandler.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_ShouldReturnCreated() throws Exception {
        ProductRequest request = new ProductRequest("iPhone", "Apple", new BigDecimal("999"), "IP15");
        ProductResponse response = new ProductResponse(1L, "iPhone", "Apple", new BigDecimal("999"), "IP15", Instant.now(), Instant.now());

        when(service.create(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("iPhone"));
    }

    @Test
    void getById_ShouldReturnProduct() throws Exception {
        ProductResponse response = new ProductResponse(1L, "iPhone", "Apple", new BigDecimal("999"), "IP15", Instant.now(), Instant.now());
        when(service.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getById_ShouldReturnNotFound_WhenProductDoesNotExist() throws Exception {
        when(service.getById(1L)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void list_ShouldReturnPage() throws Exception {
        ProductResponse response = new ProductResponse(1L, "iPhone", "Apple", new BigDecimal("999"), "IP15", Instant.now(), Instant.now());
        Page<ProductResponse> page = new PageImpl<>(List.of(response));
        when(service.list(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }

    @Test
    void update_ShouldReturnUpdatedProduct() throws Exception {
        ProductRequest request = new ProductRequest("iPhone New", "Apple", new BigDecimal("1099"), "IP15");
        ProductResponse response = new ProductResponse(1L, "iPhone New", "Apple", new BigDecimal("1099"), "IP15", Instant.now(), Instant.now());

        when(service.update(eq(1L), any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone New"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());
    }
}
