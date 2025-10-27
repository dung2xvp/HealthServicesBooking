package com.example.HealthServicesBooking.controller;

import com.example.HealthServicesBooking.constant.MessageConstant;
import com.example.HealthServicesBooking.dto.response.ApiResponse;
import com.example.HealthServicesBooking.entity.*;
import com.example.HealthServicesBooking.exception.ResourceNotFoundException;
import com.example.HealthServicesBooking.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "APIs quản trị hệ thống")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {
    
    private final UserRepository userRepository;
    private final HealthFacilityRepository facilityRepository;
    private final MedicalServiceRepository medicalServiceRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    
    // User Management
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách người dùng thành công", users));
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_NOT_FOUND));
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin người dùng thành công", user));
    }
    
    @PatchMapping("/users/{id}/activate")
    public ResponseEntity<ApiResponse<User>> toggleUserStatus(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_NOT_FOUND));
        user.setIsActive(!user.getIsActive());
        user = userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái người dùng thành công", user));
    }
    
    // Facility Management
    @GetMapping("/facilities")
    public ResponseEntity<ApiResponse<List<HealthFacility>>> getAllFacilities() {
        List<HealthFacility> facilities = facilityRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách cơ sở y tế thành công", facilities));
    }
    
    @PostMapping("/facilities")
    public ResponseEntity<ApiResponse<HealthFacility>> createFacility(@RequestBody HealthFacility facility) {
        facility = facilityRepository.save(facility);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(MessageConstant.FACILITY_CREATED, facility));
    }
    
    @PutMapping("/facilities/{id}")
    public ResponseEntity<ApiResponse<HealthFacility>> updateFacility(
            @PathVariable Long id, @RequestBody HealthFacility facilityDetails) {
        HealthFacility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.FACILITY_NOT_FOUND));
        
        facility.setName(facilityDetails.getName());
        facility.setType(facilityDetails.getType());
        facility.setAddress(facilityDetails.getAddress());
        facility.setPhoneNumber(facilityDetails.getPhoneNumber());
        facility.setEmail(facilityDetails.getEmail());
        facility.setDescription(facilityDetails.getDescription());
        facility.setOpeningHours(facilityDetails.getOpeningHours());
        facility.setIsActive(facilityDetails.getIsActive());
        
        facility = facilityRepository.save(facility);
        return ResponseEntity.ok(ApiResponse.success(MessageConstant.FACILITY_UPDATED, facility));
    }
    
    @DeleteMapping("/facilities/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFacility(@PathVariable Long id) {
        HealthFacility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.FACILITY_NOT_FOUND));
        facilityRepository.delete(facility);
        return ResponseEntity.ok(ApiResponse.success(MessageConstant.FACILITY_DELETED));
    }
    
    // Medical Service Management
    @GetMapping("/services")
    public ResponseEntity<ApiResponse<List<MedicalService>>> getAllServices() {
        List<MedicalService> services = medicalServiceRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách dịch vụ y tế thành công", services));
    }
    
    @PostMapping("/services")
    public ResponseEntity<ApiResponse<MedicalService>> createService(@RequestBody MedicalService service) {
        service = medicalServiceRepository.save(service);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(MessageConstant.SERVICE_CREATED, service));
    }
    
    @PutMapping("/services/{id}")
    public ResponseEntity<ApiResponse<MedicalService>> updateService(
            @PathVariable Long id, @RequestBody MedicalService serviceDetails) {
        MedicalService service = medicalServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.SERVICE_NOT_FOUND));
        
        service.setName(serviceDetails.getName());
        service.setType(serviceDetails.getType());
        service.setDescription(serviceDetails.getDescription());
        service.setPrice(serviceDetails.getPrice());
        service.setDurationMinutes(serviceDetails.getDurationMinutes());
        service.setIsActive(serviceDetails.getIsActive());
        
        service = medicalServiceRepository.save(service);
        return ResponseEntity.ok(ApiResponse.success(MessageConstant.SERVICE_UPDATED, service));
    }
    
    @DeleteMapping("/services/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        MedicalService service = medicalServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.SERVICE_NOT_FOUND));
        medicalServiceRepository.delete(service);
        return ResponseEntity.ok(ApiResponse.success(MessageConstant.SERVICE_DELETED));
    }
    
    // Doctor Management
    @GetMapping("/doctors")
    public ResponseEntity<ApiResponse<List<Doctor>>> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách bác sĩ thành công", doctors));
    }
    
    @PatchMapping("/doctors/{id}/availability")
    public ResponseEntity<ApiResponse<Doctor>> toggleDoctorAvailability(@PathVariable Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.DOCTOR_NOT_FOUND));
        doctor.setIsAvailable(!doctor.getIsAvailable());
        doctor = doctorRepository.save(doctor);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái bác sĩ thành công", doctor));
    }
    
    // Appointment Management
    @GetMapping("/appointments")
    public ResponseEntity<ApiResponse<List<Appointment>>> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách lịch hẹn thành công", appointments));
    }
    
    @GetMapping("/appointments/status/{status}")
    public ResponseEntity<ApiResponse<List<Appointment>>> getAppointmentsByStatus(@PathVariable String status) {
        Appointment.AppointmentStatus appointmentStatus = Appointment.AppointmentStatus.valueOf(status.toUpperCase());
        List<Appointment> appointments = appointmentRepository.findByStatus(appointmentStatus);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách lịch hẹn thành công", appointments));
    }
}

