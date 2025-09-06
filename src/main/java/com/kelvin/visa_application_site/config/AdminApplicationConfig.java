package com.kelvin.visa_application_site.Users.config;

import com.kelvin.visa_application_site.Admin.service.AdminDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminApplicationConfig {

    private final AdminDetailsService adminDetailsService;

    public AdminApplicationConfig(AdminDetailsService adminDetailsService) {
        this.adminDetailsService = adminDetailsService;
    }


    @Bean
    public PasswordEncoder adminPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminDetailsService);
        authProvider.setPasswordEncoder(adminPasswordEncoder());
        return authProvider;
    }
}