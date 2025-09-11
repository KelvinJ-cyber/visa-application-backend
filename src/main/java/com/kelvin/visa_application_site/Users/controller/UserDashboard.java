package com.kelvin.visa_application_site.Users.controller;

import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import com.kelvin.visa_application_site.Users.services.VisaApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
@PreAuthorize("hasRole('USER')")
@CrossOrigin(origins =  "http://localhost:5173")
@RequestMapping("/api/user")
public class UserDashboard {

    public final VisaApplicationService serv;

    public UserDashboard(VisaApplicationService serv) {
        this.serv = serv;
    }

    @GetMapping("/home")
    public LinkedHashMap<String, String> greetAdmin(@AuthenticationPrincipal Users user, VisaApplications applications) {

        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put("FirstName", user.getFirstName());
        response.put("LastName", user.getLastName());
        response.put("profession", "Software Engineer");
        response.put("status", applications.getStatus().toString());
        return response;

    }

}
