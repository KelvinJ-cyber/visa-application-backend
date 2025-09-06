package com.kelvin.visa_application_site.Admin.service;

import com.kelvin.visa_application_site.Admin.dto.AdminAuthResponse;
import com.kelvin.visa_application_site.Admin.dto.AdminLoginDto;
import com.kelvin.visa_application_site.Admin.model.Admin;
import com.kelvin.visa_application_site.Admin.repo.AdminRepo;
import com.kelvin.visa_application_site.Users.services.JwtServices;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    private final AdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtServices jwtServices;
    private final AuthenticationManager authenticationManager;


    public AdminAuthService(
            AdminRepo adminRepo,
            @Qualifier("adminPasswordEncoder") PasswordEncoder passwordEncoder,
            JwtServices jwtServices,
            AuthenticationManager authenticationManager
    ) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtServices = jwtServices;
        this.authenticationManager = authenticationManager;
    }


    public AdminAuthResponse authenticateAdmin(AdminLoginDto data) {

        Admin admin = adminRepo.findByEmail(data.email())
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found for:  " + data.email()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        data.email(),
                        data.password()
                )
        );
        String token = jwtServices.generateToken(admin);

        return new AdminAuthResponse(admin.getFirstName(), admin.getLastName(), token, admin.getRole().toString(), admin.getUsername());
    }


}
