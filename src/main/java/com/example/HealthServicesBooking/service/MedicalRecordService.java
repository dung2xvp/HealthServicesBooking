package com.example.HealthServicesBooking.service;

import com.example.HealthServicesBooking.constant.MessageConstant;
import com.example.HealthServicesBooking.dto.request.MedicalRecordRequest;
import com.example.HealthServicesBooking.entity.Appointment;
import com.example.HealthServicesBooking.entity.Doctor;
import com.example.HealthServicesBooking.entity.MedicalRecord;
import com.example.HealthServicesBooking.entity.Patient;
import com.example.HealthServicesBooking.exception.BadRequestException;
import com.example.HealthServicesBooking.exception.ResourceNotFoundException;
import com.example.HealthServicesBooking.exception.UnauthorizedException;
import com.example.HealthServicesBooking.repository.AppointmentRepository;
import com.example.HealthServicesBooking.repository.DoctorRepository;
import com.example.HealthServicesBooking.repository.MedicalRecordRepository;
import com.example.HealthServicesBooking.repository.PatientRepository;
import com.example.HealthServicesBooking.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {
    
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    
    @Transactional
    public MedicalRecord createMedicalRecord(MedicalRecordRequest request) {
        CustomUserDetails userDetails = getCurrentUser();
        
        // Get appointment
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.APPOINTMENT_NOT_FOUND));
        
        // Check if doctor owns this appointment
        Doctor doctor = doctorRepository.findByUserId(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.DOCTOR_NOT_FOUND));
        
        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new UnauthorizedException(MessageConstant.UNAUTHORIZED);
        }
        
        // Check if medical record already exists
        if (medicalRecordRepository.findByAppointment(appointment).isPresent()) {
            throw new BadRequestException("Hồ sơ khám bệnh cho lịch hẹn này đã tồn tại");
        }
        
        // Create medical record
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .appointment(appointment)
                .patient(appointment.getPatient())
                .doctor(doctor)
                .diagnosis(request.getDiagnosis())
                .symptoms(request.getSymptoms())
                .treatment(request.getTreatment())
                .prescription(request.getPrescription())
                .labResults(request.getLabResults())
                .followUpInstructions(request.getFollowUpInstructions())
                .attachments(request.getAttachments())
                .notes(request.getNotes())
                .build();
        
        // Update appointment status to completed
        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
        
        return medicalRecordRepository.save(medicalRecord);
    }
    
    @Transactional
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecordRequest request) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.MEDICAL_RECORD_NOT_FOUND));
        
        CustomUserDetails userDetails = getCurrentUser();
        
        // Check if doctor owns this medical record
        if (!medicalRecord.getDoctor().getUser().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException(MessageConstant.UNAUTHORIZED);
        }
        
        // Update fields
        if (request.getDiagnosis() != null) medicalRecord.setDiagnosis(request.getDiagnosis());
        if (request.getSymptoms() != null) medicalRecord.setSymptoms(request.getSymptoms());
        if (request.getTreatment() != null) medicalRecord.setTreatment(request.getTreatment());
        if (request.getPrescription() != null) medicalRecord.setPrescription(request.getPrescription());
        if (request.getLabResults() != null) medicalRecord.setLabResults(request.getLabResults());
        if (request.getFollowUpInstructions() != null) medicalRecord.setFollowUpInstructions(request.getFollowUpInstructions());
        if (request.getAttachments() != null) medicalRecord.setAttachments(request.getAttachments());
        if (request.getNotes() != null) medicalRecord.setNotes(request.getNotes());
        
        return medicalRecordRepository.save(medicalRecord);
    }
    
    public List<MedicalRecord> getPatientMedicalRecords() {
        CustomUserDetails userDetails = getCurrentUser();
        Patient patient = patientRepository.findByUserId(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.PATIENT_NOT_FOUND));
        
        return medicalRecordRepository.findByPatientOrderByCreatedAtDesc(patient);
    }
    
    public MedicalRecord getMedicalRecordById(Long id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.MEDICAL_RECORD_NOT_FOUND));
        
        CustomUserDetails userDetails = getCurrentUser();
        
        // Check authorization
        if (!isAuthorizedToViewMedicalRecord(medicalRecord, userDetails)) {
            throw new UnauthorizedException(MessageConstant.UNAUTHORIZED);
        }
        
        return medicalRecord;
    }
    
    private CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }
    
    private boolean isAuthorizedToViewMedicalRecord(MedicalRecord record, CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        return record.getPatient().getUser().getId().equals(userId) ||
               record.getDoctor().getUser().getId().equals(userId) ||
               userDetails.getRoleName().equals("ROLE_ADMIN");
    }
}

