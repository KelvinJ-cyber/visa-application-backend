package com.kelvin.visa_application_site.Users.services;

import com.kelvin.visa_application_site.Admin.service.MailService;
import com.kelvin.visa_application_site.Users.dto.UserLoginDto;
import com.kelvin.visa_application_site.Users.dto.UserLoginResponse;
import com.kelvin.visa_application_site.Users.dto.UserRegisterDto;
import com.kelvin.visa_application_site.Users.dto.VerifyUserDto;
import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.repo.UserRepo;
import com.kelvin.visa_application_site.exception.InvalidCodeException;
import com.kelvin.visa_application_site.exception.UserNotFoundException;
import com.kelvin.visa_application_site.exception.VerificationExpiredException;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class UserAuthService {


    private final UserRepo userRepo;
    private final PasswordEncoder userPasswordEncoder;
    private final UserVerifyEmailService userVerifyEmailService;
    private final AuthenticationManager authenticationManager;
    private final JwtServices jwtServices;
    private final MailService mailService;

    public UserAuthService(
            UserRepo userRepo,
            PasswordEncoder userPasswordEncoder,
            AuthenticationManager authenticationManager,
            UserVerifyEmailService userVerifyEmailService,
            JwtServices jwtServices,
            MailService mailService
    ) {
        this.userRepo = userRepo;
        this.userPasswordEncoder = userPasswordEncoder;
        this.userVerifyEmailService = userVerifyEmailService;
        this.authenticationManager = authenticationManager;
        this.jwtServices = jwtServices;
        this.mailService = mailService;

    }

    public Users register(UserRegisterDto data) {
        Users user = new Users(
                data.email(),
                userPasswordEncoder.encode(data.password()),
                data.firstName(),
                data.lastName()
        );
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationExpiry(LocalDateTime.now().plusMinutes(20));
        user.setEnabled(false);
        sendVerificationEmail(user);
        userRepo.save(user);

        return user;
    }

    private void sendVerificationEmail(Users user) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage =
                "<html>" +
                        "<body style='margin:0; padding:0; font-family: Arial, Helvetica, sans-serif; background-color:#f9f9f9;'>" +
                        "<table align='center' width='100%' cellpadding='0' cellspacing='0' " +
                        "style='max-width:600px; margin:auto; background:#ffffff; border-radius:8px; overflow:hidden; " +
                        "box-shadow:0 4px 12px rgba(0,0,0,0.1);'>" +

                        "<tr>" +
                        "<td style='background-color:#007bff; padding:20px; text-align:center; color:#ffffff;'>" +
                        "<h1 style='margin:0; font-size:24px;'>Welcome to TravelSure </h1>" +
                        "</td>" +
                        "</tr>" +

                        "<tr>" +
                        "<td style='padding:30px; color:#333333;'>" +
                        "<p style='font-size:16px; margin-bottom:20px;'>" +
                        "Hi there, thank you for signing up! To complete your registration, please use the verification code below:" +
                        "</p>" +

                        "<div style='text-align:center; margin:30px 0;'>" +
                        "<span style='display:inline-block; background:#f0f4ff; color:#007bff; font-size:22px; font-weight:bold; " +
                        "padding:15px 30px; border-radius:6px; letter-spacing:2px;'>" +
                        verificationCode +
                        "</span>" +
                        "</div>" +

                        "<p style='font-size:14px; color:#555555;'>" +
                        "This code will expire in <b>10 minutes</b>. If you didn’t request this, you can safely ignore this email." +
                        "</p>" +
                        "</td>" +
                        "</tr>" +

                        "<tr>" +
                        "<td style='background:#f5f5f5; padding:15px; text-align:center; font-size:12px; color:#777777;'>" +
                        "&copy; " + java.time.Year.now() + " Visa. All rights reserved." +
                        "</td>" +
                        "</tr>" +

                        "</table>" +
                        "</body>" +
                        "</html>";
        try {
            userVerifyEmailService.sendVerificationEmail(user.getUsername(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void verifyUser(VerifyUserDto input) {
        Optional<Users> optionalUser = userRepo.findByEmail(input.email());

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found with email: " + input.email());
        }

        Users user = optionalUser.get();

        if (user.getVerificationExpiry().isBefore(LocalDateTime.now())) {
            throw new VerificationExpiredException("Verification code has expired");
        }

        if (!user.getVerificationCode().equals(input.verificationCode())) {
            throw new InvalidCodeException("Invalid verification code");
        }
        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationExpiry(null);
        userRepo.save(user);
    }

    public void resendVerificationCode(String email) {
        Optional<Users> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationExpiry(LocalDateTime.now().plusMinutes(5));
            sendVerificationEmail(user);
            userRepo.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public UserLoginResponse authenticate(UserLoginDto data) {
        Users user = userRepo.findByEmail(data.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified. Please verify your account.");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        data.email(),
                        data.password()
                )
        );
        String token = jwtServices.generateToken(user);

        return new UserLoginResponse(user.getFirstName(), user.getLastName(), user.getRole().toString(), token, jwtServices.extractExpiration(token));

    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    public void sendOtpEmail(String to, String otp) throws MessagingException {
        String subject = "TravelSure | Password reset otp";
        String htmlContent = """
                <html>
                  <body style="font-family: Arial, sans-serif;">
                    <h2>Password Reset OTP</h2>
                    <p>Your OTP code is:</p>
                    <h1 style="color: #2E86C1;">%s</h1>
                    <p>This code will expire in 10 minutes.</p>
                    <p>How could you forget your password ??</p>
                  </body>
                </html>
                """.formatted(otp);
        mailService.sendHtmlMail(to, subject, htmlContent);
    }

    public void requestResetOtp(Map<String, String> request) throws MessagingException {
        String email = request.get("email");

        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String otp = String.format("%06d", new Random().nextInt(99999));
        user.setResetOtp(otp);
        user.setResetOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepo.save(user);

        sendOtpEmail(email, otp);
    }

    public void resetPassword(Map<String, String> request) {
        String email = request.get("email").trim();
        String otp = request.get("otp").trim().toLowerCase();
        String newPassword = request.get("newPassword");

        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!otp.equals(user.getResetOtp())) {
             ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "The OTP you entered is incorrect"));
        }

        if (user.getResetOtpExpiry().isBefore(LocalDateTime.now())) {
             ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "OTP expired"));
        }


        user.setPassword(userPasswordEncoder.encode(newPassword));
        user.setResetOtpExpiry(null);
        user.setResetOtp(null);
        userRepo.save(user);

         ResponseEntity.ok(Map.of(
                "message", "Password reset successful"
        ));
    }
}



