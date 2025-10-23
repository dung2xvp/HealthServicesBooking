package com.example.HealthServicesBooking.config;

import com.example.HealthServicesBooking.constant.RoleConstant;
import com.example.HealthServicesBooking.entity.Role;
import com.example.HealthServicesBooking.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initialize default data when application starts
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        initializeRoles();
    }

    /**
     * Initialize default roles if they don't exist
     */
    private void initializeRoles() {
        log.info("Initializing default roles...");

        createRoleIfNotExists(RoleConstant.ROLE_ADMIN);
        createRoleIfNotExists(RoleConstant.ROLE_DOCTOR);
        createRoleIfNotExists(RoleConstant.ROLE_PATIENT);

        log.info("Default roles initialized successfully");
    }

    /**
     * Create role if it doesn't exist
     */
    private void createRoleIfNotExists(String roleName) {
        if (!roleRepository.findByName(roleName).isPresent()) {
            Role role = new Role(roleName);
            roleRepository.save(role);
            log.info("Created role: {}", roleName);
        } else {
            log.info("Role already exists: {}", roleName);
        }
    }
}

