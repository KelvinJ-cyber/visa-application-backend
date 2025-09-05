package com.kelvin.visa_application_site.Admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@RestController
@RequestMapping("/api/admin")
public class AdminDashboard {

    public HashMap<String, String> name() {
        HashMap<String, String> name = new HashMap<>();
        name.put("age", "Kelvin");
        name.put("ge", "Kelvin");
        
        return name;

    }
    @GetMapping("/home")
    public HashMap<String, String> greetAdmin(){
        return name();
    }

}
