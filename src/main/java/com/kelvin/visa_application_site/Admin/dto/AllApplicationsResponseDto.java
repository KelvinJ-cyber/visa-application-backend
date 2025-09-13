package com.kelvin.visa_application_site.Admin.dto;

import com.kelvin.visa_application_site.enumerated.ApplicationStatus;

import java.time.LocalDateTime;

public record AllApplicationsResponseDto(
        int id,
        String firstName,
        String lastName,
        String passportNumber,
        String VisaType,
        String countryOfApplication,
        ApplicationStatus status,
        LocalDateTime dateCreated
) {
}
