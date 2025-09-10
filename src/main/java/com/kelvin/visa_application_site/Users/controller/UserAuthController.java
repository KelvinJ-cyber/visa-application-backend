package com.kelvin.visa_application_site.Users.controller;

import com.kelvin.visa_application_site.Users.dto.UserLoginDto;
import com.kelvin.visa_application_site.Users.dto.UserLoginResponse;
import com.kelvin.visa_application_site.Users.dto.UserRegisterDto;
import com.kelvin.visa_application_site.Users.dto.VerifyUserDto;
import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.services.UserAuthService;
import com.kelvin.visa_application_site.exception.InvalidCodeException;
import com.kelvin.visa_application_site.exception.UserNotFoundException;
import com.kelvin.visa_application_site.exception.VerificationExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/auth/user")
public class UserAuthController {

    private final UserAuthService userAuthService;

    public UserAuthController(
            UserAuthService userAuthService
    ) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody UserRegisterDto data) {

        Users user = userAuthService.register(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyUser(@RequestBody VerifyUserDto data) {
        try {
            userAuthService.verifyUser(data);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "User verified successfully"
            ));

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (VerificationExpiredException e) {
            return ResponseEntity.status(HttpStatus.GONE).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (InvalidCodeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerificationEmail(@RequestParam String email) {
        try {
            userAuthService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification email resent successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto data) {

        try {
            UserLoginResponse authenticatedUser = userAuthService.authenticate(data);

            if (authenticatedUser != null && authenticatedUser.token() != null) {
                return ResponseEntity.ok(authenticatedUser);
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Login failed: invalid credentials");
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + e.getMessage());

        }
    }


}
