package com.example.HealthServicesBooking.entity;

import com.example.HealthServicesBooking.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;
    
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    @Column(name = "transaction_id", unique = true)
    private String transactionId;
    
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    
    @Column(name = "notes")
    private String notes;
    
    public enum PaymentMethod {
        CASH,           // Tiền mặt
        CREDIT_CARD,    // Thẻ tín dụng
        DEBIT_CARD,     // Thẻ ghi nợ
        BANK_TRANSFER,  // Chuyển khoản
        VNPAY,          // VNPay
        MOMO,           // MoMo
        OTHER           // Khác
    }
    
    public enum PaymentStatus {
        PENDING,        // Chờ thanh toán
        COMPLETED,      // Đã thanh toán
        FAILED,         // Thất bại
        REFUNDED        // Đã hoàn tiền
    }
}

