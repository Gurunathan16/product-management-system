package com.porul.product_management.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String productName;

    private String category;

    private String company;

    private String usageLocation;

    private LocalDate dateOfPurchase;

    @Enumerated(EnumType.STRING)
    private ModeOfPurchase modeOfPurchase;

    public enum ModeOfPurchase
    {
        ONLINE, OFFLINE
    }

    private String purchaseSourceName;

    private Double price;

    private Integer warrantyPeriodInMonths;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Boolean reminderEnabled;

    private String notes;

    private String username;
}
