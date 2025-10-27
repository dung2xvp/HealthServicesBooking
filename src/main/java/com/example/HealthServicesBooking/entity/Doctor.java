package com.example.HealthServicesBooking.entity;

import com.example.HealthServicesBooking.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private HealthFacility facility;
    
    @Column(name = "specialization", nullable = false)
    private String specialization;
    
    @Column(name = "qualification")
    private String qualification;
    
    @Column(name = "experience_years")
    private Integer experienceYears;
    
    @Column(name = "consultation_fee", precision = 10, scale = 2)
    private java.math.BigDecimal consultationFee;
    
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;
    
    @Column(name = "is_available")
    @Builder.Default
    private Boolean isAvailable = true;
}

