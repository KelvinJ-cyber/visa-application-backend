package com.kelvin.visa_application_site.Admin.service;

import com.kelvin.visa_application_site.Admin.dto.AllApplicationsResponseDto;
import com.kelvin.visa_application_site.Admin.dto.UpdateStatusRequest;
import com.kelvin.visa_application_site.Users.dto.VisaApplicationResponseDto;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import com.kelvin.visa_application_site.Users.repo.VisaApplicationRepo;
import com.kelvin.visa_application_site.enumerated.ApplicationStatus;
import com.kelvin.visa_application_site.exception.ApplicationNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationServices {

    public final VisaApplicationRepo visaRepo;
    public ApplicationServices(VisaApplicationRepo visaRepo){
        this.visaRepo = visaRepo;
    }


    // ! Method Out Dated for now !
    public List<AllApplicationsResponseDto> viewApplication(){

        List<VisaApplications> apps = visaRepo.findAll();

        return apps.stream()
                .map(app -> new AllApplicationsResponseDto(
                        app.getApplicationId(),
                        app.getUser().getFirstName(),
                        app.getUser().getLastName(),
                        app.getPassportNumber(),
                        app.getVisaType(),
                        app.getCountryOfApplication(),
                        app.getStatus(),
                        app.getCreatedAt()
                )).toList();

    }


    public List<AllApplicationsResponseDto> getApplicationsByStatus(String status){
        List<VisaApplications> applications;

        if (status == null || status.isBlank()) {
            applications = visaRepo.findAll();
        } else {
            ApplicationStatus applicationStatus = ApplicationStatus.valueOf(status.toUpperCase());
            applications = visaRepo.findByStatus(applicationStatus);
        }
        return applications.stream()
                .map(app -> new AllApplicationsResponseDto(
                        app.getApplicationId(),
                        app.getUser().getFirstName(),
                        app.getUser().getLastName(),
                        app.getPassportNumber(),
                        app.getVisaType(),
                        app.getCountryOfApplication(),
                        app.getStatus(),
                        app.getCreatedAt()
                )).toList();
    }



    public VisaApplicationResponseDto updateApplicationStatus(
            int applicationId,
            UpdateStatusRequest request) {

        VisaApplications app = visaRepo.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found"));

        ApplicationStatus newStatus = request.status();

        app.setStatus(newStatus);
        app.setRemarks(request.remarks());
        app.setUpdatedAt(LocalDateTime.now());

        VisaApplications updated = visaRepo.save(app);

        return new VisaApplicationResponseDto(
                updated.getApplicationId(),
                updated.getVisaType(),
                updated.getCountryOfApplication(),
                updated.getPassportNumber(),
                updated.getNationality(),
                updated.getMessage(),
                updated.getCreatedAt(),
                updated.getRemarks(),
                updated.getStatus()
        );
    }

    public Long dashboardData(){
        return visaRepo.count();
    }
}
