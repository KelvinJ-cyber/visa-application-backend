package com.kelvin.visa_application_site.Users.dto;

public record DocumentUploadDto(
        String documentType,
        String fileName,
        String filePath,
        String contentType,
        Long fileSize
) {
}
