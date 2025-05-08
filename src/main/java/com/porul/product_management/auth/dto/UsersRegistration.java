package com.porul.product_management.auth.dto;

import com.porul.product_management.util.annotations.MinAge;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersRegistration
{
    @NotBlank(message = "First Name cannot be null or blank.")
    private String firstName;

    private String lastName;

    @NotBlank(message = "Gender cannot be null or blank.")
    private String gender;

    @NotNull(message = "Date of Birth cannot be null.")
    @Past
    @MinAge(18)
    private LocalDate dateOfBirth;

    @NotBlank(message = "Username cannot be null or blank.")
    @Size(min = 6, max = 15, message = "Username cannot be less than 6 characters and more than 15 characters.")
    private String username;

    @NotBlank(message = "Password cannot be null or blank.")
    @Size(min = 8, max = 12, message = "Password cannot be less than 8 characters and more than 12 characters.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@$%^&*])[A-Za-z\\d!@$%^&*]{8,12}$",
    message = "Password must contain a Uppercase letter, Lowercase letter, Digit, and Special Character.")
    private String password;

    @NotBlank(message = "Confirm Password cannot be null or blank.")
    private String confirmPassword;

    @NotBlank(message = "Mail ID cannot be null or blank.")
    @Email
    private String mailId;

    @NotBlank(message = "Phone Number cannot be null or blank.")
    @Pattern(regexp = "^\\+\\d{12}$", message = "Phone number not in correct format.")
    private String phoneNumber;

}
