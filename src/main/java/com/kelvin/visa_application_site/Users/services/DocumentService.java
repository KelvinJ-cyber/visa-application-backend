package com.kelvin.visa_application_site.Users.services;

import com.kelvin.visa_application_site.Users.dto.DocumentUploadDto;
import com.kelvin.visa_application_site.Users.model.Documents;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import com.kelvin.visa_application_site.Users.repo.DocumentRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepo documentRepo;

    public DocumentService( DocumentRepo documentRepo){
        this.documentRepo = documentRepo;
    }

    public Documents uploadDocument(VisaApplications application, DocumentUploadDto dto) {
        Documents doc = new Documents();
        doc.setDocumentType(dto.documentType());
        doc.setFileName(dto.fileName());
        doc.setFilePath(dto.filePath());
        doc.setContentType(dto.contentType());
        doc.setFileSize(dto.fileSize());
        doc.setVisaApplication(application);

        return documentRepo.save(doc);

        }

    public List<Documents> getDocumentsByApplication(VisaApplications application) {
        return documentRepo.findByVisaApplication(application);
    }

}
