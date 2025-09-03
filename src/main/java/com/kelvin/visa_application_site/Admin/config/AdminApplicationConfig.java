package com.kelvin.visa_application_site.Admin.config;

import com.kelvin.visa_application_site.Admin.repo.AdminRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminApplicationConfig {

    public final AdminRepo adminRepo;
    public AdminApplicationConfig(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    @Bean
    public UserDetailsService adminDetailsService(){
        return email -> adminRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    @Bean
    public PasswordEncoder adminPasswordEncoder(){
        return new BCryptPasswordEncoder(); // Use BCrypt for password encoding
    }

    @Bean
    public AuthenticationProvider adminAuthenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminDetailsService());
        authProvider.setPasswordEncoder(adminPasswordEncoder());
        return authProvider;
    }

    public AuthenticationManager adminAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
