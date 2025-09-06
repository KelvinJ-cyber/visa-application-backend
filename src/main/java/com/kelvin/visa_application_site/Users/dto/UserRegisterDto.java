package com.kelvin.visa_application_site.Users.dto;

public record UserRegisterDto
        (
                String firstName,
                String lastName,
                String email,
                String password
        ) {
}
