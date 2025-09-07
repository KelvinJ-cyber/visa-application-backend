package com.kelvin.visa_application_site.Users.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserDashboard {

    @GetMapping("/home")
    public Map<String, String> greetAdmin() {
        return Map.of("name", "Kelvin", "profession", "Software Engineer");
    }

}
