# 📘 Hướng dẫn sử dụng Swagger UI

## 🎉 Swagger đã được tích hợp thành công!

Swagger/OpenAPI đã được thêm vào dự án để bạn có thể test API dễ dàng hơn với giao diện web trực quan.

---

## 🌐 Truy cập Swagger UI

Sau khi chạy application, truy cập:

### **Swagger UI:**
```
http://localhost:8080/swagger-ui.html
```
hoặc
```
http://localhost:8080/swagger-ui/index.html
```

### **OpenAPI JSON:**
```
http://localhost:8080/v3/api-docs
```

---

## 🚀 Cách sử dụng Swagger UI

### Bước 1: Mở Swagger UI

1. Đảm bảo application đang chạy
2. Mở trình duyệt và truy cập: `http://localhost:8080/swagger-ui.html`
3. Bạn sẽ thấy giao diện Swagger với tất cả APIs

### Bước 2: Đăng nhập để lấy Token

1. Mở section **"Authentication"**
2. Tìm endpoint **POST /api/auth/login**
3. Click **"Try it out"**
4. Nhập thông tin đăng nhập:

```json
{
  "email": "patient@healthservices.com",
  "password": "patient123"
}
```

5. Click **"Execute"**
6. Copy **accessToken** từ response

### Bước 3: Xác thực với JWT Token

1. Kéo lên đầu trang, tìm nút **"Authorize"** 🔓 (góc trên bên phải)
2. Click vào nút **"Authorize"**
3. Trong popup, nhập token theo format:
   ```
   Bearer eyJhbGciOiJIUzUxMiJ9...
   ```
   (Lưu ý: Có chữ "Bearer " phía trước token)
4. Click **"Authorize"**
5. Click **"Close"**

### Bước 4: Test các APIs

Giờ bạn đã được xác thực và có thể test tất cả APIs:

1. Chọn endpoint muốn test (ví dụ: **GET /api/patient/appointments**)
2. Click **"Try it out"**
3. Nhập parameters nếu cần
4. Click **"Execute"**
5. Xem response ở phía dưới

---

## 📚 Các nhóm API có sẵn

### 🔐 Authentication
- POST `/api/auth/register` - Đăng ký
- POST `/api/auth/login` - Đăng nhập
- POST `/api/auth/verify-email` - Xác thực email
- POST `/api/auth/forgot-password` - Quên mật khẩu
- POST `/api/auth/reset-password` - Đặt lại mật khẩu
- POST `/api/auth/refresh-token` - Làm mới token

### 👤 Patient APIs (Yêu cầu role PATIENT)
- POST `/api/patient/appointments` - Đặt lịch hẹn
- GET `/api/patient/appointments` - Xem lịch hẹn
- GET `/api/patient/appointments/{id}` - Chi tiết lịch hẹn
- DELETE `/api/patient/appointments/{id}` - Hủy lịch hẹn
- GET `/api/patient/medical-records` - Xem hồ sơ khám bệnh
- GET `/api/patient/medical-records/{id}` - Chi tiết hồ sơ

### 👨‍⚕️ Doctor APIs (Yêu cầu role DOCTOR)
- GET `/api/doctor/appointments` - Xem lịch hẹn
- GET `/api/doctor/appointments/{id}` - Chi tiết lịch hẹn
- PATCH `/api/doctor/appointments/{id}/status` - Cập nhật trạng thái
- POST `/api/doctor/medical-records` - Tạo hồ sơ khám bệnh
- PUT `/api/doctor/medical-records/{id}` - Cập nhật hồ sơ
- GET `/api/doctor/medical-records/{id}` - Chi tiết hồ sơ

### 👑 Admin APIs (Yêu cầu role ADMIN)
- **User Management**: GET, POST, PUT, DELETE users
- **Facility Management**: CRUD health facilities
- **Service Management**: CRUD medical services
- **Doctor Management**: GET, PATCH doctors
- **Appointment Management**: GET all appointments

---

## 💡 Tips & Tricks

### 1. Test nhiều roles khác nhau

Đăng nhập với các tài khoản khác nhau để test quyền:

**Patient:**
```json
{"email": "patient@healthservices.com", "password": "patient123"}
```

**Doctor:**
```json
{"email": "doctor@healthservices.com", "password": "doctor123"}
```

**Admin:**
```json
{"email": "admin@healthservices.com", "password": "admin123"}
```

### 2. Copy Request as cURL

Swagger cho phép export request thành cURL command:
1. Sau khi Execute
2. Tìm nút **"Copy as cURL"**
3. Paste vào terminal để test

### 3. Xem Request/Response Schema

Click vào **"Schema"** tab để xem cấu trúc dữ liệu đầy đủ của request/response.

### 4. Download OpenAPI Spec

Truy cập `http://localhost:8080/v3/api-docs` để tải OpenAPI specification (JSON format).

---

## 🎨 Giao diện Swagger UI

Swagger UI hiển thị:

- ✅ **Tất cả endpoints** được nhóm theo tags
- ✅ **Mô tả chi tiết** cho mỗi API
- ✅ **Request parameters** với validation rules
- ✅ **Response examples** với status codes
- ✅ **Schema models** đầy đủ
- ✅ **Try it out** - Test trực tiếp trên browser
- ✅ **Authorization** - Xác thực JWT dễ dàng

---

## 🔒 Security

- APIs yêu cầu authentication sẽ có biểu tượng 🔒
- Cần authorize trước khi test các APIs được bảo vệ
- Token có thời gian hết hạn (24h), cần login lại khi hết hạn

---

## 🐛 Troubleshooting

### Lỗi: "Cannot access Swagger UI"
✅ Kiểm tra application đang chạy
✅ Truy cập đúng URL: `http://localhost:8080/swagger-ui.html`

### Lỗi: "401 Unauthorized"
✅ Kiểm tra đã click "Authorize" chưa
✅ Kiểm tra token format: `Bearer <token>`
✅ Token có thể đã hết hạn, login lại

### Lỗi: "403 Forbidden"
✅ Kiểm tra role có quyền truy cập API này không
✅ Login với tài khoản đúng role

---

## 📊 So sánh với cách test khác

| Feature | Swagger UI | Postman | cURL |
|---------|-----------|---------|------|
| Giao diện đẹp | ✅ | ✅ | ❌ |
| Test nhanh | ✅ | ✅ | ⚠️ |
| Tài liệu tích hợp | ✅ | ❌ | ❌ |
| Không cần cài đặt | ✅ | ❌ | ✅ |
| Export/Import | ⚠️ | ✅ | ❌ |
| Automation | ❌ | ✅ | ✅ |

---

## 🎯 Best Practices

1. **Luôn Authorize trước** khi test protected APIs
2. **Test theo flow**: Register → Login → Use APIs
3. **Kiểm tra Response** để hiểu cấu trúc dữ liệu
4. **Sử dụng Schema** để biết field nào required
5. **Copy cURL** nếu muốn test bằng command line

---

## 📖 Tài liệu thêm

- **SpringDoc OpenAPI**: https://springdoc.org/
- **Swagger UI**: https://swagger.io/tools/swagger-ui/
- **OpenAPI Specification**: https://swagger.io/specification/

---

## 🎉 Kết luận

Swagger UI giúp bạn:
- ✅ Test API dễ dàng hơn với giao diện trực quan
- ✅ Tự động tạo documentation từ code
- ✅ Chia sẻ API docs với team
- ✅ Không cần công cụ bên ngoài như Postman

**Enjoy testing your APIs! 🚀**

