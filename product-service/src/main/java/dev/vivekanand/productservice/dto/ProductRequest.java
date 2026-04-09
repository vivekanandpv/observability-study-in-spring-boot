package dev.vivekanand.productservice.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 120, message = "Name must be at most 120 characters")
        String name,

        @Size(max = 1024, message = "Description must be at most 1024 characters")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
        @Digits(integer = 17, fraction = 2, message = "Price must have up to 2 decimal places")
        BigDecimal price,

        @NotBlank(message = "SKU is required")
        @Pattern(regexp = "[A-Z0-9_-]+", message = "SKU must be alphanumeric with dashes/underscores only and uppercase")
        @Size(max = 64, message = "SKU must be at most 64 characters")
        String sku
) { }
