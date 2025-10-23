package com.example.HealthServicesBooking.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String fullname;
    private String mobile;
    private Integer gender;
    private String address;
    private LocalDate birthday;
    private Integer age;
    private Boolean isActive;
    private Integer badPoint;
    private List<String> roles;
}
