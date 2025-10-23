package com.example.HealthServicesBooking.controller;

import com.example.HealthServicesBooking.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * GET /api/user/profile
     * Get current user profile (requires authentication)
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        Map<String, Object> response = new HashMap<>();
        response.put("id", currentUser.getId());
        response.put("username", currentUser.getUsername());
        response.put("email", currentUser.getEmail());
        response.put("authorities", currentUser.getAuthorities());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/user/dashboard
     * User dashboard (requires PATIENT role)
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, String>> userDashboard() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to Patient Dashboard");
        return ResponseEntity.ok(response);
    }
}