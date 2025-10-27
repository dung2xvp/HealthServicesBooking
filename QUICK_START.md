# Quick Start Guide

## 🎉 Dự án đã hoàn thành!

Dự án **Health Services Booking API** đã được xây dựng hoàn chỉnh với đầy đủ chức năng:

### ✅ Đã hoàn thành:

1. **Entities & Database** (11 entities)
   - User, Role, HealthFacility, MedicalService
   - Doctor, Patient, DoctorSchedule
   - Appointment, Payment, MedicalRecord, RefreshToken

2. **Repositories** (11 repositories với các query methods)

3. **Security & Authentication**
   - Spring Security với JWT
   - Custom UserDetails & UserDetailsService
   - JWT Token Provider & Authentication Filter
   - Role-based access control

4. **DTOs** (Request/Response objects)
   - Authentication DTOs
   - Appointment DTOs
   - Medical Record DTOs
   - API Response wrapper

5. **Services** (Business logic)
   - AuthService (đăng ký, đăng nhập, xác thực email, quên mật khẩu)
   - EmailService (gửi email async)
   - AppointmentService (đặt lịch, quản lý lịch hẹn)
   - MedicalRecordService (hồ sơ khám bệnh)
   - RefreshTokenService (quản lý refresh tokens)

6. **Controllers** (REST APIs)
   - AuthController (authentication endpoints)
   - PatientController (patient endpoints)
   - DoctorController (doctor endpoints)
   - AdminController (admin management)

7. **Exception Handling**
   - Global exception handler
   - Custom exceptions (BadRequestException, ResourceNotFoundException, UnauthorizedException)
   - Error response format

8. **Data Initializer**
   - Tự động tạo roles mặc định
   - Tài khoản test (admin, doctor, patient)
   - Dữ liệu mẫu (facilities, services, schedules)

9. **Documentation**
   - README.md (hướng dẫn chi tiết)
   - API_DOCUMENTATION.md (tài liệu API đầy đủ)

---

## 🔧 Fix Lombok Compile Issues

Lỗi compile hiện tại là do Lombok annotation processing. Để fix:

### Option 1: Rebuild trong IDE

1. **IntelliJ IDEA:**
   ```
   File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors
   ✅ Enable annotation processing
   ```
   Sau đó: `Build > Rebuild Project`

2. **Eclipse:**
   ```
   Cài đặt Lombok:
   - Download lombok.jar
   - Chạy: java -jar lombok.jar
   - Select Eclipse installation
   ```

### Option 2: Clean và Rebuild

```bash
# Windows
.\mvnw.cmd clean install -DskipTests

# Hoặc với Maven đã cài
mvn clean install -DskipTests
```

### Option 3: Compile với explicit annotation processing

```bash
.\mvnw.cmd clean compile -Dmaven.compiler.proc=full
```

---

## 🚀 Chạy ứng dụng

### 1. Tạo Database

```sql
CREATE DATABASE healthservicesbooking 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

### 2. Cấu hình Database

Cập nhật `application.properties` nếu cần:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/healthservicesbooking
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Chạy Application

```bash
# Với Maven Wrapper
.\mvnw.cmd spring-boot:run

# Hoặc với Maven đã cài
mvn spring-boot:run
```

Application sẽ chạy tại: `http://localhost:8080`

---

## 📝 Test API

### 1. Đăng nhập với tài khoản test

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "patient@healthservices.com",
    "password": "patient123"
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Đăng nhập thành công!",
  "data": {
    "accessToken": "eyJhbGc...",
    "refreshToken": "uuid...",
    "tokenType": "Bearer",
    "user": { ... }
  }
}
```

### 2. Sử dụng token

```bash
curl -X GET http://localhost:8080/api/patient/appointments \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## 👤 Tài khoản mặc định

Sau khi chạy lần đầu, hệ thống sẽ tự động tạo:

### Admin
- **Email**: `admin@healthservices.com`
- **Password**: `admin123`
- **Quyền**: Quản lý toàn bộ hệ thống

### Doctor (Bác sĩ)
- **Email**: `doctor@healthservices.com`
- **Password**: `doctor123`
- **Chuyên khoa**: Tim mạch
- **Lịch làm việc**: T2-T6 (8h-12h, 14h-17h), T7 (8h-12h)

### Patient (Bệnh nhân)
- **Email**: `patient@healthservices.com`
- **Password**: `patient123`

---

## 📚 Cấu trúc dự án

