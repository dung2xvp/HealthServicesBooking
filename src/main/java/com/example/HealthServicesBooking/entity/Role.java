package com.example.HealthServicesBooking.entity;

import com.example.HealthServicesBooking.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity Role - Đại diện cho vai trò trong hệ thống
 * VD: ROLE_PATIENT, ROLE_DOCTOR, ROLE_ADMIN
 */
@Entity
@Table(name = "role")
@Data
@EqualsAndHashCode(callSuper = true, exclude = "users")
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    /**
     * Tên vai trò
     * Format: ROLE_XXX (theo convention của Spring Security)
     * VD: ROLE_PATIENT, ROLE_DOCTOR, ROLE_ADMIN
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * Quan hệ Many-to-Many với User
     * mappedBy = "roles" → Bên User sẽ quản lý relationship
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    /**
     * Constructor với name
     */
    public Role(String name) {
        this.name = name;
    }
}
