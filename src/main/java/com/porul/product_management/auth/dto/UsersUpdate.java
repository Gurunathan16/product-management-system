package com.porul.product_management.auth.dto;

import com.porul.product_management.util.annotations.MinAge;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UsersUpdate(

        @NotBlank(message = "First Name cannot be null or blank")
        String firstName,

        String lastName,

        @NotBlank(message = "Gender cannot be null or blank")
        String gender,

        @NotNull(message = "Date of Birth cannot be null")
        @Past
        @MinAge(18)
        LocalDate dateOfBirth

) { }
