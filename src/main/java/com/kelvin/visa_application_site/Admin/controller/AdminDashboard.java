package com.kelvin.visa_application_site.Admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@PreAuthorize("hasRole('ADMIN')")
@RestController
@CrossOrigin(origins =  "http://localhost:5173")
@RequestMapping("/api/admin")
public class AdminDashboard {


    @GetMapping("/home")
    public Map<String, String> greetAdmin() {
        return Map.of("name", "Kelvin", "age", "16");
    }

}
