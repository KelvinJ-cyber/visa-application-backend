package com.kelvin.visa_application_site.Users.dto;

public record UserLoginResponse(
        String token,
        Long expiresAt
) {
}
