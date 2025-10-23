package com.example.HealthServicesBooking.repository;

import com.example.HealthServicesBooking.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    
    Optional<Role> findByName(String name);
    
    Boolean existsByName(String name);
}
