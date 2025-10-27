package com.example.HealthServicesBooking.service;

import com.example.HealthServicesBooking.constant.MessageConstant;
import com.example.HealthServicesBooking.dto.request.AppointmentRequest;
import com.example.HealthServicesBooking.dto.request.UpdateAppointmentStatusRequest;
import com.example.HealthServicesBooking.entity.*;
import com.example.HealthServicesBooking.exception.BadRequestException;
import com.example.HealthServicesBooking.exception.ResourceNotFoundException;
import com.example.HealthServicesBooking.exception.UnauthorizedException;
import com.example.HealthServicesBooking.repository.*;
import com.example.HealthServicesBooking.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalServiceRepository medicalServiceRepository;
    private final HealthFacilityRepository healthFacilityRepository;
    private final PaymentRepository paymentRepository;
    private final EmailService emailService;
    
    @Transactional
    public Appointment createAppointment(AppointmentRequest request) {
        CustomUserDetails userDetails = getCurrentUser();
        
        // Get patient
        Patient patient = patientRepository.findByUserId(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.PATIENT_NOT_FOUND));
        
        // Get doctor
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.DOCTOR_NOT_FOUND));
        
        // Get service
        MedicalService service = medicalServiceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.SERVICE_NOT_FOUND));
        
        // Get facility
        HealthFacility facility = healthFacilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.FACILITY_NOT_FOUND));
        
        // Check for time conflicts
        LocalDateTime startTime = request.getAppointmentDate();
        LocalDateTime endTime = startTime.plusMinutes(service.getDurationMinutes() != null ? service.getDurationMinutes() : 30);
        
        List<Appointment> conflictingAppointments = appointmentRepository
                .findByDoctorAndDateRange(doctor, startTime, endTime);
        
        if (!conflictingAppointments.isEmpty()) {
            throw new BadRequestException(MessageConstant.APPOINTMENT_TIME_CONFLICT);
        }
        
        // Create appointment
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .service(service)
                .facility(facility)
                .appointmentDate(request.getAppointmentDate())
                .reason(request.getReason())
                .notes(request.getNotes())
                .status(Appointment.AppointmentStatus.PENDING)
                .build();
        
        appointment = appointmentRepository.save(appointment);
        
        // Create payment record
        Payment payment = Payment.builder()
                .appointment(appointment)
                .amount(service.getPrice())
                .paymentStatus(Payment.PaymentStatus.PENDING)
                .paymentMethod(Payment.PaymentMethod.CASH)
                .build();
        paymentRepository.save(payment);
        
        return appointment;
    }
    
    public List<Appointment> getMyAppointments() {
        CustomUserDetails userDetails = getCurrentUser();
        Patient patient = patientRepository.findByUserId(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.PATIENT_NOT_FOUND));
        
        return appointmentRepository.findByPatient(patient);
    }
    
    public List<Appointment> getDoctorAppointments() {
        CustomUserDetails userDetails = getCurrentUser();
        Doctor doctor = doctorRepository.findByUserId(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.DOCTOR_NOT_FOUND));
        
        return appointmentRepository.findByDoctor(doctor);
    }
    
    public Appointment getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.APPOINTMENT_NOT_FOUND));
        
        // Check authorization
        CustomUserDetails userDetails = getCurrentUser();
        if (!isAuthorizedToViewAppointment(appointment, userDetails)) {
            throw new UnauthorizedException(MessageConstant.UNAUTHORIZED);
        }
        
        return appointment;
    }
    
    @Transactional
    public Appointment updateAppointmentStatus(Long id, UpdateAppointmentStatusRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.APPOINTMENT_NOT_FOUND));
        
        CustomUserDetails userDetails = getCurrentUser();
        
        Appointment.AppointmentStatus newStatus = Appointment.AppointmentStatus.valueOf(request.getStatus());
        
        // Validate status transition
        if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED ||
            appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED) {
            throw new BadRequestException(MessageConstant.APPOINTMENT_CANNOT_CANCEL);
        }
        
        appointment.setStatus(newStatus);
        
        if (newStatus == Appointment.AppointmentStatus.CANCELLED) {
            appointment.setCancellationReason(request.getCancellationReason());
            appointment.setCancelledBy(userDetails.getId());
            appointment.setCancelledAt(LocalDateTime.now());
        }
        
        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }
        
        appointment = appointmentRepository.save(appointment);
        
        // Send notification email if confirmed
        if (newStatus == Appointment.AppointmentStatus.CONFIRMED) {
            User patientUser = appointment.getPatient().getUser();
            User doctorUser = appointment.getDoctor().getUser();
            String formattedDate = appointment.getAppointmentDate()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            
            emailService.sendAppointmentConfirmation(
                    patientUser.getEmail(),
                    patientUser.getFullName(),
                    doctorUser.getFullName(),
                    formattedDate
            );
        }
        
        return appointment;
    }
    
    @Transactional
    public void cancelAppointment(Long id, String reason) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.APPOINTMENT_NOT_FOUND));
        
        CustomUserDetails userDetails = getCurrentUser();
        
        // Check if user is patient of this appointment
        if (!appointment.getPatient().getUser().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException(MessageConstant.UNAUTHORIZED);
        }
        
        // Check if appointment can be cancelled
        if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED ||
            appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED) {
            throw new BadRequestException(MessageConstant.APPOINTMENT_CANNOT_CANCEL);
        }
        
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(reason);
        appointment.setCancelledBy(userDetails.getId());
        appointment.setCancelledAt(LocalDateTime.now());
        
        appointmentRepository.save(appointment);
    }
    
    private CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }
    
    private boolean isAuthorizedToViewAppointment(Appointment appointment, CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        return appointment.getPatient().getUser().getId().equals(userId) ||
               appointment.getDoctor().getUser().getId().equals(userId) ||
               userDetails.getRoleName().equals("ROLE_ADMIN");
    }
}