```
src/main/java/com/example/HealthServicesBooking/
├── config/                      # Cấu hình
│   ├── DataInitializer.java    # Khởi tạo dữ liệu
│   ├── SecurityConfig.java     # Cấu hình security
│   └── WebConfig.java          # Cấu hình CORS
├── constant/                    # Hằng số
│   ├── AppConstant.java
│   ├── MessageConstant.java
│   └── RoleConstant.java
├── controller/                  # REST Controllers
│   ├── AuthController.java
│   ├── PatientController.java
│   ├── DoctorController.java
│   └── AdminController.java
├── dto/                        # Data Transfer Objects
│   ├── request/
│   └── response/
├── entity/                     # JPA Entities
│   ├── base/BaseEntity.java
│   ├── User.java
│   ├── Role.java
│   ├── HealthFacility.java
│   ├── MedicalService.java
│   ├── Doctor.java
│   ├── Patient.java
│   ├── DoctorSchedule.java
│   ├── Appointment.java
│   ├── Payment.java
│   ├── MedicalRecord.java
│   └── RefreshToken.java
├── exception/                  # Exception handling
│   ├── GlobalExceptionHandler.java
│   ├── BadRequestException.java
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   └── ErrorResponse.java
├── repository/                 # JPA Repositories
│   └── ... (11 repositories)
├── security/                   # Security components
│   ├── CustomUserDetails.java
│   ├── CustomUserDetailsService.java
│   └── jwt/
│       ├── JwtTokenProvider.java
│       └── JwtAuthenticationFilter.java
└── service/                    # Business logic
    ├── AuthService.java
    ├── EmailService.java
    ├── RefreshTokenService.java
    ├── AppointmentService.java
    └── MedicalRecordService.java
```

---

## 🌐 API Endpoints Summary

### Authentication (`/api/auth`)
- POST `/register` - Đăng ký
- POST `/login` - Đăng nhập
- POST `/verify-email` - Xác thực email
- POST `/forgot-password` - Quên mật khẩu
- POST `/reset-password` - Đặt lại mật khẩu
- POST `/refresh-token` - Làm mới token

### Patient (`/api/patient`) - Yêu cầu role PATIENT
- POST `/appointments` - Đặt lịch hẹn
- GET `/appointments` - Xem lịch hẹn của tôi
- GET `/appointments/{id}` - Chi tiết lịch hẹn
- DELETE `/appointments/{id}` - Hủy lịch hẹn
- GET `/medical-records` - Xem hồ sơ khám bệnh
- GET `/medical-records/{id}` - Chi tiết hồ sơ

### Doctor (`/api/doctor`) - Yêu cầu role DOCTOR
- GET `/appointments` - Xem lịch hẹn
- PATCH `/appointments/{id}/status` - Cập nhật trạng thái
- POST `/medical-records` - Tạo hồ sơ khám bệnh
- PUT `/medical-records/{id}` - Cập nhật hồ sơ

### Admin (`/api/admin`) - Yêu cầu role ADMIN
- GET/POST/PUT/DELETE `/users/**` - Quản lý người dùng
- GET/POST/PUT/DELETE `/facilities/**` - Quản lý cơ sở y tế
- GET/POST/PUT/DELETE `/services/**` - Quản lý dịch vụ
- GET/PATCH `/doctors/**` - Quản lý bác sĩ
- GET `/appointments/**` - Xem tất cả lịch hẹn

---

## 🛠️ Troubleshooting

### Lỗi: "Cannot connect to database"
```
✅ Kiểm tra MySQL đang chạy
✅ Kiểm tra database đã được tạo
✅ Kiểm tra username/password trong application.properties
```

### Lỗi: "Port 8080 already in use"
```
Option 1: Dừng ứng dụng đang chạy trên port 8080
Option 2: Thay đổi port trong application.properties:
server.port=8081
```

### Lỗi: "Email not sending"
```
✅ Cấu hình SMTP trong application.properties
✅ Đối với Gmail, cần tạo App Password
✅ Bật 2-Step Verification trong Google Account
```

---

## 📖 Tài liệu chi tiết

- **README.md** - Hướng dẫn đầy đủ về dự án
- **API_DOCUMENTATION.md** - Tài liệu API chi tiết với examples

---

## 🎯 Next Steps

1. ✅ Fix Lombok compile issues (xem phần trên)
2. ✅ Tạo database và cấu hình connection
3. ✅ Chạy application lần đầu (sẽ tự động tạo tables và data)
4. ✅ Test APIs với Postman hoặc curl
5. ⭐ Customize theo nhu cầu của bạn:
   - Thêm chức năng mới
   - Tích hợp payment gateway (VNPay, MoMo)
   - Thêm real-time notifications
   - Xây dựng frontend

---

**🎉 Chúc bạn thành công với dự án!**

Nếu có vấn đề gì, hãy tham khảo:
- README.md cho thông tin tổng quan
- API_DOCUMENTATION.md cho chi tiết API
- Spring Boot Documentation: https://spring.io/projects/spring-boot

