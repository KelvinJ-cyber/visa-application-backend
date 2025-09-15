package com.kelvin.visa_application_site.Admin.controller;

import com.kelvin.visa_application_site.Admin.service.ApplicationServices;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private ApplicationServices services;

    @GetMapping("/home")
    public ResponseEntity<Map<String, Long>> greetAdmin() {
        return ResponseEntity.ok(Map.of("All Applications", services.dashboardData()));
    }

}
