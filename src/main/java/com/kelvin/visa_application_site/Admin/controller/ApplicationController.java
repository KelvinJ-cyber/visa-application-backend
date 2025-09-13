package com.kelvin.visa_application_site.Admin.controller;


import com.kelvin.visa_application_site.Admin.dto.AllApplicationsResponseDto;
import com.kelvin.visa_application_site.Admin.service.ViewAllApplication;
import com.kelvin.visa_application_site.Users.dto.VisaApplicationResponseDto;
import com.kelvin.visa_application_site.Users.model.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@CrossOrigin(origins =  "http://localhost:5173")
@RequestMapping("/api/admin/applications")
public class ApplicationController {

    public final ViewAllApplication viewAllApplication;
    public ApplicationController(ViewAllApplication viewAllApplication){
        this.viewAllApplication = viewAllApplication;
    }

    @GetMapping
    public ResponseEntity<List<AllApplicationsResponseDto>> getApplications() {

        return ResponseEntity.ok(viewAllApplication.viewApplication());

    }
}
