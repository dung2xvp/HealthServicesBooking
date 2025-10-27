package com.example.HealthServicesBooking.entity;

import com.example.HealthServicesBooking.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
    
    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;
    
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    
    @Column(name = "is_email_verified")
    @Builder.Default
    private Boolean isEmailVerified = false;
    
    @Column(name = "email_verification_code")
    private String emailVerificationCode;
    
    @Column(name = "email_verification_expiry")
    private LocalDateTime emailVerificationExpiry;
    
    @Column(name = "reset_password_token")
    private String resetPasswordToken;
    
    @Column(name = "reset_password_expiry")
    private LocalDateTime resetPasswordExpiry;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    
    public enum Gender {
        MALE, FEMALE, OTHER
    }
}

