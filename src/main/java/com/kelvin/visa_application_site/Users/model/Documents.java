package com.kelvin.visa_application_site.Users.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "documents")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String documentType;
    private String fileName;
    private String filePath;
    private final LocalDateTime uploadedAt = LocalDateTime.now();


    // relationship with User or Application
    @ManyToOne
    @JoinColumn(name = "application_id")
    private VisaApplications visaApplication;

    public VisaApplications getVisaApplication() {
        return visaApplication;
    }

    public void setVisaApplication(VisaApplications visaApplication) {
        this.visaApplication = visaApplication;
    }


    public void setFileSize(Long fileSize) {
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}
