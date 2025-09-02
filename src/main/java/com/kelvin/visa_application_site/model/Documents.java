package com.kelvin.visa_application_site.model;

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
    private String contentType;
    private Long fileSize;

    private final LocalDateTime uploadedAt = LocalDateTime.now();

    // relationship with User or Application
    @ManyToOne
    @JoinColumn(name = "application_id")
    private VisaApplications visaApplication;
}
