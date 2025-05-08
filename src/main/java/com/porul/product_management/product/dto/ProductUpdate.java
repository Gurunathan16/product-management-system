package com.porul.product_management.product.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdate
{
    private Integer id;

    @NotBlank(message = "Product Name cannot be null or blank")
    @Size(min = 5, max = 50, message = "Product Name cannot be less than 5 and more than 50 Characters.")
    private String productName;

    @NotBlank(message = "Category cannot be null or blank")
    private String category;

    @NotBlank(message = "Company cannot be null or blank")
    private String company;

    @NotBlank(message = "Usage Location cannot be null or blank")
    private String usageLocation;

    @NotNull(message = "Price cannot be null or blank")
    @Min(0)
    private Double price;

    private Boolean reminderEnabled;

    private String notes;
}
