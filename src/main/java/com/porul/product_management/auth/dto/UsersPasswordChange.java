package com.porul.product_management.auth.dto;

import jakarta.validation.constraints.*;

public record UsersPasswordChange(

        @NotBlank(message = "Old Password cannot be null or blank")
        String oldPassword,

        @NotBlank(message = "New Password cannot be null or blank")
        @Size(min = 8, max = 12, message = "Password cannot be less than 8 Characters and more than 12 Characters")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@$%^&*])[A-Za-z\\d!@$%^&*]{8,12}$",
                message = "Password must contain a Uppercase letter, Lowercase letter, Digit, and Special Character")
        String password,

        @NotBlank(message = "Confirm Password cannot be null or blank")
        String confirmPassword

) { }
