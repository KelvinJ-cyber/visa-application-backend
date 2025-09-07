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

    private final LocalDateTime uploadedAt = LocalDateTime.now();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String documentType;
    private String fileName;
    private String filePath;
    private String contentType;
    private Long fileSize;
    // relationship with User or Application
    @ManyToOne
    @JoinColumn(name = "application_id")
    private VisaApplications visaApplication;
}
