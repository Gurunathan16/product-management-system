package com.porul.product_management.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(

        @NotBlank(message = "Refresh Token cannot be null or blank.")
        String refreshToken
) { }
