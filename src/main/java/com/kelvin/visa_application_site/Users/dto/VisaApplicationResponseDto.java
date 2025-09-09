package com.kelvin.visa_application_site.Users.dto;

import com.kelvin.visa_application_site.enumerated.ApplicationStatus;

public record VisaApplicationResponseDto(
        int applicationId,
        String visaType,
        String countryOfApplication,
        String passportNumber,
        String nationality,
        String message,
        ApplicationStatus status
) {}