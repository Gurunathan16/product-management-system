package com.porul.product_management.auth.service.impl;

import com.porul.product_management.auth.dto.UsersPasswordChange;
import com.porul.product_management.auth.dto.UsersLogin;
import com.porul.product_management.auth.dto.UsersRegistration;
import com.porul.product_management.auth.dto.UsersUpdate;
import com.porul.product_management.auth.entity.Users;
import com.porul.product_management.auth.repository.UsersRepository;
import com.porul.product_management.util.response.ResponseCookieHandler;
import com.porul.product_management.util.response.ResponseEntityHandler;
import com.porul.product_management.auth.dto.TokenRequest;
import com.porul.product_management.auth.service.RedisService;
import com.porul.product_management.auth.service.UsersService;
import com.porul.product_management.util.mapper.UsersMapperImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTServiceImpl jwtService;
    private final RedisService redisService;
    private final int maximumSessions;
    private Users users = null;

    public UsersServiceImpl(UsersRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                            AuthenticationManager authenticationManager, JWTServiceImpl jwtService, RedisService redisService,
                            @Value("${session.maximumSessions}") int maximumSessions)
    {
        this.usersRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.redisService = redisService;
        this.maximumSessions = maximumSessions;
    }

    @Override
    public ResponseEntity<Map<String, Object>> register(@Valid UsersRegistration usersRegistration)
    {

        if(usersRepository.existsByUsername(usersRegistration.getUsername()))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.CONFLICT, "Username already in use.",
                    "Recovery", "Retry with different username.");

        if(usersRepository.existsByMailId(usersRegistration.getMailId()))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.CONFLICT, "MailID already exists",
                    "Recovery", "Try login with existing account.");

        if(!usersRegistration.getPassword().equals(usersRegistration.getConfirmPassword()))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Password - Confirm " +
                    "Password Mismatch", "Recovery", "Password and Confirm Password should be same.");

        usersRegistration.setPassword(bCryptPasswordEncoder.encode(usersRegistration.getPassword()));

        usersRepository.save(UsersMapperImpl.UsersRegistrationToEntity(usersRegistration));

        return ResponseEntityHandler.getResponseEntity(HttpStatus.CREATED, "User registered successfully", "Details", usersRegistration.getFirstName());

    }

    @Override
    public ResponseEntity<Map<String, Object>> login(UsersLogin usersLogin)
    {
        final String username = usersLogin.username();

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                        usersLogin.password()));

        if(authentication.isAuthenticated())
        {
            redisService.evictIfSessionLimitReached(username, maximumSessions);

            List<String> generatedTokens = generateTokens(username);

            redisService.storeRefreshToken(username, generatedTokens.get(1));

            Users users = usersRepository.findByUsername(username);

            if(!users.getLastLoginDate().equals(LocalDate.now()))
            {
                users.setLastLoginDate(LocalDate.now());
                users.setPasswordExpiryDate(LocalDate.now().plusDays(90));

                usersRepository.save(users);
            }

            return responseCookieGenerator(generatedTokens);
        }
        else
            return ResponseEntityHandler.getResponseEntity(HttpStatus.UNAUTHORIZED, "Authentication Failed."
                    , "Recovery", "Check your credentials.");
    }

    @Override
    public ResponseEntity<Map<String, Object>> refreshTokens(TokenRequest tokenRequest)
    {
        String oldRefreshToken = tokenRequest.refreshToken();
        String username = jwtService.extractUsername(oldRefreshToken);

        if(!redisService.isRefreshTokenValid(username, oldRefreshToken))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.UNAUTHORIZED, "Authentication Failed.",
                    "Recovery",
                    "Please login again.");

        redisService.removeRefreshToken(username, oldRefreshToken);

        List<String> generatedTokens = generateTokens(username);

        redisService.storeRefreshToken(username, generatedTokens.get(1));

        return responseCookieGenerator(generatedTokens);
    }

    @Override
    public ResponseEntity<Map<String, Object>> update(UsersUpdate usersUpdate)
    {
        Users users = getUsers();

        usersRepository.save(UsersMapperImpl.UsersUpdateToEntity(users, usersUpdate));

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "User Profile updated successfully", "Details", users.getFirstName());
    }

    @Override
    public ResponseEntity<Map<String, Object>> updatePassword(UsersPasswordChange usersPasswordChange)
    {
        if(!usersPasswordChange.password().equals(usersPasswordChange.confirmPassword()))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Password - Confirm Password Mismatch","Recovery", "Password and Confirm Password should be same.");

        Users users = getUsers();

        if(bCryptPasswordEncoder.matches(usersPasswordChange.oldPassword(), users.getPassword()))
        {
            users.setPassword(bCryptPasswordEncoder.encode(usersPasswordChange.password()));

            usersRepository.save(users);

            updateExpiryDate();

            return ResponseEntityHandler.getResponseEntity(HttpStatus.OK,
                    "Change Password request processed successfully", "Details", users.getFirstName());

        }
        else
            return ResponseEntityHandler.getResponseEntity(HttpStatus.FORBIDDEN, "Old Password - Incorrect", "Recovery", "Enter correct Old Password");

    }

    @Override
    public ResponseEntity<Map<String, Object>> viewProfile()
    {
        Users users = getUsers();

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK,
                "User profile fetch successful",
                "Details", UsersMapperImpl.UsersToUsersProfile(users));
    }

    @Transactional
    @Override
    public ResponseEntity<String> delete()
    {
        String username = getUsernameFromAuth();

        usersRepository.deleteByUsername(username);

        return ResponseEntity.ok("Profile Deleted SuccessFully.");
    }

    private void updateExpiryDate()
    {
        users = getUsers();

        users.setLastLoginDate(LocalDate.now());
        users.setPasswordExpiryDate(LocalDate.now().plusDays(90));

        usersRepository.save(users);
    }

    private Users getUsers()
    {
        String username = getUsernameFromAuth();

        return usersRepository.findByUsername(username);
    }

    private String getUsernameFromAuth()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    @Override
    public ResponseEntity<Map<String, Object>> responseCookieGenerator(List<String> generatedTokens)
    {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", "Success");
        response.put("message", "Tokens generated");
        response.put("payload", jwtService.getClaims(generatedTokens.get(0)));

        ResponseCookie accessCookie = ResponseCookieHandler.responseCookieBuilder("accessToken",
                generatedTokens.get(0),
                true,
                true, "Strict", "/", 900);

        ResponseCookie refreshCookie = ResponseCookieHandler.responseCookieBuilder("refreshToken",
                generatedTokens.get(1),
                true,
                true, "Strict", "/", 604800);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessCookie.toString(),
                refreshCookie.toString()).body(response);
    }

    private List<String> generateTokens(String username)
    {
        String accessToken = jwtService.generateAccessToken(username);

        String refreshToken = jwtService.generateRefreshToken(username);

        return List.of(accessToken, refreshToken);
    }
}
