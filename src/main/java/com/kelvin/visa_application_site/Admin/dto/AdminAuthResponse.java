package com.kelvin.visa_application_site.Admin.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record AdminAuthResponse(
        String firstName,
        String lastName,
        String token,
        String role,
        String email,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss Z")
        Date expiresAt
) {
}
