package com.kelvin.visa_application_site.Users.services;

import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.repo.UserRepo;
import com.kelvin.visa_application_site.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProfileSettingsServices {
    private final UserRepo userRepo;
    private final PasswordEncoder userPasswordEncoder;

    public ProfileSettingsServices(UserRepo userRepo, PasswordEncoder userPasswordEncoder){
        this.userPasswordEncoder = userPasswordEncoder;
        this.userRepo = userRepo;
    }

    public ResponseEntity<?> changePassword(int userId, Map<String, String> passwordDetails ){
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        String recentPassword = passwordDetails.get("recentPassword");
        String newPassword = passwordDetails.get("newPassword");
        String realPassword = user.getPassword();

        boolean securityCheck = userPasswordEncoder.matches(recentPassword,realPassword);
        if (!securityCheck) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Your password is incorrect, Enter your Current password to change"));
        }

        user.setPassword(userPasswordEncoder.encode(newPassword));
        userRepo.save(user);
        return ResponseEntity.ok(Map.of("message", "Password change successful, Logout to confirm"));
    }
}
