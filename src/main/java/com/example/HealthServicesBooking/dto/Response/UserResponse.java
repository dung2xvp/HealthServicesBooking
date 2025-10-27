package com.example.HealthServicesBooking.dto.response;

import com.example.HealthServicesBooking.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private String gender;
    private String address;
    private String avatarUrl;
    private String role;
    private Boolean isEmailVerified;
    private Boolean isActive;
    private LocalDateTime createdAt;
    
    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender() != null ? user.getGender().name() : null)
                .address(user.getAddress())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole().getName())
                .isEmailVerified(user.getIsEmailVerified())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

