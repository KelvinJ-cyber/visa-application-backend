package com.kelvin.visa_application_site.Users.dto;

import java.time.LocalDateTime;

public record DocumentResponseDto(
        String fileName,
        String fileType,
        byte [] data,
        LocalDateTime uploadedAt
) {
}