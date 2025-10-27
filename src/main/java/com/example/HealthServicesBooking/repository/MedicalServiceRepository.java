package com.example.HealthServicesBooking.repository;

import com.example.HealthServicesBooking.entity.MedicalService;
import com.example.HealthServicesBooking.entity.HealthFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalServiceRepository extends JpaRepository<MedicalService, Long> {
    List<MedicalService> findByIsActiveTrue();
    List<MedicalService> findByFacility(HealthFacility facility);
    List<MedicalService> findByFacilityAndIsActiveTrue(HealthFacility facility);
    List<MedicalService> findByType(MedicalService.ServiceType type);
    List<MedicalService> findByTypeAndIsActiveTrue(MedicalService.ServiceType type);
}

