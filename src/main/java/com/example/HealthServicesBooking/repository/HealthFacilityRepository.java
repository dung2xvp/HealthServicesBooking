package com.example.HealthServicesBooking.repository;

import com.example.HealthServicesBooking.entity.HealthFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthFacilityRepository extends JpaRepository<HealthFacility, Long> {
    List<HealthFacility> findByIsActiveTrue();
    List<HealthFacility> findByType(HealthFacility.FacilityType type);
    List<HealthFacility> findByTypeAndIsActiveTrue(HealthFacility.FacilityType type);
}

