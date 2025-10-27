package com.example.HealthServicesBooking.entity;

import com.example.HealthServicesBooking.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;
    
    @Column(name = "symptoms", columnDefinition = "TEXT")
    private String symptoms;
    
    @Column(name = "treatment", columnDefinition = "TEXT")
    private String treatment;
    
    @Column(name = "prescription", columnDefinition = "TEXT")
    private String prescription;
    
    @Column(name = "lab_results", columnDefinition = "TEXT")
    private String labResults;
    
    @Column(name = "follow_up_instructions", columnDefinition = "TEXT")
    private String followUpInstructions;
    
    @Column(name = "attachments", columnDefinition = "TEXT")
    private String attachments;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}

