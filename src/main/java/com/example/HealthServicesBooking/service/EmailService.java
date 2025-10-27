package com.example.HealthServicesBooking.service;

import com.example.HealthServicesBooking.constant.AppConstant;
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
    
    @Async
    public void sendVerificationEmail(String toEmail, String verificationCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(AppConstant.EMAIL_VERIFICATION_SUBJECT);
            message.setText(buildVerificationEmailBody(verificationCode));
            
            mailSender.send(message);
            log.info("Verification email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", toEmail, e);
        }
    }
    
    @Async
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(AppConstant.PASSWORD_RESET_SUBJECT);
            message.setText(buildPasswordResetEmailBody(resetToken));
            
            mailSender.send(message);
            log.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
        }
    }
    
    @Async
    public void sendAppointmentConfirmation(String toEmail, String patientName, 
                                           String doctorName, String appointmentDate) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(AppConstant.APPOINTMENT_CONFIRMATION_SUBJECT);
            message.setText(buildAppointmentConfirmationBody(patientName, doctorName, appointmentDate));
            
            mailSender.send(message);
            log.info("Appointment confirmation email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send appointment confirmation email to: {}", toEmail, e);
        }
    }
    
    private String buildVerificationEmailBody(String code) {
        return "Xin chào,\n\n" +
               "Cảm ơn bạn đã đăng ký tài khoản tại Health Services Booking!\n\n" +
               "Mã xác thực email của bạn là: " + code + "\n\n" +
               "Mã này sẽ hết hạn sau 5 phút.\n\n" +
               "Nếu bạn không thực hiện đăng ký này, vui lòng bỏ qua email này.\n\n" +
               "Trân trọng,\n" +
               "Health Services Booking Team";
    }
    
    private String buildPasswordResetEmailBody(String token) {
        return "Xin chào,\n\n" +
               "Chúng tôi nhận được yêu cầu khôi phục mật khẩu cho tài khoản của bạn.\n\n" +
               "Mã khôi phục mật khẩu của bạn là: " + token + "\n\n" +
               "Mã này sẽ hết hạn sau 15 phút.\n\n" +
               "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.\n\n" +
               "Trân trọng,\n" +
               "Health Services Booking Team";
    }
    
    private String buildAppointmentConfirmationBody(String patientName, String doctorName, String appointmentDate) {
        return "Xin chào " + patientName + ",\n\n" +
               "Lịch hẹn của bạn đã được xác nhận thành công!\n\n" +
               "Thông tin lịch hẹn:\n" +
               "- Bác sĩ: " + doctorName + "\n" +
               "- Thời gian: " + appointmentDate + "\n\n" +
               "Vui lòng đến đúng giờ hẹn.\n\n" +
               "Trân trọng,\n" +
               "Health Services Booking Team";
    }
}

