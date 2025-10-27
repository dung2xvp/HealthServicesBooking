package com.example.HealthServicesBooking.config;

import com.example.HealthServicesBooking.constant.RoleConstant;
import com.example.HealthServicesBooking.entity.*;
import com.example.HealthServicesBooking.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final HealthFacilityRepository facilityRepository;
    private final MedicalServiceRepository serviceRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Bean
    public CommandLineRunner initData() {
        return args -> {
            log.info("Starting data initialization...");
            
            // Initialize Roles
            initRoles();
            
            // Initialize Users
            initUsers();
            
            // Initialize Health Facilities
            initHealthFacilities();
            
            // Initialize Medical Services
            initMedicalServices();
            
            // Initialize Doctor Schedules
            initDoctorSchedules();
            
            log.info("Data initialization completed!");
        };
    }
    
    private void initRoles() {
        if (roleRepository.count() == 0) {
            log.info("Initializing roles...");
            
            Role adminRole = Role.builder()
                    .name(RoleConstant.ADMIN)
                    .description("Administrator with full access")
                    .build();
            
            Role doctorRole = Role.builder()
                    .name(RoleConstant.DOCTOR)
                    .description("Medical doctor")
                    .build();
            
            Role patientRole = Role.builder()
                    .name(RoleConstant.PATIENT)
                    .description("Patient user")
                    .build();
            
            roleRepository.save(adminRole);
            roleRepository.save(doctorRole);
            roleRepository.save(patientRole);
            
            log.info("Roles initialized successfully");
        }
    }
    
    private void initUsers() {
        if (userRepository.count() == 0) {
            log.info("Initializing users...");
            
            Role adminRole = roleRepository.findByName(RoleConstant.ADMIN).orElseThrow();
            Role doctorRole = roleRepository.findByName(RoleConstant.DOCTOR).orElseThrow();
            Role patientRole = roleRepository.findByName(RoleConstant.PATIENT).orElseThrow();
            
            // Create Admin User
            User admin = User.builder()
                    .email("admin@healthservices.com")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Administrator")
                    .phoneNumber("0123456789")
                    .role(adminRole)
                    .isEmailVerified(true)
                    .isActive(true)
                    .gender(User.Gender.OTHER)
                    .build();
            userRepository.save(admin);
            
            // Create Doctor User
            User doctorUser = User.builder()
                    .email("doctor@healthservices.com")
                    .password(passwordEncoder.encode("doctor123"))
                    .fullName("Dr. Nguyễn Văn A")
                    .phoneNumber("0987654321")
                    .dateOfBirth(LocalDateTime.of(1980, 5, 15, 0, 0))
                    .role(doctorRole)
                    .isEmailVerified(true)
                    .isActive(true)
                    .gender(User.Gender.MALE)
                    .address("Hà Nội, Việt Nam")
                    .build();
            doctorUser = userRepository.save(doctorUser);
            
            // Create Doctor Profile
            HealthFacility facility = facilityRepository.findAll().stream().findFirst().orElse(null);
            if (facility != null) {
                Doctor doctor = Doctor.builder()
                        .user(doctorUser)
                        .facility(facility)
                        .specialization("Tim mạch")
                        .qualification("Bác sĩ chuyên khoa I")
                        .experienceYears(10)
                        .consultationFee(new BigDecimal("300000"))
                        .bio("Bác sĩ có 10 năm kinh nghiệm trong lĩnh vực tim mạch")
                        .isAvailable(true)
                        .build();
                doctorRepository.save(doctor);
            }
            
            // Create Patient User
            User patientUser = User.builder()
                    .email("patient@healthservices.com")
                    .password(passwordEncoder.encode("patient123"))
                    .fullName("Trần Thị B")
                    .phoneNumber("0912345678")
                    .dateOfBirth(LocalDateTime.of(1990, 8, 20, 0, 0))
                    .role(patientRole)
                    .isEmailVerified(true)
                    .isActive(true)
                    .gender(User.Gender.FEMALE)
                    .address("TP. Hồ Chí Minh, Việt Nam")
                    .build();
            patientUser = userRepository.save(patientUser);
            
            // Create Patient Profile
            Patient patient = Patient.builder()
                    .user(patientUser)
                    .bloodGroup("O+")
                    .allergies("Không có")
                    .medicalHistory("Khỏe mạnh")
                    .emergencyContactName("Trần Văn C")
                    .emergencyContactPhone("0901234567")
                    .build();
            patientRepository.save(patient);
            
            log.info("Users initialized successfully");
            log.info("Admin credentials: admin@healthservices.com / admin123");
            log.info("Doctor credentials: doctor@healthservices.com / doctor123");
            log.info("Patient credentials: patient@healthservices.com / patient123");
        }
    }
    
    private void initHealthFacilities() {
        if (facilityRepository.count() == 0) {
            log.info("Initializing health facilities...");
            
            HealthFacility hospital1 = HealthFacility.builder()
                    .name("Bệnh viện Đa khoa Trung ương")
                    .type(HealthFacility.FacilityType.HOSPITAL)
                    .address("123 Đường ABC, Quận 1, TP. Hồ Chí Minh")
                    .phoneNumber("0281234567")
                    .email("contact@hospital1.com")
                    .description("Bệnh viện đa khoa hàng đầu với đầy đủ chuyên khoa")
                    .openingHours("24/7")
                    .isActive(true)
                    .build();
            
            HealthFacility clinic1 = HealthFacility.builder()
                    .name("Phòng khám Đa khoa Sài Gòn")
                    .type(HealthFacility.FacilityType.CLINIC)
                    .address("456 Đường XYZ, Quận 3, TP. Hồ Chí Minh")
                    .phoneNumber("0287654321")
                    .email("contact@clinic1.com")
                    .description("Phòng khám chất lượng cao với đội ngũ bác sĩ giàu kinh nghiệm")
                    .openingHours("7:00 - 21:00 (Thứ 2 - Chủ nhật)")
                    .isActive(true)
                    .build();
            
            HealthFacility medicalCenter = HealthFacility.builder()
                    .name("Trung tâm Y tế Quận 1")
                    .type(HealthFacility.FacilityType.MEDICAL_CENTER)
                    .address("789 Đường DEF, Quận 1, TP. Hồ Chí Minh")
                    .phoneNumber("0289876543")
                    .email("contact@medicalcenter.com")
                    .description("Trung tâm y tế cộng đồng")
                    .openingHours("7:00 - 17:00 (Thứ 2 - Thứ 6)")
                    .isActive(true)
                    .build();
            
            facilityRepository.save(hospital1);
            facilityRepository.save(clinic1);
            facilityRepository.save(medicalCenter);
            
            log.info("Health facilities initialized successfully");
        }
    }
    
    private void initMedicalServices() {
        if (serviceRepository.count() == 0) {
            log.info("Initializing medical services...");
            
            HealthFacility facility = facilityRepository.findAll().stream().findFirst().orElseThrow();
            
            MedicalService generalCheckup = MedicalService.builder()
                    .name("Khám tổng quát")
                    .type(MedicalService.ServiceType.GENERAL_CHECKUP)
                    .description("Khám sức khỏe tổng quát định kỳ")
                    .price(new BigDecimal("200000"))
                    .durationMinutes(30)
                    .facility(facility)
                    .isActive(true)
                    .build();
            
            MedicalService cardiology = MedicalService.builder()
                    .name("Khám tim mạch")
                    .type(MedicalService.ServiceType.SPECIALIST_CONSULTATION)
                    .description("Khám và tư vấn các vấn đề về tim mạch")
                    .price(new BigDecimal("300000"))
                    .durationMinutes(45)
                    .facility(facility)
                    .isActive(true)
                    .build();
            
            MedicalService bloodTest = MedicalService.builder()
                    .name("Xét nghiệm máu tổng quát")
                    .type(MedicalService.ServiceType.LABORATORY_TEST)
                    .description("Xét nghiệm các chỉ số máu cơ bản")
                    .price(new BigDecimal("150000"))
                    .durationMinutes(15)
                    .facility(facility)
                    .isActive(true)
                    .build();
            
            MedicalService xray = MedicalService.builder()
                    .name("Chụp X-quang")
                    .type(MedicalService.ServiceType.DIAGNOSTIC_IMAGING)
                    .description("Chụp X-quang các bộ phận")
                    .price(new BigDecimal("250000"))
                    .durationMinutes(20)
                    .facility(facility)
                    .isActive(true)
                    .build();
            
            MedicalService vaccination = MedicalService.builder()
                    .name("Tiêm chủng")
                    .type(MedicalService.ServiceType.VACCINATION)
                    .description("Tiêm phòng các loại vaccine")
                    .price(new BigDecimal("100000"))
                    .durationMinutes(15)
                    .facility(facility)
                    .isActive(true)
                    .build();
            
            serviceRepository.save(generalCheckup);
            serviceRepository.save(cardiology);
            serviceRepository.save(bloodTest);
            serviceRepository.save(xray);
            serviceRepository.save(vaccination);
            
            log.info("Medical services initialized successfully");
        }
    }
    
    private void initDoctorSchedules() {
        if (scheduleRepository.count() == 0) {
            log.info("Initializing doctor schedules...");
            
            Doctor doctor = doctorRepository.findAll().stream().findFirst().orElse(null);
            
            if (doctor != null) {
                // Monday to Friday: 8:00 - 12:00 and 14:00 - 17:00
                for (DayOfWeek day : new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, 
                        DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY}) {
                    
                    DoctorSchedule morning = DoctorSchedule.builder()
                            .doctor(doctor)
                            .dayOfWeek(day)
                            .startTime(LocalTime.of(8, 0))
                            .endTime(LocalTime.of(12, 0))
                            .slotDurationMinutes(30)
                            .isActive(true)
                            .build();
                    
                    DoctorSchedule afternoon = DoctorSchedule.builder()
                            .doctor(doctor)
                            .dayOfWeek(day)
                            .startTime(LocalTime.of(14, 0))
                            .endTime(LocalTime.of(17, 0))
                            .slotDurationMinutes(30)
                            .isActive(true)
                            .build();
                    
                    scheduleRepository.save(morning);
                    scheduleRepository.save(afternoon);
                }
                
                // Saturday: 8:00 - 12:00
                DoctorSchedule saturday = DoctorSchedule.builder()
                        .doctor(doctor)
                        .dayOfWeek(DayOfWeek.SATURDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(12, 0))
                        .slotDurationMinutes(30)
                        .isActive(true)
                        .build();
                scheduleRepository.save(saturday);
                
                log.info("Doctor schedules initialized successfully");
            }
        }
    }
}

