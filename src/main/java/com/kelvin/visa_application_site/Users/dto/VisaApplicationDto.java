package com.kelvin.visa_application_site.Users.dto;

public record VisaApplicationDto(
        String visaType,
        String countryOfApplication,
        String passportNumber,
        String nationality
) {
}
