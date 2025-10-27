package com.example.HealthServicesBooking.entity;

import com.example.HealthServicesBooking.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "medical_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalService extends BaseEntity {
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceType type;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private HealthFacility facility;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    
    public enum ServiceType {
        GENERAL_CHECKUP,        // Khám tổng quát
        SPECIALIST_CONSULTATION, // Khám chuyên khoa
        LABORATORY_TEST,        // Xét nghiệm
        DIAGNOSTIC_IMAGING,     // Chẩn đoán hình ảnh (X-quang, CT, MRI)
        VACCINATION,            // Tiêm chủng
        MINOR_SURGERY,          // Phẫu thuật nhỏ
        PHYSICAL_THERAPY,       // Vật lý trị liệu
        DENTAL,                 // Nha khoa
        OTHER                   // Khác
    }
}

