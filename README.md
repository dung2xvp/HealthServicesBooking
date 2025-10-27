# Health Services Booking API

Hệ thống đặt lịch khám bệnh trực tuyến được xây dựng với Spring Boot.

## 🚀 Tính năng chính

### 👥 Quản lý người dùng
- ✅ Đăng ký tài khoản (Bệnh nhân & Bác sĩ)
- ✅ Đăng nhập với JWT authentication
- ✅ Xác thực email với mã OTP
- ✅ Quên mật khẩu & Khôi phục mật khẩu
- ✅ Refresh token

### 🏥 Quản lý cơ sở y tế
- ✅ CRUD cơ sở y tế (Bệnh viện, Phòng khám, Trung tâm y tế)
- ✅ Quản lý thông tin liên hệ, địa chỉ, giờ mở cửa

### 💊 Quản lý dịch vụ y tế
- ✅ CRUD dịch vụ y tế
- ✅ Các loại dịch vụ: Khám tổng quát, Chuyên khoa, Xét nghiệm, Chẩn đoán hình ảnh, Tiêm chủng, v.v.
- ✅ Quản lý giá cả và thời gian khám

### 👨‍⚕️ Quản lý bác sĩ
- ✅ Hồ sơ bác sĩ với chuyên khoa, bằng cấp, kinh nghiệm
- ✅ Lịch làm việc của bác sĩ
- ✅ Phí tư vấn
- ✅ Trạng thái hoạt động

### 📅 Đặt lịch hẹn
- ✅ Bệnh nhân đặt lịch hẹn với bác sĩ
- ✅ Kiểm tra xung đột thời gian
- ✅ Trạng thái lịch hẹn: Chờ xác nhận, Đã xác nhận, Đang khám, Hoàn thành, Đã hủy
- ✅ Xác nhận lịch hẹn qua email
- ✅ Hủy lịch hẹn

### 💰 Thanh toán
- ✅ Tạo thông tin thanh toán tự động khi đặt lịch
- ✅ Các phương thức thanh toán: Tiền mặt, Thẻ, Chuyển khoản, VNPay, MoMo
- ✅ Trạng thái thanh toán

### 📋 Hồ sơ khám bệnh
- ✅ Bác sĩ tạo và cập nhật hồ sơ khám bệnh
- ✅ Chẩn đoán, triệu chứng, điều trị, đơn thuốc
- ✅ Kết quả xét nghiệm
- ✅ Hướng dẫn tái khám
- ✅ Lịch sử khám bệnh của bệnh nhân

### 📧 Thông báo Email
- ✅ Email xác thực tài khoản
- ✅ Email khôi phục mật khẩu
- ✅ Email xác nhận lịch hẹn

## 🏗️ Công nghệ sử dụng

- **Backend Framework**: Spring Boot 3.5.6
- **Database**: MySQL
- **Security**: Spring Security + JWT
- **Email**: Spring Mail (SMTP)
- **ORM**: Spring Data JPA / Hibernate
- **Validation**: Jakarta Validation
- **Build Tool**: Maven
- **Java Version**: 17

## 📦 Cấu trúc dự án

```
src/main/java/com/example/HealthServicesBooking/
├── config/              # Cấu hình (Security, Web, DataInitializer)
├── constant/            # Hằng số (Messages, Roles, App)
├── controller/          # REST Controllers
│   ├── AuthController.java
│   ├── AdminController.java
│   ├── DoctorController.java
│   └── PatientController.java
├── dto/                 # Data Transfer Objects
│   ├── request/
│   └── response/
├── entity/              # JPA Entities
│   ├── base/
│   ├── User.java
│   ├── Role.java
│   ├── HealthFacility.java
│   ├── MedicalService.java
│   ├── Doctor.java
│   ├── Patient.java
│   ├── Appointment.java
│   ├── Payment.java
│   ├── MedicalRecord.java
│   └── RefreshToken.java
├── exception/           # Exception handling
├── repository/          # JPA Repositories
├── security/            # Security & JWT
└── service/             # Business logic
```

## 🔧 Cài đặt và Chạy dự án

### Yêu cầu hệ thống
- Java 17 hoặc cao hơn
- MySQL 8.0 hoặc cao hơn
- Maven 3.6 hoặc cao hơn

### 1. Clone repository

```bash
git clone <repository-url>
cd HealthServicesBooking
```

### 2. Cấu hình database

Tạo database MySQL:

```sql
CREATE DATABASE healthservicesbooking;
```

Cập nhật thông tin database trong `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/healthservicesbooking
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Cấu hình email (Optional)

Cập nhật cấu hình email trong `application.properties`:

```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**Lưu ý**: Để gửi email qua Gmail, bạn cần:
- Bật "2-Step Verification" trong tài khoản Google
- Tạo "App Password" và sử dụng password đó

### 4. Build và chạy

```bash
# Build project
mvn clean install

# Chạy application
mvn spring-boot:run
```

Application sẽ chạy tại: `http://localhost:8080`

## 👤 Tài khoản mặc định

Sau khi chạy lần đầu, hệ thống sẽ tự động tạo các tài khoản mặc định:

### Admin
- **Email**: admin@healthservices.com
- **Password**: admin123

### Bác sĩ
- **Email**: doctor@healthservices.com
- **Password**: doctor123

### Bệnh nhân
- **Email**: patient@healthservices.com
- **Password**: patient123

## 📚 API Endpoints

