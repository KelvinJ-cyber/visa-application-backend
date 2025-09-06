package com.kelvin.visa_application_site.Users.dto;

public record VerifyUserDto(
        String email,
        String verificationCode
) {
}
