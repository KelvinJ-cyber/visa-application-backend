package com.kelvin.visa_application_site.Admin.dto;

import com.kelvin.visa_application_site.enumerated.ApplicationStatus;

public record UpdateStatusRequest(
        ApplicationStatus status,
        String remarks
) {}