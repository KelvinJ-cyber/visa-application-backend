package com.kelvin.visa_application_site.Users.dto;

import java.time.LocalDateTime;

public record DocumentResponseDto(
        Long id,
        String fileName,
        String fileType,
        String filePath,
        LocalDateTime uploadedAt
) {
}