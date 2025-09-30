package com.kelvin.visa_application_site.Users.services;

import com.kelvin.visa_application_site.Users.dto.SubmitApplicationResponse;
import com.kelvin.visa_application_site.Users.dto.VisaApplicationDto;
import com.kelvin.visa_application_site.Users.dto.VisaApplicationResponseDto;
import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import com.kelvin.visa_application_site.Users.repo.UserRepo;
import com.kelvin.visa_application_site.Users.repo.VisaApplicationRepo;
import com.kelvin.visa_application_site.enumerated.ApplicationStatus;
import com.kelvin.visa_application_site.exception.ApplicationNotFoundException;
import com.kelvin.visa_application_site.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisaApplicationService {

    private final VisaApplicationRepo visaRepo;
    public final UserRepo userRepo;

    public VisaApplicationService(VisaApplicationRepo visaRepo, UserRepo userRepo) {
        this.visaRepo = visaRepo;
        this.userRepo = userRepo;
    }


    public VisaApplicationResponseDto createApplication(int userId, VisaApplicationDto data) {

        VisaApplications applications = new VisaApplications();
         Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        applications.setUser(user);
        applications.setVisaType(data.visaType());
        applications.setCountryOfApplication(data.countryOfApplication());
        applications.setPassportNumber(data.passportNumber());
        applications.setNationality(data.nationality());
        applications.setCreatedAt(LocalDateTime.now());

        VisaApplications saved = visaRepo.save(applications);


        return new VisaApplicationResponseDto(
                saved.getApplicationId(),
                saved.getVisaType(),
                saved.getCountryOfApplication(),
                saved.getPassportNumber(),
                saved.getNationality(),
                saved.getMessage(),
                saved.getCreatedAt(),
                saved.getRemarks(),
                saved.getStatus()
        );

    }

    public List<VisaApplicationResponseDto> getUserApplications(Users user) {
        return visaRepo.findByUser(user).stream()
                .map(app -> new VisaApplicationResponseDto(
                        app.getApplicationId(),
                        app.getVisaType(),
                        app.getCountryOfApplication(),
                        app.getPassportNumber(),
                        app.getNationality(),
                        app.getMessage(),
                        app.getCreatedAt(),
                        app.getRemarks(),
                        app.getStatus()
                ))
                .collect(Collectors.toList());
    }

    public VisaApplications findApplicationById(int id){
              return visaRepo.findById(id).orElseThrow(() -> new ApplicationNotFoundException("Application Not Found"));
    }

    public SubmitApplicationResponse submitApplication(int applicationId){
        VisaApplications application = visaRepo.findById(applicationId)
                .orElseThrow( () -> new ApplicationNotFoundException("Application not found"));

        application.setStatus(ApplicationStatus.SUBMITTED);
        String message = "Thank you. Your application and documents have been submitted successfully.";

        VisaApplications submitted = visaRepo.save(application);

        return  new SubmitApplicationResponse(
                message
        );
    }

}
