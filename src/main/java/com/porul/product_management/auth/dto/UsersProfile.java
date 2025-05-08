package com.porul.product_management.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsersProfile
{
    private String firstName;

    private String lastName;

    private String gender;

    private LocalDate dateOfBirth;

    private String username;

    private String mailId;

    private Boolean isMailIdVerified;

    private String phoneNumber;

    private Boolean isPhoneNumberVerified;
}