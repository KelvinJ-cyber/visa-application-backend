package com.kelvin.visa_application_site.Users.dto;

import com.kelvin.visa_application_site.enumerated.ApplicationStatus;

import java.time.LocalDateTime;

public record VisaApplicationResponseDto(
        int applicationId,
        String visaType,
        String countryOfApplication,
        String passportNumber,
        String nationality,
        String message,
        LocalDateTime createAt,
        ApplicationStatus status
) {
}