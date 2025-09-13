package com.kelvin.visa_application_site.Users.controller;

import com.kelvin.visa_application_site.Users.dto.VisaApplicationDto;
import com.kelvin.visa_application_site.Users.dto.VisaApplicationResponseDto;
import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import com.kelvin.visa_application_site.Users.repo.UserRepo;
import com.kelvin.visa_application_site.Users.services.VisaApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('USER')")
@CrossOrigin(origins =  "http://localhost:5173")
@RequestMapping("/api/user/applications")
public class VisaApplicationController {

    public final VisaApplicationService applicationService;

    public VisaApplicationController(VisaApplicationService applicationService) {
        this.applicationService = applicationService;

    }

    @PostMapping("/{userId}")
    public ResponseEntity<VisaApplicationResponseDto> createApplication(
            @RequestBody VisaApplicationDto data,
            @PathVariable int userId
    ) {
        VisaApplicationResponseDto responseDto = applicationService.createApplication(userId, data);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<VisaApplicationResponseDto>> getApplications(@AuthenticationPrincipal Users user) {

        return ResponseEntity.ok(applicationService.getUserApplications(user));

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicationById(@PathVariable int id, @AuthenticationPrincipal Users user ){

        return applicationService.getUserApplications(user).stream()
                .filter(app -> app.applicationId() == id)
                .findFirst()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Application not found"));
    }
}
