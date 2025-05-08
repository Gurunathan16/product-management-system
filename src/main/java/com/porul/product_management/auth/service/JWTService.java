package com.porul.product_management.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.function.Function;

public interface JWTService {
    String generateAccessToken(String username);

    String generateRefreshToken(String username);

    Boolean verifyToken(String accessToken, UserDetails userDetails);

    String extractUsername(String accessToken);

    Claims getClaims(String accessToken);

    Claims extractClaims(String accessToken);

    Header getHeader(String accessToken);

    <T> T claimResolver(Function<Claims, T> extractClaim);
}
