package com.example.HealthServicesBooking.repository;

import com.example.HealthServicesBooking.entity.Payment;
import com.example.HealthServicesBooking.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByAppointment(Appointment appointment);
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByPaymentStatus(Payment.PaymentStatus status);
    List<Payment> findByPaymentMethod(Payment.PaymentMethod method);
}

