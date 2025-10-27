# Test API - Health Services Booking

## ✅ Application đã chạy thành công!

Server đang chạy tại: `http://localhost:8080`

---

## 🧪 Test APIs với cURL

### 1. Đăng nhập với tài khoản Patient

```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"patient@healthservices.com\",\"password\":\"patient123\"}"
```

**Response mẫu:**
```json
{
  "success": true,
  "message": "Đăng nhập thành công!",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "uuid-string",
    "tokenType": "Bearer",
    "user": {
      "id": 3,
      "email": "patient@healthservices.com",
      "fullName": "Trần Thị B",
      "role": "ROLE_PATIENT"
    }
  }
}
```

**Lưu lại accessToken để dùng cho các request tiếp theo!**

---

### 2. Xem danh sách cơ sở y tế

```bash
curl -X GET http://localhost:8080/api/admin/facilities ^
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

### 3. Xem danh sách dịch vụ y tế

```bash
curl -X GET http://localhost:8080/api/admin/services ^
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

### 4. Xem danh sách bác sĩ

```bash
curl -X GET http://localhost:8080/api/admin/doctors ^
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

### 5. Đặt lịch hẹn (Patient)

```bash
curl -X POST http://localhost:8080/api/patient/appointments ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" ^
  -d "{\"doctorId\":1,\"serviceId\":2,\"facilityId\":1,\"appointmentDate\":\"2025-12-25T10:00:00\",\"reason\":\"Khám tim mạch định kỳ\",\"notes\":\"Có tiền sử bệnh tim\"}"
```

---

### 6. Xem lịch hẹn của tôi (Patient)

```bash
curl -X GET http://localhost:8080/api/patient/appointments ^
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

### 7. Đăng nhập với tài khoản Doctor

```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"doctor@healthservices.com\",\"password\":\"doctor123\"}"
```

---

### 8. Xem lịch hẹn (Doctor)

```bash
curl -X GET http://localhost:8080/api/doctor/appointments ^
  -H "Authorization: Bearer DOCTOR_ACCESS_TOKEN"
```

---

### 9. Cập nhật trạng thái lịch hẹn (Doctor)

```bash
curl -X PATCH http://localhost:8080/api/doctor/appointments/1/status ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer DOCTOR_ACCESS_TOKEN" ^
  -d "{\"status\":\"CONFIRMED\",\"notes\":\"Đã xác nhận lịch hẹn\"}"
```

---

### 10. Tạo hồ sơ khám bệnh (Doctor)

```bash
curl -X POST http://localhost:8080/api/doctor/medical-records ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer DOCTOR_ACCESS_TOKEN" ^
  -d "{\"appointmentId\":1,\"diagnosis\":\"Cao huyết áp độ 1\",\"symptoms\":\"Đau đầu, chóng mặt\",\"treatment\":\"Thuốc hạ huyết áp\",\"prescription\":\"Amlodipine 5mg\",\"followUpInstructions\":\"Tái khám sau 2 tuần\"}"
```

---

## 📱 Test với Postman

### Bước 1: Import Collection

1. Mở Postman
2. Click "Import"
3. Paste URL này hoặc tạo requests mới:

### Bước 2: Tạo Environment Variables

```
BASE_URL = http://localhost:8080
ACCESS_TOKEN = (lấy từ login response)
```

### Bước 3: Test Flow

1. **Login** → Lấy accessToken
2. **Get Facilities** → Xem danh sách cơ sở
3. **Get Services** → Xem danh sách dịch vụ
4. **Get Doctors** → Xem danh sách bác sĩ
5. **Create Appointment** → Đặt lịch hẹn
6. **Get My Appointments** → Xem lịch hẹn

---

## 🔐 Tài khoản test

### Admin
- **Email**: admin@healthservices.com
- **Password**: admin123
- **Quyền**: Quản lý toàn bộ hệ thống

### Doctor
- **Email**: doctor@healthservices.com
- **Password**: doctor123
- **Chuyên khoa**: Tim mạch

### Patient
- **Email**: patient@healthservices.com
- **Password**: patient123

---

## 📊 Dữ liệu mẫu đã tạo sẵn

### Cơ sở y tế (3):
1. Bệnh viện Đa khoa Trung ương
2. Phòng khám Đa khoa Sài Gòn
3. Trung tâm Y tế Quận 1

### Dịch vụ y tế (5):
1. Khám tổng quát - 200,000 VNĐ
2. Khám tim mạch - 300,000 VNĐ
3. Xét nghiệm máu - 150,000 VNĐ
4. Chụp X-quang - 250,000 VNĐ
5. Tiêm chủng - 100,000 VNĐ

### Bác sĩ (1):
- Dr. Nguyễn Văn A - Chuyên khoa Tim mạch
- Lịch làm việc: T2-T6 (8h-12h, 14h-17h), T7 (8h-12h)

---

## 🐛 Kiểm tra logs

Nếu có lỗi, kiểm tra console logs của application:

```bash
# Xem logs
tail -f logs/spring.log

# Hoặc xem trong terminal đang chạy application
```

---

## ✅ Checklist Test

- [ ] Login với cả 3 roles (Admin, Doctor, Patient)
- [ ] Xem danh sách facilities
- [ ] Xem danh sách services
- [ ] Xem danh sách doctors
- [ ] Đặt lịch hẹn (Patient)
- [ ] Xem lịch hẹn của mình
- [ ] Xác nhận lịch hẹn (Doctor)
- [ ] Tạo hồ sơ khám bệnh (Doctor)
- [ ] Xem hồ sơ khám bệnh (Patient)
- [ ] Hủy lịch hẹn (Patient)

---

## 📞 Troubleshooting

### Lỗi: "401 Unauthorized"
✅ Kiểm tra token đã được thêm vào header `Authorization: Bearer <token>`
✅ Token có thể đã hết hạn (24h), cần login lại

### Lỗi: "403 Forbidden"
✅ Kiểm tra role có quyền truy cập endpoint này không
✅ Patient không thể truy cập `/api/doctor/**`
✅ Doctor không thể truy cập `/api/patient/**`

### Lỗi: "404 Not Found"
✅ Kiểm tra URL có đúng không
✅ Kiểm tra ID có tồn tại không (doctor, service, facility)

---

**Happy Testing! 🚀**

