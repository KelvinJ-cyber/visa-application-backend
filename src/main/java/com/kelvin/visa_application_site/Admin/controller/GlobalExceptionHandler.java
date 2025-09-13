package com.kelvin.visa_application_site.Admin.controller;

import com.kelvin.visa_application_site.exception.ApplicationNotFoundException;
import com.kelvin.visa_application_site.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredToken(ExpiredJwtException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "Token expired");
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidSignature(SignatureException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "Invalid token signature");
    }

    @ExceptionHandler({MalformedJwtException.class, UnsupportedJwtException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, Object>> handleInvalidToken(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", "Invalid token");
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, Object>> handleGenericJwtError(JwtException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "JWT error: " + ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundError(UsernameNotFoundException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "User Not Found", ex.getMessage());
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleApplicationNotFound(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Application Not Found", ex.getMessage());
    }
}
