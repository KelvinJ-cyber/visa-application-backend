package com.kelvin.visa_application_site.Admin.controller;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, String>> handleExpiredToken(ExpiredJwtException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Token expired"));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Map<String, String>> handleInvalidSignature(SignatureException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid token signature"));
    }

    @ExceptionHandler({MalformedJwtException.class, UnsupportedJwtException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> handleInvalidToken(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid token"));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, String>> handleGenericJwtError(JwtException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "JWT error: " + ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFoundError(UsernameNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", " " + ex.getMessage()));
    }
}