package com.porul.product_management.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record ProductInfo(
        @NotNull(message = "Product Id cannot be null")
        Integer id
) { }
