package com.example.HealthServicesBooking.service;

import com.example.HealthServicesBooking.dto.Request.*;
import com.example.HealthServicesBooking.dto.Response.LoginResponse;
import com.example.HealthServicesBooking.dto.Response.UserResponse;
import com.example.HealthServicesBooking.entity.RefreshToken;
import com.example.HealthServicesBooking.entity.Role;
import com.example.HealthServicesBooking.entity.User;
import com.example.HealthServicesBooking.exception.BadRequestException;
import com.example.HealthServicesBooking.exception.ResourceNotFoundException;
import com.example.HealthServicesBooking.exception.UnauthorizedException;
import com.example.HealthServicesBooking.repository.RoleRepository;
import com.example.HealthServicesBooking.repository.UserRepository;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;

    @Value("${app.verification.code.expiration}")
    private Long verificationCodeExpirationMs;

    /**
     * Login user and return JWT token with refresh token
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT access token
        String accessToken = tokenProvider.generateToken(authentication);

        // Get user info with roles
        User user = userRepository.findByUsernameWithRoles(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Create refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .user(convertToUserResponse(user))
                .build();
    }

    /**
     * Refresh access token using refresh token
     */
    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        log.info("Refresh token request");

        // Find and verify refresh token
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());
        refreshTokenService.verifyExpiration(refreshToken);

        // Get user with roles
        User user = userRepository.findByUsernameWithRoles(refreshToken.getUser().getUsername())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        // Check if user is still active
        if (!user.getIsActive()) {
            throw new UnauthorizedException("User account is disabled");
        }

        // Generate new access token
        CustomUserDetails userDetails = CustomUserDetails.build(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        String newAccessToken = tokenProvider.generateToken(authentication);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .user(convertToUserResponse(user))
                .build();
    }

    /**
     * Logout user by deleting refresh token
     */
    @Transactional
    public void logout(String userId) {
        log.info("Logout request for user: {}", userId);
        refreshTokenService.deleteByUserId(userId);
    }

    /**
     * Register new user
     */
    @Transactional
    public UserResponse register(RegisterRequest request) {
        log.info("Registration attempt for username: {}", request.getUsername());

        // Validate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists!");
        }

        // Validate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists!");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullname(request.getFullname());
        user.setMobile(request.getMobile());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setBirthday(request.getBirthday());
        user.setBadPoint(0);
        
        // Calculate age from birthday
        user.calculateAge();

        // Generate verification code
        String verificationCode = generateVerificationCode();
        user.setCode(verificationCode);
        user.setCodeExpiryDate(LocalDateTime.now().plusSeconds(verificationCodeExpirationMs / 1000));
        user.setEmailVerified(false);

        // Assign default role (PATIENT)
        Role patientRole = roleRepository.findByName("ROLE_PATIENT")
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
        user.addRole(patientRole);

        // Save user (timestamps and isActive will be set by @PrePersist)
        User savedUser = userRepository.save(user);

        log.info("User registered successfully: {}", savedUser.getUsername());

        // Send verification email with code
        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getFullname(), verificationCode);

        return convertToUserResponse(savedUser);
    }

    /**
     * Verify email with code
     */
    @Transactional
    public void verifyEmail(VerifyEmailRequest request) {
        log.info("Email verification attempt for: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // Check if already verified
        if (user.getEmailVerified()) {
            throw new BadRequestException("Email is already verified");
        }

        // Check verification code
        if (user.getCode() == null || !user.getCode().equals(request.getCode())) {
            throw new BadRequestException("Invalid verification code");
        }

        // Check if code expired
        if (user.getCodeExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Verification code has expired. Please request a new code");
        }

        // Verify email
        user.setEmailVerified(true);
        user.setCode(null);
        user.setCodeExpiryDate(null);
        userRepository.save(user);

        log.info("Email verified successfully for: {}", request.getEmail());

        // Send welcome email
        emailService.sendWelcomeEmail(user.getEmail(), user.getFullname());
    }

    /**
     * Resend verification code
     */
    @Transactional
    public void resendVerificationCode(ResendCodeRequest request) {
        log.info("Resend verification code request for: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // Check if already verified
        if (user.getEmailVerified()) {
            throw new BadRequestException("Email is already verified");
        }

        // Generate new verification code
        String newCode = generateVerificationCode();
        user.setCode(newCode);
        user.setCodeExpiryDate(LocalDateTime.now().plusSeconds(verificationCodeExpirationMs / 1000));
        userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), user.getFullname(), newCode);

        log.info("Verification code resent to: {}", request.getEmail());
    }

    /**
     * Request password reset
     */
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        log.info("Forgot password request for: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // Check if account is active
        if (!user.getIsActive()) {
            throw new BadRequestException("Account is disabled. Please contact administrator");
        }

        // Generate reset code
        String resetCode = generateVerificationCode();
        user.setCode(resetCode);
        user.setCodeExpiryDate(LocalDateTime.now().plusSeconds(verificationCodeExpirationMs / 1000));
        userRepository.save(user);

        // Send password reset email
        emailService.sendPasswordResetEmail(user.getEmail(), user.getFullname(), resetCode);

        log.info("Password reset code sent to: {}", request.getEmail());
    }

    /**
     * Reset password with code
     */
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        log.info("Reset password attempt for: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // Check reset code
        if (user.getCode() == null || !user.getCode().equals(request.getCode())) {
            throw new BadRequestException("Invalid reset code");
        }

        // Check if code expired
        if (user.getCodeExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset code has expired. Please request a new code");
        }

        // Reset password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setCode(null);
        user.setCodeExpiryDate(null);
        userRepository.save(user);

        // Delete all refresh tokens for this user (force re-login)
        refreshTokenService.deleteByUserId(user.getId());

        log.info("Password reset successfully for: {}", request.getEmail());
    }

    /**
     * Convert User entity to UserResponse DTO
     */
    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullname(user.getFullname())
                .mobile(user.getMobile())
                .gender(user.getGender())
                .address(user.getAddress())
                .birthday(user.getBirthday())
                .age(user.getAge())
                .isActive(user.getIsActive())
                .badPoint(user.getBadPoint())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .toList())
                .build();
    }

    /**
     * Generate 6-digit verification code
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
