package com.kelvin.visa_application_site.Users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider userAuthenticationProvider;
    private final AuthenticationProvider adminAuthenticationProvider;

    public SecurityConfig(
            AuthenticationProvider userAuthenticationProvider,
            AuthenticationProvider adminAuthenticationProvider,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ){
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.adminAuthenticationProvider = adminAuthenticationProvider;
        this.jwtAuthFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests( authorize -> authorize

                        .requestMatchers("/api/auth/user/**").permitAll()
                        .requestMatchers("/api/auth/admin/**").permitAll()
                        .requestMatchers("/api/user/**").hasRole("USER")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                // Session management configuration
                .sessionManagement(session -> session
                        // Use stateless sessions: no HTTP session will be created or used
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationManager(authenticationManager())

                // Add the JWT filter *before* the UsernamePasswordAuthenticationFilter
                // so JWTs are processed before Spring tries username/password authentication
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    //  Unified AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(userAuthenticationProvider, adminAuthenticationProvider));
    }

    // Defines CORS (Cross-Origin Resource Sharing) rules for frontend-backend communication
    @Bean
    public UrlBasedCorsConfigurationSource corsConfiguration(){
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.setAllowedOrigins(List.of(
                "http://localhost:5173" // React dev server
        ));

        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Register this configuration for all paths (/**)
       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/**", corsConfig);

       return source;
    }
}
