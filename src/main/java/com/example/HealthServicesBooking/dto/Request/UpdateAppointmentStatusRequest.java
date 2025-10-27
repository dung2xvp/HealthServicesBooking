package com.example.HealthServicesBooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentStatusRequest {
    
    @NotBlank(message = "Trạng thái không được để trống")
    private String status; // CONFIRMED, CANCELLED, COMPLETED, etc.
    
    private String cancellationReason;
    
    private String notes;
}

