package com.kelvin.visa_application_site.Users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record UserLoginResponse(
        String firstName,
        String lastName,
        String role,
        String token,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss Z")
        Date expiresAt
) {
}
