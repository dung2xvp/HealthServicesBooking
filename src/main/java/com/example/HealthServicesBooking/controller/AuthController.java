package com.example.HealthServicesBooking.controller;

import com.example.HealthServicesBooking.constant.MessageConstant;
import com.example.HealthServicesBooking.dto.request.*;
import com.example.HealthServicesBooking.dto.response.ApiResponse;
import com.example.HealthServicesBooking.dto.response.LoginResponse;
import com.example.HealthServicesBooking.dto.response.UserResponse;
import com.example.HealthServicesBooking.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs cho xác thực và quản lý tài khoản")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    @Operation(summary = "Đăng ký tài khoản mới", description = "Tạo tài khoản mới cho Patient hoặc Doctor")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(MessageConstant.REGISTER_SUCCESS, user));
    }
    
    @PostMapping("/login")
    @Operation(summary = "Đăng nhập", description = "Đăng nhập vào hệ thống và nhận JWT token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(MessageConstant.LOGIN_SUCCESS, response));
    }
    
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request);
        return ResponseEntity.ok(ApiResponse.success(MessageConstant.EMAIL_VERIFIED));
    }
    
    @PostMapping("/resend-verification-code")
    public ResponseEntity<ApiResponse<Void>> resendVerificationCode(@Valid @RequestBody ResendCodeRequest request) {
        authService.resendVerificationCode(request);
        return ResponseEntity.ok(ApiResponse.success(MessageConstant.VERIFICATION_CODE_SENT));
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success(MessageConstant.PASSWORD_RESET_EMAIL_SENT));
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(MessageConstant.PASSWORD_RESET_SUCCESS));
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token đã được làm mới", response));
    }
}

