package com.porul.product_management.util.exception;

import com.porul.product_management.util.response.ResponseEntityHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.UNAUTHORIZED, "Bad Credentials. " +
                        "Authentication failed.","Validation Error", "Invalid username or password");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, Object>> handleDisabled(DisabledException ex)
    {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.FORBIDDEN, "Account is disabled.",
                "recovery", "Verify Email to re-enable.");
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Map<String, Object>> handleLocked(LockedException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.FORBIDDEN, "Account is locked.",
                "recovery", "Maximum Password limit try reached. Continue Reset Password.");
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleExpired(AccountExpiredException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.FORBIDDEN, "Account is Expired.",
                "recovery", "Continue Reset Password. Verify Email then.");
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleCredentialsExpired(CredentialsExpiredException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.FORBIDDEN, "Password Expired.",
                "recovery", "Continue Reset Password.");
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<Map<String, Object>> handleServiceError(AuthenticationServiceException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication " +
                        "service error.", "recovery", "Try again after sometime.");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundError(AuthenticationServiceException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.UNAUTHORIZED, "Username not found.",
                "recovery", ex);
    }

    // Catch-all fallback
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuth(AuthenticationException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.UNAUTHORIZED, "Authentication failed.", "details", ex.getMessage());
    }
}
