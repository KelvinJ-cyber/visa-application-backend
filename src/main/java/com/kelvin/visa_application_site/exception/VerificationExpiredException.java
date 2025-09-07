package com.kelvin.visa_application_site.exception;

public class VerificationExpiredException extends RuntimeException {
    public VerificationExpiredException(String message) {
        super(message);
    }
}
