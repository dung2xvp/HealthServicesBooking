package com.example.HealthServicesBooking.controller;

import com.example.HealthServicesBooking.dto.Request.*;
import com.example.HealthServicesBooking.dto.Response.LoginResponse;
import com.example.HealthServicesBooking.dto.Response.UserResponse;
import com.example.HealthServicesBooking.security.CustomUserDetails;
import com.example.HealthServicesBooking.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Register new user endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResponse = authService.register(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully. Please check your email for verification code.");
        response.put("user", userResponse);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/auth/verify-email
     * Verify email with code
     */
    @PostMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Email verified successfully. Your account is now active.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/resend-code
     * Resend verification code
     */
    @PostMapping("/resend-code")
    public ResponseEntity<Map<String, String>> resendVerificationCode(@Valid @RequestBody ResendCodeRequest request) {
        authService.resendVerificationCode(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Verification code has been resent to your email.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/login
     * Login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/refresh-token
     * Refresh access token using refresh token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/logout
     * Logout user by deleting refresh token
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            authService.logout(userDetails.getId());
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/forgot-password
     * Request password reset
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset code has been sent to your email.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/reset-password
     * Reset password with code
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password has been reset successfully. Please login with your new password.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/auth/test
     * Test endpoint
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Auth API is working!");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
