package com.porul.product_management.util.exception;

import com.porul.product_management.util.response.ResponseEntityHandler;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;

@RestControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request from Client!", "Error", ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandler(NoHandlerFoundException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "Trying to hit non-existing Endpoint 🫠", "Error", ex.getMessage());
    }


    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Map<String, Object>> handlePropertyReference(PropertyReferenceException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request. Check the attributes.", "Error", ex.getMessage());
    }
}
