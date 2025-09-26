package com.kelvin.visa_application_site.Admin.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        int id,
        String message,
        String type,
        LocalDateTime time,
        boolean read
) {
}