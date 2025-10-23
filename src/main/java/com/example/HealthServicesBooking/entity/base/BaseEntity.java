package com.example.HealthServicesBooking.entity.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private String id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "delete_by")
    private String deleteBy;

    @Column(name = "update_by")
    private String updateBy;



    /**
     * Tự động set ID, timestamps và isActive trước khi persist
     */
    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    /**
     * Tự động update timestamp trước khi update
     */
    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}