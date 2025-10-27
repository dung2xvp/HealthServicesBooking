package com.example.HealthServicesBooking.repository;

import com.example.HealthServicesBooking.entity.Patient;
import com.example.HealthServicesBooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);
    Optional<Patient> findByUserId(Long userId);
}

