package com.example.HealthServicesBooking.repository;

import com.example.HealthServicesBooking.entity.Appointment;
import com.example.HealthServicesBooking.entity.Doctor;
import com.example.HealthServicesBooking.entity.Patient;
import com.example.HealthServicesBooking.entity.HealthFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByFacility(HealthFacility facility);
    List<Appointment> findByStatus(Appointment.AppointmentStatus status);
    
    List<Appointment> findByPatientAndStatus(Patient patient, Appointment.AppointmentStatus status);
    List<Appointment> findByDoctorAndStatus(Doctor doctor, Appointment.AppointmentStatus status);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Appointment> findByDoctorAndDateRange(
        @Param("doctor") Doctor doctor,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT a FROM Appointment a WHERE a.patient = :patient AND a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Appointment> findByPatientAndDateRange(
        @Param("patient") Patient patient,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    List<Appointment> findByAppointmentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}

