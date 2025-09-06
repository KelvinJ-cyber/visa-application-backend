package com.kelvin.visa_application_site.Users.controller;

import com.kelvin.visa_application_site.Users.dto.UserRegisterDto;
import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.services.JwtServices;
import com.kelvin.visa_application_site.Users.services.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/user")
public class UserAuthController {
    private final JwtServices jwtService;

    private final UserAuthService userAuthService;

    public UserAuthController(
            JwtServices jwtService,
            UserAuthService userAuthService
    ){
        this.jwtService = jwtService;
        this.userAuthService = userAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody UserRegisterDto data){
        Users user = userAuthService.register(data);
        return ResponseEntity.ok(user);
    }
}
