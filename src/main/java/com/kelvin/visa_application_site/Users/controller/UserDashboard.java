package com.kelvin.visa_application_site.Users.controller;

import com.kelvin.visa_application_site.Admin.dto.NotificationResponse;
import com.kelvin.visa_application_site.Admin.service.NotificationService;
import com.kelvin.visa_application_site.Users.dto.VisaApplicationResponseDto;
import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import com.kelvin.visa_application_site.Users.services.ProfileSettingsServices;
import com.kelvin.visa_application_site.Users.services.VisaApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('USER')")
@CrossOrigin(origins =  "https://visa-application-frontend.vercel.app/")
@RequestMapping("/api/user")
public class UserDashboard {

    public final VisaApplicationService serv;
    private final NotificationService notificationService;
    private final ProfileSettingsServices profileSettingsServices;

    public UserDashboard(
            VisaApplicationService serv,
            NotificationService notificationService,
            ProfileSettingsServices profileSettingsServices
    ) {
        this.serv = serv;
        this.notificationService = notificationService;
        this.profileSettingsServices = profileSettingsServices;
    }

    @GetMapping("/home")
    public ResponseEntity<List<VisaApplicationResponseDto>> getApplications(@AuthenticationPrincipal Users user) {
        return ResponseEntity.ok(serv.getUserApplications(user));
    }

    @GetMapping("/notification")
    public ResponseEntity<List<NotificationResponse>>getNotification( @AuthenticationPrincipal Users user){
        return ResponseEntity.ok(notificationService.getNotification(user));
    }

    @PostMapping("/change-password/{userId}")
    public ResponseEntity<?> changePassword(@PathVariable int userId,@RequestBody Map<String, String> passwordDetails){

        return profileSettingsServices.changePassword(userId, passwordDetails);
    }

    @PostMapping("/{userId}/deactivate")
    public ResponseEntity<?> deactivateAccount(
            @PathVariable int userId,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Users user
    ){
        try{
            String currentPassword = body != null ? body.get("currentPassword") : null;

            profileSettingsServices.deactivateAccount(userId,currentPassword);
            return ResponseEntity.ok(Map.of("message", "Account deactivated. Your account has been deactivated. GoodBye!"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to deactivate account. Try again later."));
        }
    }
}
