# 🚀 Swagger UI - Quick Start

## ✅ Swagger đã hoạt động!

### 🌐 Truy cập ngay:

```
http://localhost:8080/swagger-ui.html
```

---

## 🎯 Test API trong 3 bước đơn giản:

### **Bước 1: Đăng nhập lấy Token** 🔐

1. Tìm section **"Authentication"** 
2. Click **POST /api/auth/login**
3. Click **"Try it out"**
4. Nhập:
   ```json
   {
     "email": "patient@healthservices.com",
     "password": "patient123"
   }
   ```
5. Click **"Execute"**
6. **Copy accessToken** từ response

### **Bước 2: Authorize** 🔓

1. Click nút **"Authorize"** (góc trên bên phải)
2. Nhập vào ô:
   ```
   Bearer <paste-token-của-bạn-vào-đây>
   ```
   Ví dụ: `Bearer eyJhbGciOiJIUzUxMiJ9...`
3. Click **"Authorize"**
4. Click **"Close"**

### **Bước 3: Test APIs** ✨

Giờ bạn có thể test bất kỳ API nào:
- Click vào API muốn test
- Click **"Try it out"**
- Điền thông tin (nếu cần)
- Click **"Execute"**
- Xem kết quả!

---

## 👥 Tài khoản test:

| Role | Email | Password |
|------|-------|----------|
| **Patient** | patient@healthservices.com | patient123 |
| **Doctor** | doctor@healthservices.com | doctor123 |
| **Admin** | admin@healthservices.com | admin123 |

---

## 💡 Tips:

### ✅ Thử các role khác nhau:
- Đăng nhập với Patient → Test APIs bệnh nhân
- Đăng nhập với Doctor → Test APIs bác sĩ
- Đăng nhập với Admin → Test APIs quản trị

### ✅ APIs không cần đăng nhập:
- Tất cả APIs trong **"Authentication"** section
- Đăng ký, Đăng nhập, Quên mật khẩu, etc.

### ✅ APIs cần đăng nhập:
- **Patient** APIs: Đặt lịch, Xem lịch hẹn, Xem hồ sơ
- **Doctor** APIs: Xem lịch hẹn, Tạo hồ sơ khám bệnh
- **Admin** APIs: Quản lý users, facilities, services

---

## 🎨 Giao diện Swagger UI:

Bạn sẽ thấy:
- ✅ **4 nhóm APIs**: Authentication, Patient, Doctor, Admin
- ✅ **Mô tả chi tiết** cho mỗi endpoint
- ✅ **Request body examples** 
- ✅ **Response examples**
- ✅ **Schemas** - Cấu trúc dữ liệu đầy đủ

---

## 🔥 Ví dụ nhanh:

### Đặt lịch hẹn (Patient):

1. Login với Patient account → Copy token
2. Click **"Authorize"** → Paste token
3. Mở **POST /api/patient/appointments**
4. Click **"Try it out"**
5. Nhập:
```json
{
  "doctorId": 1,
  "serviceId": 2,
  "facilityId": 1,
  "appointmentDate": "2025-12-25T10:00:00",
  "reason": "Khám tim mạch",
  "notes": "Có tiền sử bệnh tim"
}
```
6. Click **"Execute"** → Xem kết quả!

---

## 📱 Alternative URLs:

Nếu URL trên không hoạt động, thử:
- `http://localhost:8080/swagger-ui/index.html`
- `http://localhost:8080/v3/api-docs` (JSON format)

---

## 🐛 Nếu gặp lỗi 401:

✅ Đảm bảo đã click **"Authorize"** và nhập token đúng format:
   ```
   Bearer <your-token>
   ```

✅ Token hết hạn sau 24h, cần login lại để lấy token mới

---

## 🎊 Happy Testing!

Bây giờ bạn có thể test tất cả APIs một cách dễ dàng và trực quan! 🚀

**Không cần Postman hay cURL nữa!** ✨

