package com.kelvin.visa_application_site.Users.model;

import com.kelvin.visa_application_site.enumerated.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "visa_applications")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisaApplications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int applicationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private String visaType;

    @Column(nullable = false)
    private String countryOfApplication;

    @Column(nullable = false)
    private String passportNumber;

    @Column(nullable = false)
    private String nationality;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    private final LocalDateTime createdAt = LocalDateTime.now();
}
