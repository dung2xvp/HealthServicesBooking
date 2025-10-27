package com.example.HealthServicesBooking.service;

import com.example.HealthServicesBooking.constant.MessageConstant;
import com.example.HealthServicesBooking.constant.RoleConstant;
import com.example.HealthServicesBooking.dto.request.*;
import com.example.HealthServicesBooking.dto.response.LoginResponse;
import com.example.HealthServicesBooking.dto.response.UserResponse;
import com.example.HealthServicesBooking.entity.*;
import com.example.HealthServicesBooking.exception.BadRequestException;
import com.example.HealthServicesBooking.exception.ResourceNotFoundException;
import com.example.HealthServicesBooking.exception.UnauthorizedException;
import com.example.HealthServicesBooking.repository.*;
import com.example.HealthServicesBooking.security.CustomUserDetails;
import com.example.HealthServicesBooking.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;
    
    @Value("${app.verification.code.expiration}")
    private Long verificationCodeExpiration;
    
    @Transactional
    public UserResponse register(RegisterRequest request) {
        // Validate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(MessageConstant.EMAIL_ALREADY_EXISTS);
        }
        
        // Validate phone number
        if (request.getPhoneNumber() != null && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BadRequestException(MessageConstant.PHONE_ALREADY_EXISTS);
        }
        
        // Get role
        String roleName = request.getRole().equalsIgnoreCase("DOCTOR") 
                ? RoleConstant.DOCTOR 
                : RoleConstant.PATIENT;
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.ROLE_NOT_FOUND));
        
        // Generate verification code
        String verificationCode = generateVerificationCode();
        
        // Create user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .isEmailVerified(false)
                .isActive(true)
                .emailVerificationCode(verificationCode)
                .emailVerificationExpiry(LocalDateTime.now().plusSeconds(verificationCodeExpiration / 1000))
                .build();
        
        user = userRepository.save(user);
        
        // Create profile based on role
        if (roleName.equals(RoleConstant.DOCTOR)) {
            Doctor doctor = Doctor.builder()
                    .user(user)
                    .isAvailable(false) // Will be set to true after admin approval
                    .build();
            doctorRepository.save(doctor);
        } else {
            Patient patient = Patient.builder()
                    .user(user)
                    .build();
            patientRepository.save(patient);
        }
        
        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), verificationCode);
        
        return UserResponse.fromUser(user);
    }
    
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        
        // Check if email is verified
        if (!userDetails.isEmailVerified()) {
            throw new UnauthorizedException(MessageConstant.EMAIL_NOT_VERIFIED);
        }
        
        // Check if account is active
        if (!userDetails.isActive()) {
            throw new UnauthorizedException(MessageConstant.ACCOUNT_INACTIVE);
        }
        
        // Generate tokens
        String accessToken = tokenProvider.generateToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_NOT_FOUND));
        
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .user(UserResponse.fromUser(user))
                .build();
    }
    
    @Transactional
    public void verifyEmail(VerifyEmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_NOT_FOUND));
        
        if (user.getIsEmailVerified()) {
            throw new BadRequestException(MessageConstant.EMAIL_ALREADY_VERIFIED);
        }
        
        if (user.getEmailVerificationCode() == null || 
            !user.getEmailVerificationCode().equals(request.getCode())) {
            throw new BadRequestException(MessageConstant.INVALID_VERIFICATION_CODE);
        }
        
        if (user.getEmailVerificationExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException(MessageConstant.INVALID_VERIFICATION_CODE);
        }
        
        user.setIsEmailVerified(true);
        user.setEmailVerificationCode(null);
        user.setEmailVerificationExpiry(null);
        userRepository.save(user);
    }
    
    @Transactional
    public void resendVerificationCode(ResendCodeRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_NOT_FOUND));
        
        if (user.getIsEmailVerified()) {
            throw new BadRequestException(MessageConstant.EMAIL_ALREADY_VERIFIED);
        }
        
        String verificationCode = generateVerificationCode();
        user.setEmailVerificationCode(verificationCode);
        user.setEmailVerificationExpiry(LocalDateTime.now().plusSeconds(verificationCodeExpiration / 1000));
        userRepository.save(user);
        
        emailService.sendVerificationEmail(user.getEmail(), verificationCode);
    }
    
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_NOT_FOUND));
        
        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        user.setResetPasswordExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);
        
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
    }
    
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new BadRequestException(MessageConstant.INVALID_RESET_TOKEN));
        
        if (user.getResetPasswordExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException(MessageConstant.INVALID_RESET_TOKEN);
        }
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiry(null);
        userRepository.save(user);
    }
    
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = tokenProvider.generateTokenFromEmail(
                            user.getEmail(), 
                            user.getId(), 
                            user.getRole().getName()
                    );
                    return LoginResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(request.getRefreshToken())
                            .user(UserResponse.fromUser(user))
                            .build();
                })
                .orElseThrow(() -> new BadRequestException("Refresh token không hợp lệ"));
    }
    
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}

