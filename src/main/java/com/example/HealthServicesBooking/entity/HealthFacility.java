package com.example.HealthServicesBooking.entity;

import com.example.HealthServicesBooking.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "health_facilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthFacility extends BaseEntity {
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FacilityType type;
    
    @Column(name = "address", nullable = false)
    private String address;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "opening_hours")
    private String openingHours;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    
    public enum FacilityType {
        HOSPITAL,           // Bệnh viện
        CLINIC,             // Phòng khám
        MEDICAL_CENTER,     // Trung tâm y tế
        DIAGNOSTIC_CENTER   // Trung tâm chẩn đoán
    }
}

