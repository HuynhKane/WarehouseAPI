package com.WarehouseAPI.WarehouseAPI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientStockException(InsufficientStockException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", 400); // You can choose a different status code if needed
        response.put("error", "Insufficient Stock");
        response.put("message", ex.getMessage());
        response.put("path", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
