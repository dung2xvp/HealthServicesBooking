package com.example.HealthServicesBooking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    /**
     * Send verification email with code
     */
    @Async
    public void sendVerificationEmail(String toEmail, String fullname, String verificationCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Email Verification - Health Services Booking");
            message.setText(buildVerificationEmailContent(fullname, verificationCode));

            mailSender.send(message);
            log.info("Verification email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", toEmail, e);
        }
    }

    /**
     * Send password reset email with code
     */
    @Async
    public void sendPasswordResetEmail(String toEmail, String fullname, String resetCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset Request - Health Services Booking");
            message.setText(buildPasswordResetEmailContent(fullname, resetCode));

            mailSender.send(message);
            log.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
        }
    }

    /**
     * Send welcome email after successful registration
     */
    @Async
    public void sendWelcomeEmail(String toEmail, String fullname) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to Health Services Booking");
            message.setText(buildWelcomeEmailContent(fullname));

            mailSender.send(message);
            log.info("Welcome email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}", toEmail, e);
        }
    }

    /**
     * Build verification email content
     */
    private String buildVerificationEmailContent(String fullname, String code) {
        return String.format("""
                Dear %s,
                
                Thank you for registering with Health Services Booking!
                
                Your verification code is: %s
                
                This code will expire in 5 minutes.
                
                Please enter this code to verify your email address and activate your account.
                
                If you did not create an account, please ignore this email.
                
                Best regards,
                Health Services Booking Team
                """, fullname, code);
    }

    /**
     * Build password reset email content
     */
    private String buildPasswordResetEmailContent(String fullname, String code) {
        return String.format("""
                Dear %s,
                
                We received a request to reset your password for your Health Services Booking account.
                
                Your password reset code is: %s
                
                This code will expire in 5 minutes.
                
                If you did not request a password reset, please ignore this email or contact support if you have concerns.
                
                Best regards,
                Health Services Booking Team
                """, fullname, code);
    }

    /**
     * Build welcome email content
     */
    private String buildWelcomeEmailContent(String fullname) {
        return String.format("""
                Dear %s,
                
                Welcome to Health Services Booking!
                
                Your account has been successfully verified and activated.
                
                You can now:
                - Browse available medical services
                - Book appointments with doctors
                - Manage your health records
                - View your appointment history
                
                If you have any questions or need assistance, please don't hesitate to contact our support team.
                
                Best regards,
                Health Services Booking Team
                """, fullname);
    }
}

