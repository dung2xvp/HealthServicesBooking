package com.example.HealthServicesBooking.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    
    @NotNull(message = "ID bác sĩ không được để trống")
    private Long doctorId;
    
    @NotNull(message = "ID dịch vụ không được để trống")
    private Long serviceId;
    
    @NotNull(message = "ID cơ sở y tế không được để trống")
    private Long facilityId;
    
    @NotNull(message = "Thời gian hẹn không được để trống")
    @Future(message = "Thời gian hẹn phải là thời gian trong tương lai")
    private LocalDateTime appointmentDate;
    
    @NotBlank(message = "Lý do khám không được để trống")
    private String reason;
    
    private String notes;
}