### Authentication (`/api/auth`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/register` | Đăng ký tài khoản |
| POST | `/login` | Đăng nhập |
| POST | `/verify-email` | Xác thực email |
| POST | `/resend-verification-code` | Gửi lại mã xác thực |
| POST | `/forgot-password` | Quên mật khẩu |
| POST | `/reset-password` | Khôi phục mật khẩu |
| POST | `/refresh-token` | Làm mới token |

### Patient Endpoints (`/api/patient`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/appointments` | Đặt lịch hẹn |
| GET | `/appointments` | Xem danh sách lịch hẹn |
| GET | `/appointments/{id}` | Xem chi tiết lịch hẹn |
| DELETE | `/appointments/{id}` | Hủy lịch hẹn |
| GET | `/medical-records` | Xem lịch sử khám bệnh |
| GET | `/medical-records/{id}` | Xem chi tiết hồ sơ khám bệnh |

### Doctor Endpoints (`/api/doctor`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/appointments` | Xem danh sách lịch hẹn |
| GET | `/appointments/{id}` | Xem chi tiết lịch hẹn |
| PATCH | `/appointments/{id}/status` | Cập nhật trạng thái lịch hẹn |
| POST | `/medical-records` | Tạo hồ sơ khám bệnh |
| PUT | `/medical-records/{id}` | Cập nhật hồ sơ khám bệnh |
| GET | `/medical-records/{id}` | Xem chi tiết hồ sơ khám bệnh |

### Admin Endpoints (`/api/admin`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/users` | Xem danh sách người dùng |
| GET | `/users/{id}` | Xem chi tiết người dùng |
| PATCH | `/users/{id}/activate` | Kích hoạt/Vô hiệu hóa tài khoản |
| GET | `/facilities` | Xem danh sách cơ sở y tế |
| POST | `/facilities` | Tạo cơ sở y tế |
| PUT | `/facilities/{id}` | Cập nhật cơ sở y tế |
| DELETE | `/facilities/{id}` | Xóa cơ sở y tế |
| GET | `/services` | Xem danh sách dịch vụ |
| POST | `/services` | Tạo dịch vụ |
| PUT | `/services/{id}` | Cập nhật dịch vụ |
| DELETE | `/services/{id}` | Xóa dịch vụ |
| GET | `/doctors` | Xem danh sách bác sĩ |
| PATCH | `/doctors/{id}/availability` | Cập nhật trạng thái bác sĩ |
| GET | `/appointments` | Xem tất cả lịch hẹn |
| GET | `/appointments/status/{status}` | Lọc lịch hẹn theo trạng thái |

## 🔒 Authentication

API sử dụng JWT (JSON Web Token) cho authentication.

### Cách sử dụng:

1. Đăng nhập để nhận access token:
```json
POST /api/auth/login
{
  "email": "patient@healthservices.com",
  "password": "patient123"
}
```

2. Thêm token vào header của các request tiếp theo:
```
Authorization: Bearer <your-access-token>
```

3. Khi access token hết hạn, sử dụng refresh token để lấy token mới:
```json
POST /api/auth/refresh-token
{
  "refreshToken": "<your-refresh-token>"
}
```

## 📝 Request/Response Examples

### 1. Đăng ký tài khoản

**Request:**
```json
POST /api/auth/register
{
  "email": "newpatient@example.com",
  "password": "password123",
  "fullName": "Nguyễn Văn A",
  "phoneNumber": "0901234567",
  "role": "PATIENT"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.",
  "data": {
    "id": 1,
    "email": "newpatient@example.com",
    "fullName": "Nguyễn Văn A",
    "phoneNumber": "0901234567",
    "role": "ROLE_PATIENT",
    "isEmailVerified": false,
    "isActive": true
  }
}
```

### 2. Đặt lịch hẹn

**Request:**
```json
POST /api/patient/appointments
Authorization: Bearer <token>

{
  "doctorId": 1,
  "serviceId": 2,
  "facilityId": 1,
  "appointmentDate": "2024-12-25T10:00:00",
  "reason": "Khám tim mạch định kỳ",
  "notes": "Có tiền sử bệnh tim"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Đặt lịch hẹn thành công.",
  "data": {
    "id": 1,
    "appointmentDate": "2024-12-25T10:00:00",
    "status": "PENDING",
    "reason": "Khám tim mạch định kỳ",
    "notes": "Có tiền sử bệnh tim"
  }
}
```

## 🐛 Error Handling

API trả về error responses theo format:

```json
{
  "timestamp": "2024-10-25T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email đã tồn tại trong hệ thống",
  "path": "/api/auth/register"
}
```

### HTTP Status Codes

- `200 OK` - Request thành công
- `201 Created` - Tạo resource thành công
- `400 Bad Request` - Request không hợp lệ
- `401 Unauthorized` - Chưa xác thực
- `403 Forbidden` - Không có quyền truy cập
- `404 Not Found` - Không tìm thấy resource
- `500 Internal Server Error` - Lỗi server

## 🧪 Testing

Bạn có thể test API bằng:
- **Postman**: Import các endpoints và test
- **curl**: Command line testing
- **Swagger UI** (nếu được cấu hình): `http://localhost:8080/swagger-ui.html`

## 📄 License

MIT License

## 👨‍💻 Developer

Phát triển bởi [Your Name]

## 📞 Liên hệ

- Email: support@healthservices.com
- GitHub: [Your GitHub]

---

**Note**: Đây là dự án demo/học tập. Không nên sử dụng trực tiếp cho production mà không có các biện pháp bảo mật bổ sung.

