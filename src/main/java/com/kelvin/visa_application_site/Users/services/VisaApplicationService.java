package com.kelvin.visa_application_site.Users.services;

import com.kelvin.visa_application_site.Users.dto.VisaApplicationDto;
import com.kelvin.visa_application_site.Users.dto.VisaApplicationResponseDto;
import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import com.kelvin.visa_application_site.Users.repo.VisaApplicationRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisaApplicationService {

    private final VisaApplicationRepo visaRepo;

    public VisaApplicationService(VisaApplicationRepo visaRepo) {
        this.visaRepo = visaRepo;
    }


    public VisaApplicationResponseDto createApplication(Users user, VisaApplicationDto data) {

        VisaApplications applications = new VisaApplications();
        applications.setUser(user);
        applications.setVisaType(data.visaType());
        applications.setCountryOfApplication(data.countryOfApplication());
        applications.setPassportNumber(data.passportNumber());
        applications.setNationality(data.nationality());
        applications.setCreatedAt(LocalDateTime.now());
        applications.getStatus();

        VisaApplications saved = visaRepo.save(applications);


        return new VisaApplicationResponseDto(
                saved.getApplicationId(),
                saved.getVisaType(),
                saved.getCountryOfApplication(),
                saved.getPassportNumber(),
                saved.getNationality(),
                saved.getMessage(),
                saved.getCreatedAt(),
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
                        app.getStatus()
                ))
                .collect(Collectors.toList());
    }

}
