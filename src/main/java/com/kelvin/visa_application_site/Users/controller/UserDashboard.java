package com.kelvin.visa_application_site.Users.controller;

import com.kelvin.visa_application_site.Admin.dto.NotificationResponse;
import com.kelvin.visa_application_site.Admin.service.NotificationService;
import com.kelvin.visa_application_site.Users.dto.VisaApplicationResponseDto;
import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import com.kelvin.visa_application_site.Users.services.VisaApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@PreAuthorize("hasRole('USER')")
@CrossOrigin(origins =  "http://localhost:5173")
@RequestMapping("/api/user")
public class UserDashboard {

    public final VisaApplicationService serv;
    private final NotificationService notificationService;

    public UserDashboard(VisaApplicationService serv, NotificationService notificationService) {
        this.serv = serv;
        this.notificationService = notificationService;
    }

    @GetMapping("/home")
    public ResponseEntity<List<VisaApplicationResponseDto>> getApplications(@AuthenticationPrincipal Users user) {
        return ResponseEntity.ok(serv.getUserApplications(user));
    }

    @GetMapping("/notification")
    public ResponseEntity<List<NotificationResponse>>getNotification( @AuthenticationPrincipal Users user){
        return ResponseEntity.ok(notificationService.getNotification(user));
    }


}
