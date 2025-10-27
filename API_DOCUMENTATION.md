# API Documentation - Health Services Booking

## Base URL
```
http://localhost:8080/api
```

## Authentication

Tất cả các endpoints (trừ `/api/auth/*`) yêu cầu JWT token trong header:

```
Authorization: Bearer <your-jwt-token>
```

---

## 1. Authentication APIs

### 1.1. Register
Đăng ký tài khoản mới.

**Endpoint:** `POST /auth/register`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "Nguyễn Văn A",
  "phoneNumber": "0901234567",
  "role": "PATIENT"
}
```

**Validation:**
- email: Bắt buộc, định dạng email hợp lệ
- password: Bắt buộc, tối thiểu 6 ký tự
- fullName: Bắt buộc
- phoneNumber: Định dạng: `^(\\+84|0)[0-9]{9}$`
- role: Bắt buộc, giá trị: "PATIENT" hoặc "DOCTOR"

**Response:** `201 Created`
```json
{
  "success": true,
  "message": "Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "fullName": "Nguyễn Văn A",
    "phoneNumber": "0901234567",
    "role": "ROLE_PATIENT",
    "isEmailVerified": false,
    "isActive": true,
    "createdAt": "2024-10-25T10:00:00"
  }
}
```

---

### 1.2. Login
Đăng nhập vào hệ thống.

**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Đăng nhập thành công!",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
    "tokenType": "Bearer",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "fullName": "Nguyễn Văn A",
      "role": "ROLE_PATIENT"
    }
  }
}
```

---

### 1.3. Verify Email
Xác thực email bằng mã OTP.

**Endpoint:** `POST /auth/verify-email`

**Request Body:**
```json
{
  "email": "user@example.com",
  "code": "123456"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Email đã được xác thực thành công!"
}
```

---

### 1.4. Resend Verification Code
Gửi lại mã xác thực.

**Endpoint:** `POST /auth/resend-verification-code`

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Mã xác thực đã được gửi đến email của bạn."
}
```

---

### 1.5. Forgot Password
Yêu cầu khôi phục mật khẩu.

**Endpoint:** `POST /auth/forgot-password`

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Email khôi phục mật khẩu đã được gửi."
}
```

---

### 1.6. Reset Password
Đặt lại mật khẩu với token.

**Endpoint:** `POST /auth/reset-password`

**Request Body:**
```json
{
  "token": "reset-token-from-email",
  "newPassword": "newpassword123"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Mật khẩu đã được thay đổi thành công!"
}
```

---

### 1.7. Refresh Token
Làm mới access token.

**Endpoint:** `POST /auth/refresh-token`

**Request Body:**
```json
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Token đã được làm mới",
  "data": {
    "accessToken": "new-access-token",
    "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
    "tokenType": "Bearer"
  }
}
```

---

## 2. Patient APIs

Yêu cầu role: `ROLE_PATIENT`

### 2.1. Create Appointment
Đặt lịch hẹn khám bệnh.

**Endpoint:** `POST /patient/appointments`

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "doctorId": 1,
  "serviceId": 2,
  "facilityId": 1,
  "appointmentDate": "2024-12-25T10:00:00",
  "reason": "Khám tim mạch định kỳ",
  "notes": "Có tiền sử bệnh tim"
}
```

**Response:** `201 Created`
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

---

### 2.2. Get My Appointments
Xem danh sách lịch hẹn của mình.

**Endpoint:** `GET /patient/appointments`

**Headers:**
```
Authorization: Bearer <token>
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Lấy danh sách lịch hẹn thành công",
  "data": [
    {
      "id": 1,
      "appointmentDate": "2024-12-25T10:00:00",
      "status": "CONFIRMED",
      "reason": "Khám tim mạch định kỳ"
    }
  ]
}
```

---

### 2.3. Get Appointment Details
Xem chi tiết lịch hẹn.

**Endpoint:** `GET /patient/appointments/{id}`

**Response:** `200 OK`

---

### 2.4. Cancel Appointment
Hủy lịch hẹn.

**Endpoint:** `DELETE /patient/appointments/{id}?reason=Lý do hủy`

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Hủy lịch hẹn thành công."
}
```

---

### 2.5. Get Medical Records
Xem lịch sử khám bệnh.

