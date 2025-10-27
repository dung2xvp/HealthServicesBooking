package com.example.HealthServicesBooking.repository;

import com.example.HealthServicesBooking.entity.Doctor;
import com.example.HealthServicesBooking.entity.HealthFacility;
import com.example.HealthServicesBooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    Optional<Doctor> findByUserId(Long userId);
    List<Doctor> findByFacility(HealthFacility facility);
    List<Doctor> findByFacilityAndIsAvailableTrue(HealthFacility facility);
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findByIsAvailableTrue();
}

