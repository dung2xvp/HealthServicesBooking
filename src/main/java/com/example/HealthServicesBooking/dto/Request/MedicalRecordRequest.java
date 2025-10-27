package com.example.HealthServicesBooking.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordRequest {
    
    @NotNull(message = "ID lịch hẹn không được để trống")
    private Long appointmentId;
    
    private String diagnosis;
    private String symptoms;
    private String treatment;
    private String prescription;
    private String labResults;
    private String followUpInstructions;
    private String attachments;
    private String notes;
}

