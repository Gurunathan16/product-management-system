package com.porul.product_management.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductProjection
{
    private Integer id;

    private String productName;

    private String category;

    private String company;

    private String usageLocation;

    private LocalDate dateOfPurchase;

    private String modeOfPurchase;

    private String purchaseSourceName;

    private Double price;

    private Integer warrantyPeriodInMonths;

    private Boolean reminderEnabled;

    private String notes;
}
