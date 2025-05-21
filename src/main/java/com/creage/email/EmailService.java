package com.creage.email;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    private final JavaMailSender mailSender;
    

    
    @Value("${app.reset-password.link}")
    private String uiLink;
    
    // Constructor injection

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ✅ OTP Email Method
    public void sendEmail(String email, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            String from = "servicejnesmart@gmail.com";
            String subject = " Verification";
            String content = "<html><body>" +
                    "<p>Dear " + email + ",</p>" +
                    "<p>Please enter this OTP to verify your account:</p>" +
                    "<h3>" + otp + "</h3>" +
                    "<p>Thank you,<br>Creage</p>" +
                    "</body></html>";

            helper.setFrom(from, "Team Creage");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            LOGGER.info("✅ OTP Email sent successfully to: " + email);
        } catch (Exception e) {
            LOGGER.severe("❌ Failed to send OTP email: " + e.getMessage());
        }
    }

    @Async
    public void sendWelcomeEmail(String toEmail, String username, String rawPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            String from = "servicejnesmart@gmail.com";
            String subject = "Welcome to Creage Portal";

            String content = "<html><body>" +
                    "<p>Dear " + username + ",</p>" +
                    "<p>Your account has been created successfully on the Creage portal.</p>" +
                    "<p><b>Login Details:</b></p>" +
                    "<p>Username (Email): <b>" + toEmail + "</b><br>" +
                    "Password: <b>" + rawPassword + "</b></p>" +
                    "<p>Please log in and change your password after first login.</p>" +
                    "<br><p>Thank you,<br>Team Creage</p>" +
                    "</body></html>";

            helper.setFrom(from, "Team Creage");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            LOGGER.info("✅ Welcome email sent successfully to: " + toEmail);
        } catch (Exception e) {
            LOGGER.severe("❌ Failed to send welcome email: " + e.getMessage());
        }
    }
    
    @Async
    public void sendLoginAlertEmail(String toEmail, String username, String loginIp, LocalDateTime loginTime) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            String from = "servicejnesmart@gmail.com";
            String subject = "🔐 Login Alert - Creage Portal";

            String content = "<html><body>" +
                    "<p>Dear " + username + ",</p>" +
                    "<p>This is to inform you that a login to your account was detected.</p>" +
                    "<p><b>Login Details:</b></p>" +
                    "<ul>" +
                    "<li><b>Email:</b> " + toEmail + "</li>" +
                    "<li><b>Login Time:</b> " + loginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</li>" +
                    "<li><b>IP Address:</b> " + loginIp + "</li>" +
                    "</ul>" +
                    "<p>If this login was not you, please reset your password immediately or contact support.</p>" +
                    "<br><p>Thank you,<br>Team Creage</p>" +
                    "</body></html>";

            helper.setFrom(from, "Team Creage");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            LOGGER.info("✅ Login alert email sent to: " + toEmail);
        } catch (Exception e) {
            LOGGER.severe("❌ Failed to send login alert email: " + e.getMessage());
        }
    }
   

    @Async
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            String from = "servicejnesmart@gmail.com";
            String subject = "Password Reset Request";

            // Construct the password reset link (with token)
            String resetLink = uiLink + resetToken;

            String content = "<html><body>" +
                    "<p>Dear User,</p>" +
                    "<p>We received a request to reset your password. Please click the link below to reset your password:</p>" +
                    "<p><a href=\"" + resetLink + "\">Reset Password</a></p>" +
                    "<br><p>If you didn't request this, please ignore this email.</p>" +
                    "<br><p>Thank you,<br>Team Creage</p>" +
                    "</body></html>";

            helper.setFrom(from, "Team Creage");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            LOGGER.info("✅ Password reset email sent successfully to: " + toEmail);
        } catch (Exception e) {
            LOGGER.severe("❌ Failed to send password reset email: " + e.getMessage());
        }
    }

    @Async
    public void sendPasswordResetConfirmationEmail(String toEmail, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            String subject = "Your Creage Portal Password Has Been Reset";
            String content = "<html><body>" +
                    "<p>Dear " + username + ",</p>" +
                    "<p>Your password has been reset successfully.</p>" +
                    "<p>If you did not initiate this request, please contact support immediately.</p>" +
                    "<br><p>Regards,<br>Team Creage</p>" +
                    "</body></html>";

            helper.setFrom("service.digitalannam@gmail.com", "Team Creage");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            LOGGER.info("✅ Password reset confirmation email sent to: " + toEmail);
        } catch (Exception e) {
            LOGGER.severe("❌ Failed to send password reset confirmation email: " + e.getMessage());
        }
    }

    

    }


