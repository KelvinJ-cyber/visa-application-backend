package com.kelvin.visa_application_site.Admin.service;

import com.kelvin.visa_application_site.Admin.dto.AllApplicationsResponseDto;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import com.kelvin.visa_application_site.Users.repo.VisaApplicationRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ViewAllApplication {

    public final VisaApplicationRepo visaRepo;
    public ViewAllApplication(VisaApplicationRepo visaRepo){
        this.visaRepo = visaRepo;
    }

    public List<AllApplicationsResponseDto> viewApplication(){

        List<VisaApplications> apps = visaRepo.findAll();

        return apps.stream()
                .map(app -> new AllApplicationsResponseDto(
                        app.getUser().getFirstName(),
                        app.getUser().getLastName(),
                        app.getPassportNumber(),
                        app.getVisaType(),
                        app.getCountryOfApplication(),
                        app.getStatus(),
                        app.getCreatedAt()
                ))
                .collect(Collectors.toList());

    }
}
