package com.example.HealthServicesBooking.repository;

import com.example.HealthServicesBooking.entity.DoctorSchedule;
import com.example.HealthServicesBooking.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    List<DoctorSchedule> findByDoctor(Doctor doctor);
    List<DoctorSchedule> findByDoctorAndIsActiveTrue(Doctor doctor);
    List<DoctorSchedule> findByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek dayOfWeek);
    List<DoctorSchedule> findByDoctorAndDayOfWeekAndIsActiveTrue(Doctor doctor, DayOfWeek dayOfWeek);
}