**Endpoint:** `GET /patient/medical-records`

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Lấy danh sách hồ sơ khám bệnh thành công",
  "data": [
    {
      "id": 1,
      "diagnosis": "Cao huyết áp",
      "treatment": "Thuốc hạ huyết áp",
      "createdAt": "2024-10-20T15:30:00"
    }
  ]
}
```

---

## 3. Doctor APIs

Yêu cầu role: `ROLE_DOCTOR`

### 3.1. Get Doctor Appointments
Xem danh sách lịch hẹn của bác sĩ.

**Endpoint:** `GET /doctor/appointments`

**Headers:**
```
Authorization: Bearer <token>
```

**Response:** `200 OK`

---

### 3.2. Update Appointment Status
Cập nhật trạng thái lịch hẹn.

**Endpoint:** `PATCH /doctor/appointments/{id}/status`

**Request Body:**
```json
{
  "status": "CONFIRMED",
  "notes": "Đã xác nhận lịch hẹn"
}
```

**Status values:**
- `PENDING` - Chờ xác nhận
- `CONFIRMED` - Đã xác nhận
- `CHECKED_IN` - Đã check-in
- `IN_PROGRESS` - Đang khám
- `COMPLETED` - Hoàn thành
- `CANCELLED` - Đã hủy
- `NO_SHOW` - Không đến

**Response:** `200 OK`

---

### 3.3. Create Medical Record
Tạo hồ sơ khám bệnh.

**Endpoint:** `POST /doctor/medical-records`

**Request Body:**
```json
{
  "appointmentId": 1,
  "diagnosis": "Cao huyết áp độ 1",
  "symptoms": "Đau đầu, chóng mặt",
  "treatment": "Thuốc hạ huyết áp, thay đổi lối sống",
  "prescription": "Amlodipine 5mg, uống 1 viên/ngày",
  "labResults": "Huyết áp: 140/90 mmHg",
  "followUpInstructions": "Tái khám sau 2 tuần",
  "notes": "Bệnh nhân cần theo dõi huyết áp hàng ngày"
}
```

**Response:** `201 Created`

---

### 3.4. Update Medical Record
Cập nhật hồ sơ khám bệnh.

**Endpoint:** `PUT /doctor/medical-records/{id}`

**Request Body:** (Tương tự Create)

**Response:** `200 OK`

---

## 4. Admin APIs

Yêu cầu role: `ROLE_ADMIN`

### 4.1. User Management

#### Get All Users
**Endpoint:** `GET /admin/users`

#### Get User Details
**Endpoint:** `GET /admin/users/{id}`

#### Toggle User Status
**Endpoint:** `PATCH /admin/users/{id}/activate`

---

### 4.2. Facility Management

#### Get All Facilities
**Endpoint:** `GET /admin/facilities`

#### Create Facility
**Endpoint:** `POST /admin/facilities`

**Request Body:**
```json
{
  "name": "Bệnh viện ABC",
  "type": "HOSPITAL",
  "address": "123 Đường ABC, Quận 1, TP.HCM",
  "phoneNumber": "0281234567",
  "email": "contact@abc.com",
  "description": "Bệnh viện đa khoa",
  "openingHours": "24/7",
  "isActive": true
}
```

**Facility Types:**
- `HOSPITAL` - Bệnh viện
- `CLINIC` - Phòng khám
- `MEDICAL_CENTER` - Trung tâm y tế
- `DIAGNOSTIC_CENTER` - Trung tâm chẩn đoán

#### Update Facility
**Endpoint:** `PUT /admin/facilities/{id}`

#### Delete Facility
**Endpoint:** `DELETE /admin/facilities/{id}`

---

### 4.3. Medical Service Management

#### Get All Services
**Endpoint:** `GET /admin/services`

#### Create Service
**Endpoint:** `POST /admin/services`

**Request Body:**
```json
{
  "name": "Khám tim mạch",
  "type": "SPECIALIST_CONSULTATION",
  "description": "Khám và tư vấn về tim mạch",
  "price": 300000,
  "durationMinutes": 45,
  "facilityId": 1,
  "isActive": true
}
```

**Service Types:**
- `GENERAL_CHECKUP` - Khám tổng quát
- `SPECIALIST_CONSULTATION` - Khám chuyên khoa
- `LABORATORY_TEST` - Xét nghiệm
- `DIAGNOSTIC_IMAGING` - Chẩn đoán hình ảnh
- `VACCINATION` - Tiêm chủng
- `MINOR_SURGERY` - Phẫu thuật nhỏ
- `PHYSICAL_THERAPY` - Vật lý trị liệu
- `DENTAL` - Nha khoa
- `OTHER` - Khác

#### Update Service
**Endpoint:** `PUT /admin/services/{id}`

#### Delete Service
**Endpoint:** `DELETE /admin/services/{id}`

---

### 4.4. Doctor Management

#### Get All Doctors
**Endpoint:** `GET /admin/doctors`

#### Toggle Doctor Availability
**Endpoint:** `PATCH /admin/doctors/{id}/availability`

---

### 4.5. Appointment Management

#### Get All Appointments
**Endpoint:** `GET /admin/appointments`

#### Get Appointments by Status
**Endpoint:** `GET /admin/appointments/status/{status}`

---

## Error Responses

Tất cả error responses đều có format:

```json
{
  "timestamp": "2024-10-25T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Mô tả lỗi cụ thể",
  "path": "/api/endpoint"
}
```

### Common Error Codes

- **400 Bad Request**: Dữ liệu request không hợp lệ
- **401 Unauthorized**: Chưa đăng nhập hoặc token không hợp lệ
- **403 Forbidden**: Không có quyền truy cập
- **404 Not Found**: Không tìm thấy resource
- **500 Internal Server Error**: Lỗi server

---

## Rate Limiting

Hiện tại chưa có rate limiting. Trong production, nên implement rate limiting để bảo vệ API.

## Versioning

Hiện tại API đang ở version 1 (implicit). Trong tương lai có thể thêm versioning vào URL: `/api/v1/...`

