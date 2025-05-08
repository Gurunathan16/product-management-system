package com.porul.product_management.product.dto;

import com.porul.product_management.product.entity.Product;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRegistration
{
    @NotBlank(message = "Product Name cannot be null or blank")
    @Size(min = 5, max = 50, message = "Product Name cannot be less than 5 and more than 50 Characters.")
    private String productName;

    @NotBlank(message = "Category cannot be null or blank")
    private String category;

    @NotBlank(message = "Company cannot be null or blank")
    private String company;

    @NotBlank(message = "Usage Location cannot be null or blank")
    private String usageLocation;

    @NotNull(message = "Date of Purchase cannot be null.")
    @PastOrPresent
    private LocalDate dateOfPurchase;

    @NotNull(message = "Mode of Purchase cannot be null.")
    private Product.ModeOfPurchase modeOfPurchase;

    @NotBlank(message = "Purchase Source Name cannot be null or blank")
    private String purchaseSourceName; // Website or shop name.

    @NotNull(message = "Price cannot be null")
    @Min(0)
    private Double price;

    @NotNull(message = "Warranty Period In Months cannot be null")
    private Integer warrantyPeriodInMonths;

    private Boolean reminderEnabled;

    private String notes;
}
