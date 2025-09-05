package com.kelvin.visa_application_site.Admin.controller;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredToken(ExpiredJwtException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Token expired");
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> handleInvalidSignature(SignatureException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid token signature");
    }

    @ExceptionHandler({MalformedJwtException.class, UnsupportedJwtException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleInvalidToken(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(" Invalid token");
    }
}