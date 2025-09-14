package com.kelvin.visa_application_site.Admin.controller;

import com.kelvin.visa_application_site.Admin.service.MailService;
import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.repo.UserRepo;
import com.kelvin.visa_application_site.exception.UserNotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/admin/notification")
public class UserNotificationController {

    public final MailService mailService;
    private final UserRepo userRepo;

    public UserNotificationController(MailService mailService, UserRepo userRepo) {
        this.mailService = mailService;
        this.userRepo = userRepo;
    }

    @PostMapping("/email/{userId}")
    public ResponseEntity<?> sendEmail(@PathVariable int userId, @RequestBody Map<String, String> request) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found !"));

        if (!request.containsKey("subject") || !request.containsKey("message")
                || request.get("subject").isBlank() || request.get("message").isBlank()) {
            return ResponseEntity.badRequest().body("Subject and message are required!");
        }

        try {
            mailService.sendApplicationNotification(userId, request);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Email sent to " + user.getUsername()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/broadcastEmail")
    public ResponseEntity<?> sendBroadcastEmail(Map<String, String> request) {

        if (!request.containsKey("subject") || !request.containsKey("message")
                || request.get("subject").isBlank() || request.get("message").isBlank()) {
            return ResponseEntity.badRequest().body("Subject and message are required!");
        }
        try {
            mailService.sendBroadcastEmail(request);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Email sent to all Applicants "
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }

    }
}
