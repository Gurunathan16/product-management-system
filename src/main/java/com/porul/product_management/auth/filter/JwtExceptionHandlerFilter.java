package com.porul.product_management.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException ex) {
            buildErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token expired. Try hitting /auth/refreshToken " +
                    "or login again.\n" + ex.getMessage());
        }
        catch (MalformedJwtException ex) {
            buildErrorResponse(response, HttpStatus.BAD_REQUEST, "Invalid token structure.\n" + ex.getMessage());
        }
        catch (UnsupportedJwtException ex) {
            buildErrorResponse(response, HttpStatus.BAD_REQUEST, "Unsupported token format.\n" + ex.getMessage());
        }
        catch (SignatureException ex) {
            buildErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid token signature.\n" + ex.getMessage());
        }
        catch (BadCredentialsException ex) {
            buildErrorResponse(response, HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        catch (JwtException ex) {
            buildErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token processing error.\n" + ex.getMessage());
        }
    }

    private void buildErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        Map<String, Object> responseBody = Map.of(
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message,
                "timestamp", Instant.now().toString()
        );
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}