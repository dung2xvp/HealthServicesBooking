package com.example.HealthServicesBooking.entity;

import com.example.HealthServicesBooking.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity User - Đại diện cho người dùng trong hệ thống
 * Bao gồm: Bệnh nhân, Bác sĩ, Admin
 */
@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    // ==================== THÔNG TIN ĐĂNG NHẬP ====================

    @Column(name = "username", unique = true, length = 255)
    private String username;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    // ==================== THÔNG TIN CÁ NHÂN ====================

    @Column(name = "fullname", length = 255)
    private String fullname;

    /**
     * Gender: 0 = Nữ, 1 = Nam, 2 = Khác
     */
    @Column(name = "gender")
    private Integer gender;

    @Column(name = "mobile", length = 255)
    private String mobile;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "age")
    private Integer age;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "facebook", length = 255)
    private String facebook;

    // ==================== BẢO MẬT & XÁC THỰC ====================

    /**
     * Mã code xác thực (6 chữ số)
     * Dùng cho: Verify email, Reset password
     */
    @Column(name = "code", length = 255)
    private String code;

    /**
     * Thời gian hết hạn của code
     */
    @Column(name = "code_expiry_date")
    private java.time.LocalDateTime codeExpiryDate;

    /**
     * Email đã được xác thực chưa
     */
    @Column(name = "email_verified", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean emailVerified = false;

    /**
     * Điểm vi phạm (số lần không đến khám)
     * Nếu > 3 → Tự động khóa tài khoản
     */
    @Column(name = "bad_point", columnDefinition = "INT DEFAULT 0")
    private Integer badPoint = 0;

    // ==================== QUAN HỆ ====================

    /**
     * Quan hệ Many-to-Many với Role
     * Một user có thể có nhiều roles
     * Một role có thể thuộc nhiều users
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // ==================== HELPER METHODS ====================

    /**
     * Thêm role cho user
     */
    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    /**
     * Xóa role khỏi user
     */
    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    /**
     * Kiểm tra user có role cụ thể không
     */
    public boolean hasRole(String roleName) {
        return this.roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    /**
     * Tăng bad point và tự động khóa nếu > 3
     */
    public void incrementBadPoint() {
        this.badPoint++;
        if (this.badPoint > 3) {
            this.setIsActive(false);
            this.setDeleteBy("SYSTEM_AUTO_LOCK");
        }
    }

    /**
     * Reset bad point (cho admin)
     */
    public void resetBadPoint() {
        this.badPoint = 0;
        if (!this.getIsActive()) {
            this.setIsActive(true);
            this.setDeleteBy(null);
        }
    }

    /**
     * Tính tuổi từ ngày sinh
     */
    public void calculateAge() {
        if (this.birthday != null) {
            this.age = Period.between(this.birthday, LocalDate.now()).getYears();
        }
    }

    /**
     * Lấy tuổi hiện tại (tính real-time từ birthday)
     */
    public Integer getCurrentAge() {
        if (this.birthday != null) {
            return Period.between(this.birthday, LocalDate.now()).getYears();
        }
        return this.age;
    }
}
