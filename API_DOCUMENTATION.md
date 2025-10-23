# Health Services Booking - API Documentation

## 📋 Mục lục
- [Tổng quan](#tổng-quan)
- [Setup & Installation](#setup--installation)
- [Authentication API](#authentication-api)
- [Sử dụng](#sử-dụng)
- [Security](#security)

---

## 🎯 Tổng quan

Health Services Booking API là REST API cho hệ thống đặt lịch khám bệnh, được xây dựng với:
- **Spring Boot 3.5.6**
- **Java 17**
- **JWT Authentication**
- **MySQL Database**
- **Spring Security**
- **Email Verification**
- **Refresh Token**

---

## 🚀 Setup & Installation

### 1. Yêu cầu
- Java 17+
- MySQL 8.0+
- Maven 3.6+
- SMTP Server (Gmail recommended)

### 2. Cấu hình Database

Tạo database MySQL:
```sql
CREATE DATABASE healthservicesbooking;
```

### 3. Cấu hình Environment Variables

Tạo file `.env` từ `.env.example`:

```bash
# Spring Profile
SPRING_PROFILES_ACTIVE=dev

# Database
DB_URL=jdbc:mysql://localhost:3306/healthservicesbooking
DB_USERNAME=root
DB_PASSWORD=your_password

# JWT (Generate secure key với: openssl rand -base64 64)
JWT_SECRET=your_super_secret_key_at_least_256_bits_long
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Email (Gmail)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
MAIL_FROM=noreply@healthservices.com
```

### 4. Chạy ứng dụng

```bash
mvn clean install
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: `http://localhost:8080`

---

## 🔐 Authentication API

Base URL: `/api/auth`

### 1. Đăng ký tài khoản

**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "fullname": "John Doe",
  "mobile": "0123456789",
  "gender": 1,
  "address": "123 Street, City",
  "birthday": "1990-01-01"
}
```

**Response:** `201 CREATED`
```json
{
  "message": "User registered successfully. Please check your email for verification code.",
  "user": {
    "id": "uuid-here",
    "username": "john_doe",
    "email": "john@example.com",
    "fullname": "John Doe",
    "mobile": "0123456789",
    "gender": 1,
    "address": "123 Street, City",
    "birthday": "1990-01-01",
    "age": 34,
    "isActive": true,
    "badPoint": 0,
    "roles": ["ROLE_PATIENT"]
  }
}
```

**Notes:**
- Email verification code sẽ được gửi đến email đã đăng ký
- Code có hiệu lực trong 5 phút
- Gender: 0 = Nữ, 1 = Nam, 2 = Khác

---

### 2. Xác thực Email

**Endpoint:** `POST /api/auth/verify-email`

**Request Body:**
```json
{
  "email": "john@example.com",
  "code": "123456"
}
```

**Response:** `200 OK`
```json
{
  "message": "Email verified successfully. Your account is now active."
}
```

---

### 3. Gửi lại mã xác thực

**Endpoint:** `POST /api/auth/resend-code`

**Request Body:**
```json
{
  "email": "john@example.com"
}
```

**Response:** `200 OK`
```json
{
  "message": "Verification code has been resent to your email."
}
```

---

### 4. Đăng nhập

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:** `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "tokenType": "Bearer",
  "user": {
    "id": "uuid-here",
    "username": "john_doe",
    "email": "john@example.com",
    "fullname": "John Doe",
    "roles": ["ROLE_PATIENT"]
  }
}
```

**Lưu ý:**
- Access Token hết hạn sau 24 giờ
- Refresh Token hết hạn sau 7 ngày
- Lưu cả 2 tokens để sử dụng

---

### 5. Làm mới Access Token

**Endpoint:** `POST /api/auth/refresh-token`

**Request Body:**
```json
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response:** `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "tokenType": "Bearer",
  "user": { ... }
}
```

---

### 6. Đăng xuất

**Endpoint:** `POST /api/auth/logout`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "message": "Logged out successfully"
}
```

**Lưu ý:**
- Refresh token sẽ bị xóa khỏi database
- Access token sẽ vẫn valid cho đến khi hết hạn

---

### 7. Quên mật khẩu

**Endpoint:** `POST /api/auth/forgot-password`

**Request Body:**
```json
{
  "email": "john@example.com"
}
```

**Response:** `200 OK`
```json
{
  "message": "Password reset code has been sent to your email."
}
```

**Lưu ý:**
- Reset code có hiệu lực 5 phút
- Check email để lấy code

---

### 8. Reset mật khẩu

**Endpoint:** `POST /api/auth/reset-password`

**Request Body:**
```json
{
  "email": "john@example.com",
  "code": "123456",
  "newPassword": "newpassword123"
}
```

**Response:** `200 OK`
```json
{
  "message": "Password has been reset successfully. Please login with your new password."
}
```

**Lưu ý:**
- Tất cả refresh tokens sẽ bị xóa
- Phải đăng nhập lại với mật khẩu mới

---

## 📝 Sử dụng API với Authorization

Đối với các endpoint yêu cầu authentication, thêm header:

```
Authorization: Bearer {accessToken}
```

**Ví dụ với cURL:**
```bash
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

**Ví dụ với JavaScript:**
```javascript
fetch('http://localhost:8080/api/user/profile', {
  headers: {
    'Authorization': `Bearer ${accessToken}`,
    'Content-Type': 'application/json'
  }
})
```

---

## 🔒 Security Features

### 1. JWT Authentication
- Access Token: 24 giờ
- Refresh Token: 7 ngày
- Thuật toán: HS512

### 2. Email Verification
- Mã 6 chữ số
- Hết hạn sau 5 phút
- Tự động gửi email

### 3. Password Reset
- Mã 6 chữ số
- Hết hạn sau 5 phút
- Force logout sau reset

### 4. Account Security
- Password encoding: BCrypt
- Bad point system (auto lock after 3 violations)
- Active/Inactive status check

### 5. Role-Based Access Control
- ROLE_PATIENT: Người dùng thường
- ROLE_DOCTOR: Bác sĩ
- ROLE_ADMIN: Quản trị viên

---

## ⚠️ Error Responses

### 400 Bad Request
```json
{
  "status": 400,
  "message": "Username already exists!",
  "timestamp": "2024-01-01T10:00:00"
}
```

### 401 Unauthorized
```json
{
  "status": 401,
  "message": "Invalid username or password",
  "timestamp": "2024-01-01T10:00:00"
}
```

### 403 Forbidden
```json
{
  "status": 403,
  "message": "Account has been disabled. Please contact administrator.",
  "timestamp": "2024-01-01T10:00:00"
}
```

### 404 Not Found
```json
{
  "status": 404,
  "message": "User not found",
  "timestamp": "2024-01-01T10:00:00"
}
```

### Validation Error
```json
{
  "username": "Username must be between 3 and 50 characters",
  "email": "Email should be valid",
  "password": "Password must be at least 6 characters"
}
```

---

## 📧 Email Configuration

### Gmail Setup

1. Bật 2-Factor Authentication cho Gmail
2. Tạo App Password:
   - Vào Google Account → Security
   - Chọn "App passwords"
   - Tạo password mới cho "Mail"
3. Sử dụng App Password trong `.env`:
   ```
   MAIL_USERNAME=your-email@gmail.com
   MAIL_PASSWORD=your-16-digit-app-password
   ```

---

## 🧪 Testing

### Test Endpoint
```bash
GET /api/auth/test
```

**Response:**
```json
{
  "message": "Auth API is working!",
  "timestamp": "2024-01-01T10:00:00"
}
```

---

## 📌 Notes

1. **Environment Variables**: Không commit file `.env` vào Git
2. **JWT Secret**: Phải ít nhất 256 bits (32 characters)
3. **Database**: Tự động tạo bảng với `ddl-auto=update`
4. **Default Roles**: Tự động tạo khi application start
5. **Email**: Gửi async, không block request

---

## 🆘 Troubleshooting

### Lỗi Email không gửi được
- Check SMTP credentials
- Check firewall/antivirus
- Đảm bảo "Less secure app access" hoặc "App Password" đã được bật

### Lỗi Database connection
- Check MySQL đang chạy
- Check credentials trong `.env`
- Check database đã được tạo

### Lỗi JWT Token invalid
- Check token chưa expired
- Check JWT_SECRET giống nhau giữa các lần restart
- Check token format: "Bearer {token}"

---

## 📞 Contact

Nếu có vấn đề, liên hệ: support@healthservices.com

