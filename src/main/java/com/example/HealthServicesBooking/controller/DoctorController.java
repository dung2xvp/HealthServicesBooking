package com.example.HealthServicesBooking.controller;

import com.example.HealthServicesBooking.constant.MessageConstant;
import com.example.HealthServicesBooking.dto.request.MedicalRecordRequest;
import com.example.HealthServicesBooking.dto.request.UpdateAppointmentStatusRequest;
import com.example.HealthServicesBooking.dto.response.ApiResponse;
import com.example.HealthServicesBooking.entity.Appointment;
import com.example.HealthServicesBooking.entity.MedicalRecord;
import com.example.HealthServicesBooking.service.AppointmentService;
import com.example.HealthServicesBooking.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOCTOR')")
@Tag(name = "Doctor", description = "APIs cho bác sĩ")
@SecurityRequirement(name = "Bearer Authentication")
public class DoctorController {
    
    private final AppointmentService appointmentService;
    private final MedicalRecordService medicalRecordService;
    
    @GetMapping("/appointments")
    public ResponseEntity<ApiResponse<List<Appointment>>> getMyAppointments() {
        List<Appointment> appointments = appointmentService.getDoctorAppointments();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách lịch hẹn thành công", appointments));
    }
    
    @GetMapping("/appointments/{id}")
    public ResponseEntity<ApiResponse<Appointment>> getAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin lịch hẹn thành công", appointment));
    }
    
    @PatchMapping("/appointments/{id}/status")
    public ResponseEntity<ApiResponse<Appointment>> updateAppointmentStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        Appointment appointment = appointmentService.updateAppointmentStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(MessageConstant.APPOINTMENT_UPDATED, appointment));
    }
    
    @PostMapping("/medical-records")
    public ResponseEntity<ApiResponse<MedicalRecord>> createMedicalRecord(
            @Valid @RequestBody MedicalRecordRequest request) {
        MedicalRecord record = medicalRecordService.createMedicalRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(MessageConstant.MEDICAL_RECORD_CREATED, record));
    }
    
    @PutMapping("/medical-records/{id}")
    public ResponseEntity<ApiResponse<MedicalRecord>> updateMedicalRecord(
            @PathVariable Long id,
            @Valid @RequestBody MedicalRecordRequest request) {
        MedicalRecord record = medicalRecordService.updateMedicalRecord(id, request);
        return ResponseEntity.ok(ApiResponse.success(MessageConstant.MEDICAL_RECORD_UPDATED, record));
    }
    
    @GetMapping("/medical-records/{id}")
    public ResponseEntity<ApiResponse<MedicalRecord>> getMedicalRecord(@PathVariable Long id) {
        MedicalRecord record = medicalRecordService.getMedicalRecordById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin hồ sơ khám bệnh thành công", record));
    }
}

