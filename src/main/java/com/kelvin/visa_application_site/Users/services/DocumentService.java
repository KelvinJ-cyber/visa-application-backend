package com.kelvin.visa_application_site.Users.services;

import com.kelvin.visa_application_site.Users.dto.DocumentResponseDto;
import com.kelvin.visa_application_site.Users.model.Documents;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import com.kelvin.visa_application_site.Users.repo.DocumentRepo;
import com.kelvin.visa_application_site.Users.repo.VisaApplicationRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Service
public class DocumentService {

    private final DocumentRepo documentRepo;
    private final VisaApplicationRepo visaRepo;

    public DocumentService(DocumentRepo documentRepo,VisaApplicationRepo visaRepo) {
        this.documentRepo = documentRepo;
        this.visaRepo = visaRepo;
    }

    public Documents uploadFile(MultipartFile file, int applicationId ) throws Exception{
        VisaApplications app = visaRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application Not Found"));

        Documents doc = new Documents();
        doc.setFileName(file.getOriginalFilename());
        doc.setDocumentType(file.getContentType());
        doc.setData(file.getBytes());
        doc.setUploadedAt(LocalDateTime.now());
        doc.setVisaApplication(app);

        return documentRepo.save(doc);

    }

    public DocumentResponseDto downloadFile(Long documentId) {
        Documents doc = documentRepo.findById(documentId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        return new DocumentResponseDto(
                doc.getFileName(),
                doc.getDocumentType(),
                doc.getData(),
                doc.getUploadedAt()
        );
    }



}
