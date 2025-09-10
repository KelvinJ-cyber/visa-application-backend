package com.kelvin.visa_application_site.Users.services;

import com.kelvin.visa_application_site.Users.dto.DocumentResponseDto;
import com.kelvin.visa_application_site.Users.model.Documents;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import com.kelvin.visa_application_site.Users.repo.DocumentRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentRepo documentRepo;

    public DocumentService( DocumentRepo documentRepo){
        this.documentRepo = documentRepo;
    }

    private static final String UPLOAD_DIR = "uploads/";

    public DocumentResponseDto uploadDocument(MultipartFile file, VisaApplications applications) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Sanitize filename
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            originalFileName = "unknown";
        }
        String safeFileName = Path.of(originalFileName).getFileName().toString();
        String storedFileName = UUID.randomUUID() + "_" + safeFileName;

        Path uploadPath = Path.of(UPLOAD_DIR);
        if (Files.notExists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path target = uploadPath.resolve(storedFileName).normalize();
        file.transferTo(target.toFile());

        Documents doc = new Documents();
        doc.setDocumentType(file.getContentType());
        doc.setFileName(safeFileName);   // user-friendly
        doc.setFilePath(target.toString()); // system path
        doc.setUploadedAt(LocalDateTime.now());
        doc.setVisaApplication(applications);

        try {
            Documents saved = documentRepo.save(doc);
            return new DocumentResponseDto(
                    saved.getId(),
                    saved.getFileName(),
                    saved.getDocumentType(),
                    saved.getFilePath(),
                    saved.getUploadedAt()
            );
        } catch (Exception e) {
            Files.deleteIfExists(target); // rollback orphaned file
            throw e;
        }
    }

    public List<DocumentResponseDto> getDocumentsByApplication(VisaApplications application) {
        return documentRepo.findByVisaApplication(application)
                .stream()
                .map(doc -> new DocumentResponseDto(
                        doc.getId(),
                        doc.getFileName(),
                        doc.getDocumentType(),
                        doc.getFilePath(),
                        doc.getUploadedAt()
                ))
                .collect(Collectors.toList());
    }


}
