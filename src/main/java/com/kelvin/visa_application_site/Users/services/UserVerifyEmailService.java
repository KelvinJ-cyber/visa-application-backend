package com.kelvin.visa_application_site.Users.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class UserVerifyEmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.brevo.com/v3")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("api-key", apiKey)
                .build();
    }

    public void sendVerificationEmail(String to, String subject, String htmlBody) {
        Map<String, Object> payload = Map.of(
                "sender", Map.of(
                        "name", "Travel Sure Team",
                        "email", "justineikechi6@gmail.com"
                ),
                "to", List.of(
                        Map.of("email", to)
                ),
                "subject", subject,
                "htmlContent", htmlBody
        );
        webClient.post()
                .uri("/smtp/email")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }
}
