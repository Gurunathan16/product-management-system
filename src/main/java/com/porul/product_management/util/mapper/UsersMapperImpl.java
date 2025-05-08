package com.porul.product_management.util.mapper;

import com.porul.product_management.auth.dto.UsersProfile;
import com.porul.product_management.auth.dto.UsersRegistration;
import com.porul.product_management.auth.dto.UsersUpdate;
import com.porul.product_management.auth.entity.Users;

import java.time.LocalDate;

public class UsersMapperImpl
{
    public static Users UsersRegistrationToEntity(UsersRegistration user)
    {
        Users newUsers = new Users();

        newUsers.setFirstName(user.getFirstName());
        newUsers.setLastName(user.getLastName());
        newUsers.setGender(user.getGender());
        newUsers.setDateOfBirth(user.getDateOfBirth());
        newUsers.setUsername(user.getUsername());
        newUsers.setPassword(user.getPassword());
        newUsers.setMailId(user.getMailId());
        newUsers.setIsMailIdVerified(true); // Change
        newUsers.setPhoneNumber(user.getPhoneNumber());
        newUsers.setIsPhoneNumberVerified(true); // Change
        newUsers.setIsAccountLocked(false);
        newUsers.setLastLoginDate(LocalDate.now());
        newUsers.setPasswordExpiryDate(LocalDate.now().plusDays(90));


        return newUsers;

    }


    public static Users UsersUpdateToEntity(Users users, UsersUpdate usersUpdate)
    {
        users.setFirstName(usersUpdate.firstName());
        users.setLastName(usersUpdate.lastName());
        users.setGender(usersUpdate.gender());
        users.setDateOfBirth(usersUpdate.dateOfBirth());

        return users;
    }

    public static UsersProfile UsersToUsersProfile(Users users)
    {
        UsersProfile usersProfile = new UsersProfile();

        usersProfile.setFirstName(users.getFirstName());
        usersProfile.setLastName(users.getLastName());
        usersProfile.setUsername(users.getUsername());
        usersProfile.setDateOfBirth(users.getDateOfBirth());
        usersProfile.setGender(users.getGender());
        usersProfile.setMailId(users.getMailId());
        usersProfile.setIsMailIdVerified(users.getIsMailIdVerified());
        usersProfile.setPhoneNumber(users.getPhoneNumber());
        usersProfile.setIsPhoneNumberVerified(users.getIsPhoneNumberVerified());

        return usersProfile;
    }
}
