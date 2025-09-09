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

    private final LocalDateTime createdAt = LocalDateTime.now();
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
    private final ApplicationStatus status = ApplicationStatus.DRAFT;
    private String message = "Your visa application has been received.";

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getCountryOfApplication() {
        return countryOfApplication;
    }

    public void setCountryOfApplication(String countryOfApplication) {
        this.countryOfApplication = countryOfApplication;
    }

    public String getVisaType() {
        return visaType;
    }

    public void setVisaType(String visaType) {
        this.visaType = visaType;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
