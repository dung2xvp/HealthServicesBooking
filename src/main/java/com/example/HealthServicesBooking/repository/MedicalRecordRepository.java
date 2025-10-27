package com.example.HealthServicesBooking.repository;

import com.example.HealthServicesBooking.entity.MedicalRecord;
import com.example.HealthServicesBooking.entity.Patient;
import com.example.HealthServicesBooking.entity.Doctor;
import com.example.HealthServicesBooking.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByAppointment(Appointment appointment);
    List<MedicalRecord> findByPatient(Patient patient);
    List<MedicalRecord> findByDoctor(Doctor doctor);
    List<MedicalRecord> findByPatientOrderByCreatedAtDesc(Patient patient);
}

