package com.porul.product_management.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private String gender;

    private LocalDate dateOfBirth;

    private String username;

    private String password;

    private String mailId;

    private Boolean isMailIdVerified;

    private String phoneNumber;

    private Boolean isPhoneNumberVerified;

    private Boolean isAccountLocked;

    private LocalDate lastLoginDate;

    private LocalDate passwordExpiryDate;

}
