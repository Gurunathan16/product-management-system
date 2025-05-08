package com.porul.product_management.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsersLogin(

        @NotBlank(message = "Username cannot be null or blank.")
        @Size(min = 6, max = 15, message = "Username cannot be less than 6 characters and more than 15 Characters.")
        String username,

        @NotBlank(message = "Password cannot be null or blank.")
        @Size(min = 8, max = 12, message = "Password cannot be less than 8 characters and more than 12 characters.")
        String password

) { }
