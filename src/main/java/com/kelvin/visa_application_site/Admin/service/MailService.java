package com.kelvin.visa_application_site.Admin.service;

import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.repo.UserRepo;
import com.kelvin.visa_application_site.exception.UserNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MailService {

    public final JavaMailSender mailSender;
    public final UserRepo userRepo;

    public MailService(
            JavaMailSender mailSender,
            UserRepo userRepo
    ) {
        this.mailSender = mailSender;
        this.userRepo = userRepo;
    }


    public void sendHtmlMail(String to, String subject, String htmlBody) throws MessagingException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendApplicationNotification(int userId, Map<String, String>request) throws MessagingException{
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found !"));

        String subject = request.get("subject");
        String message = request.get("message");

        String htmlBody = """
                <html>
                <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                        <h2 style="color: #2E86C1;">Visa Application Update</h2>
                        <h2 style="color: #4CAF50; text-align: center;">Notification</h2>
                        <p style="font-size: 14px; color: #333;">Hello <b>%s</b>,</p>
                        <p style="font-size: 14px; color: #555;">%s</p>
                        <hr style="margin: 20px 0;">
                        <p style="font-size: 12px; color: gray; text-align: center;">This is an automated message. Please do not reply.</p>
                    </div>
                </body>
                </html>
                """.formatted(user.getFullName(), message);

        sendHtmlMail(user.getUsername(), subject, htmlBody);
    }

    @Async
    public void sendBroadcastEmail(Map<String, String> request){
        String subject = request.get("subject");
        String message = request.get("message");

        List<Users> users = userRepo.findAll();

        for (Users user : users){
            try{
                String htmlContent = """
                        <html>
                          <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                            <h2 style="color: #2E86C1;">Visa Application Update</h2>
                            <p>Hello <b>%s</b>,</p>
                            <p>%s</p>
                            <hr>
                            <p style="font-size: 12px; color: #777;">This is an automated message from the Visa Application System. Please do not reply.</p>
                          </body>
                        </html>
                        """.formatted(user.getFirstName(), message);
                sendHtmlMail(user.getUsername(), subject, htmlContent);
            } catch (MessagingException e) {
                System.err.println("Failed to send email to " + user.getUsername() + ": " + e.getMessage());
            }
        }
    }
}
