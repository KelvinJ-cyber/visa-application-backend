package com.kelvin.visa_application_site.Users.services;

import com.kelvin.visa_application_site.Users.dto.DocumentResponseDto;
import com.kelvin.visa_application_site.Users.model.Documents;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import com.kelvin.visa_application_site.Users.repo.DocumentRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentRepo documentRepo;

    public DocumentService( DocumentRepo documentRepo){
        this.documentRepo = documentRepo;
    }

    private final String UPLOAD_DIR = "uploads/";

    public DocumentResponseDto uploadDocument(MultipartFile file, VisaApplications applications) throws IOException {

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = UPLOAD_DIR + fileName;

        // Ensure directory exists
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        // Save file to disk
        file.transferTo(new File(filePath));

        Documents doc = new Documents();
        doc.setDocumentType(file.getContentType());
        doc.setFileName(file.getOriginalFilename());
        doc.setFilePath(filePath);
        doc.getUploadedAt();
        doc.setVisaApplication(applications);

        Documents saved = documentRepo.save(doc);

        return new DocumentResponseDto(
                saved.getId(),
                saved.getFileName(),
                saved.getDocumentType(),
                saved.getFilePath(),
                saved.getUploadedAt()
        );
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
