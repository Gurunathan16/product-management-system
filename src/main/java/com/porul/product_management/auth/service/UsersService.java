package com.porul.product_management.auth.service;

import com.porul.product_management.auth.dto.UsersPasswordChange;
import com.porul.product_management.auth.dto.UsersLogin;
import com.porul.product_management.auth.dto.UsersRegistration;
import com.porul.product_management.auth.dto.UsersUpdate;
import com.porul.product_management.auth.entity.Users;
import com.porul.product_management.auth.dto.TokenRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface UsersService
{

    ResponseEntity<Map<String, Object>> register(@Valid UsersRegistration usersRegistration);

    ResponseEntity<Map<String, Object>> login(UsersLogin usersLogin);

    ResponseEntity<Map<String, Object>> refreshTokens(TokenRequest tokenRequest);

    ResponseEntity<Map<String, Object>> update(UsersUpdate usersUpdate);

    ResponseEntity<Map<String, Object>> updatePassword(@Valid UsersPasswordChange usersPasswordChange);

    ResponseEntity<Map<String, Object>> viewProfile();

    ResponseEntity<String> delete();

    ResponseEntity<Map<String, Object>> responseCookieGenerator(List<String> generatedTokens);

}
