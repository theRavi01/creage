package com.creage.serviceImpl;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.creage.dto.UserRegistrationDto;
import com.creage.model.Users;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Service 
public class EmailService {

    private final JavaMailSender mailSender;
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    public EmailService(JavaMailSender mailSender) { // ✅ Correctly inject mail sender
        this.mailSender = mailSender;
    }

    public void sendEmail(String email, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            String from = "creage@gmail.com";  
            String to = email;
            String subject = "Account Verification";
            String content = "<html>" +
                    "<body>" +
                    "<p>Dear " + email + ",</p>" +
                    "<p>Please enter this OTP to verify your account:</p>" +
                    "<h3>" + otp + "</h3>" +
                    "<p>Thank you,<br>Creage</p>" +
                    "</body>" +
                    "</html>";

            helper.setFrom(from, "Creage");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // Enable HTML content

            mailSender.send(message);
            LOGGER.info("✅ Email sent successfully to: " + to);
        } catch (Exception e) {
            LOGGER.severe("❌ Failed to send email: " + e.getMessage());
        }
    }
}
