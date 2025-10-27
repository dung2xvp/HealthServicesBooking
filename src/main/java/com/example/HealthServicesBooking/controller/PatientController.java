package com.example.HealthServicesBooking.controller;

import com.example.HealthServicesBooking.constant.MessageConstant;
import com.example.HealthServicesBooking.dto.request.AppointmentRequest;
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
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PATIENT')")
@Tag(name = "Patient", description = "APIs cho bệnh nhân")
@SecurityRequirement(name = "Bearer Authentication")
public class PatientController {
    
    private final AppointmentService appointmentService;
    private final MedicalRecordService medicalRecordService;
    
    @PostMapping("/appointments")
    @Operation(summary = "Đặt lịch hẹn", description = "Tạo lịch hẹn khám bệnh mới")
    public ResponseEntity<ApiResponse<Appointment>> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        Appointment appointment = appointmentService.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(MessageConstant.APPOINTMENT_CREATED, appointment));
    }
    
    @GetMapping("/appointments")
    @Operation(summary = "Xem lịch hẹn của tôi", description = "Lấy danh sách tất cả lịch hẹn của bệnh nhân")
    public ResponseEntity<ApiResponse<List<Appointment>>> getMyAppointments() {
        List<Appointment> appointments = appointmentService.getMyAppointments();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách lịch hẹn thành công", appointments));
    }
    
    @GetMapping("/appointments/{id}")
    public ResponseEntity<ApiResponse<Appointment>> getAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin lịch hẹn thành công", appointment));
    }
    
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        appointmentService.cancelAppointment(id, reason);
        return ResponseEntity.ok(ApiResponse.success(MessageConstant.APPOINTMENT_CANCELLED));
    }
    
    @GetMapping("/medical-records")
    public ResponseEntity<ApiResponse<List<MedicalRecord>>> getMyMedicalRecords() {
        List<MedicalRecord> records = medicalRecordService.getPatientMedicalRecords();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách hồ sơ khám bệnh thành công", records));
    }
    
    @GetMapping("/medical-records/{id}")
    public ResponseEntity<ApiResponse<MedicalRecord>> getMedicalRecord(@PathVariable Long id) {
        MedicalRecord record = medicalRecordService.getMedicalRecordById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin hồ sơ khám bệnh thành công", record));
    }
}

