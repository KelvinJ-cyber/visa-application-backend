package com.kelvin.visa_application_site.Admin.controller;


import com.kelvin.visa_application_site.Admin.dto.AllApplicationsResponseDto;
import com.kelvin.visa_application_site.Admin.dto.UpdateStatusRequest;
import com.kelvin.visa_application_site.Admin.service.ApplicationServices;
import com.kelvin.visa_application_site.Users.dto.VisaApplicationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@CrossOrigin(origins =  "http://localhost:5173")
@RequestMapping("/api/admin/applications")
public class ApplicationController {

    public final ApplicationServices applicationServices;
    public ApplicationController(ApplicationServices applicationServices){
        this.applicationServices = applicationServices;
    }

    @GetMapping
    public ResponseEntity<List<AllApplicationsResponseDto>> getApplications(@RequestParam(required = false) String status) {

        return ResponseEntity.ok(applicationServices.getApplications(status));

    }

    @PutMapping("/{id}/status")
    public ResponseEntity<VisaApplicationResponseDto> updateStatus(
            @PathVariable int id,
            @RequestBody UpdateStatusRequest request) {

        VisaApplicationResponseDto updated = applicationServices.updateApplicationStatus(id, request);
        return ResponseEntity.ok(updated);
    }

}
