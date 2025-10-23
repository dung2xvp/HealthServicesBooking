# Quick Start Guide - Test API ngay lập tức

## 🚀 Bước 1: Setup Database (2 phút)

### Tạo database MySQL:
```sql
CREATE DATABASE healthservicesbooking;
```

Hoặc dùng command line:
```bash
mysql -u root -p -e "CREATE DATABASE healthservicesbooking;"
```

---

## 🚀 Bước 2: Cấu hình (Chọn 1 trong 2 cách)

### **Cách 1: Dùng mặc định (Nhanh nhất - Khuyến nghị cho test)**

Không cần làm gì! Application sẽ dùng config mặc định trong `application.properties`.

**Lưu ý:** 
- Database: `localhost:3306/healthservicesbooking`
- Username: `root`
- Password: `root`
- Email sẽ KHÔNG gửi được (nhưng API vẫn hoạt động bình thường)

### **Cách 2: Dùng Environment Variables (Cho production)**

Tạo file `.env` ở root project:

```env
# Database
DB_URL=jdbc:mysql://localhost:3306/healthservicesbooking
DB_USERNAME=root
DB_PASSWORD=your_actual_password

# JWT
JWT_SECRET=mySecretKeyForHealthServicesBookingApplicationThatIsAtLeast256BitsLong

# Email (Bỏ qua nếu chỉ test API)
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

---

## 🚀 Bước 3: Chạy Application

```bash
# Build và chạy
mvn clean install
mvn spring-boot:run
```

**Hoặc nếu dùng IDE (IntelliJ/Eclipse):**
- Run `HealthServicesBookingApplication.java`

**Output thành công:**
```
Started HealthServicesBookingApplication in X seconds
Initializing default roles...
Created role: ROLE_ADMIN
Created role: ROLE_DOCTOR
Created role: ROLE_PATIENT
Default roles initialized successfully
```

---

## 🚀 Bước 4: Test API

### ✅ Test 1: Kiểm tra API đang chạy

```bash
curl http://localhost:8080/api/auth/test
```

**Kết quả mong đợi:**
```json
{
  "message": "Auth API is working!",
  "timestamp": "2024-10-22T..."
}
```

---

### ✅ Test 2: Đăng ký tài khoản

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "fullname": "Test User",
    "mobile": "0123456789",
    "gender": 1,
    "address": "123 Test Street",
    "birthday": "1990-01-01"
  }'
```

**Kết quả mong đợi:** `201 CREATED`
```json
{
  "message": "User registered successfully. Please check your email for verification code.",
  "user": {
    "id": "...",
    "username": "testuser",
    "email": "test@example.com",
    "fullname": "Test User",
    "isActive": true,
    "roles": ["ROLE_PATIENT"]
  }
}
```

**⚠️ Lưu ý về Email:**
- Nếu CHƯA config email → Email sẽ KHÔNG gửi được nhưng user vẫn được tạo
- Check console log để xem verification code (trong development mode)

---

### ✅ Test 3: Check Database

Kiểm tra user đã được tạo:

```sql
USE healthservicesbooking;
SELECT * FROM user;
SELECT * FROM role;
SELECT * FROM user_role;
```

**Bạn sẽ thấy:**
- User `testuser` đã được tạo
- 3 roles đã tồn tại (ADMIN, DOCTOR, PATIENT)
- User được gán role PATIENT

---

### ✅ Test 4: Login (Có 2 cách)

#### Cách 1: Login KHÔNG cần verify email (Development)

**Tạm thời set emailVerified = true trong database:**
```sql
UPDATE user SET email_verified = true WHERE username = 'testuser';
```

Sau đó login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

#### Cách 2: Verify email trước (Nếu đã config SMTP)

**Lấy verification code từ email hoặc database:**
```sql
SELECT code FROM user WHERE username = 'testuser';
```

**Verify email:**
```bash
curl -X POST http://localhost:8080/api/auth/verify-email \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "code": "123456"
  }'
```

**Sau đó login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

**Kết quả mong đợi:**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "tokenType": "Bearer",
  "user": {
    "id": "...",
    "username": "testuser",
    "email": "test@example.com",
    "fullname": "Test User",
    "roles": ["ROLE_PATIENT"]
  }
}
```

**⚠️ LƯU ACCESS TOKEN để dùng cho các request khác!**

---

### ✅ Test 5: Test Authenticated Endpoint

Lấy access token từ bước trước, thay vào `YOUR_ACCESS_TOKEN`:

```bash
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

### ✅ Test 6: Refresh Token

```bash
curl -X POST http://localhost:8080/api/auth/refresh-token \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN"
  }'
```

---

### ✅ Test 7: Logout

```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## 🐛 Troubleshooting

### Lỗi 1: "Communications link failure"
**Nguyên nhân:** MySQL chưa chạy hoặc sai credentials

**Giải pháp:**
```bash
# Check MySQL đang chạy
sudo systemctl status mysql

# Hoặc với Windows:
net start MySQL80
```

### Lỗi 2: "Access denied for user"
**Nguyên nhân:** Sai username/password MySQL

**Giải pháp:**
- Sửa trong `application-dev.properties`
- Hoặc set environment variable `DB_USERNAME` và `DB_PASSWORD`

### Lỗi 3: Email không gửi được
**Nguyên nhân:** Chưa config SMTP

**Giải pháp cho test:** 
- Bỏ qua! API vẫn hoạt động
- Lấy verification code từ database:
```sql
SELECT code FROM user WHERE email = 'test@example.com';
```

### Lỗi 4: "User not found" khi login
**Nguyên nhân:** User chưa được tạo hoặc sai username

**Giải pháp:**
```sql
-- Check user tồn tại
SELECT * FROM user WHERE username = 'testuser';
```

---

## 📱 Test với Postman

### Import Collection:

1. Tạo Collection mới: "Health Services Booking"
2. Add các requests:

**GET Test API**
- URL: `http://localhost:8080/api/auth/test`
- Method: GET

**POST Register**
- URL: `http://localhost:8080/api/auth/register`
- Method: POST
- Body (JSON):
```json
{
  "username": "postmanuser",
  "email": "postman@example.com",
  "password": "password123",
  "fullname": "Postman User",
  "mobile": "0987654321",
  "gender": 1,
  "address": "456 Postman Street",
  "birthday": "1995-05-15"
}
```

**POST Login**
- URL: `http://localhost:8080/api/auth/login`
- Method: POST
- Body (JSON):
```json
{
  "username": "postmanuser",
  "password": "password123"
}
```

**Lưu accessToken vào Environment Variable:**
- Tên: `accessToken`
- Value: Copy từ response

**Authenticated Requests:**
- Add Header: `Authorization: Bearer {{accessToken}}`

---

## ✅ Checklist Để Test Thành Công

- [ ] MySQL đang chạy
- [ ] Database `healthservicesbooking` đã được tạo
- [ ] Application chạy thành công (port 8080)
- [ ] Test endpoint `/api/auth/test` trả về 200 OK
- [ ] Đăng ký user thành công
- [ ] User xuất hiện trong database
- [ ] 3 roles đã được tạo tự động
- [ ] Login thành công và nhận được tokens
- [ ] (Optional) Email được gửi nếu đã config SMTP

---

## 🎯 Test Flow Hoàn Chỉnh (Recommended)

```bash
# 1. Test API
curl http://localhost:8080/api/auth/test

# 2. Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"demo","email":"demo@test.com","password":"demo123","fullname":"Demo User","mobile":"0999999999"}'

# 3. Get verification code from database
mysql -u root -p -e "USE healthservicesbooking; SELECT code FROM user WHERE username='demo';"

# 4. Verify email
curl -X POST http://localhost:8080/api/auth/verify-email \
  -H "Content-Type: application/json" \
  -d '{"email":"demo@test.com","code":"YOUR_CODE_HERE"}'

# 5. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"demo","password":"demo123"}'

# Save the accessToken and refreshToken from response!
```

---

## 🎉 Nếu Mọi Thứ Hoạt Động

Bạn sẽ thấy:
- ✅ API endpoints trả về đúng response
- ✅ Database có users và roles
- ✅ JWT tokens được generate
- ✅ Authentication và Authorization hoạt động
- ✅ Refresh token mechanism hoạt động

**Chúc mừng! API đã sẵn sàng để phát triển tiếp!** 🎊

---

## 📞 Cần Giúp Đỡ?

Nếu gặp lỗi:
1. Check console logs
2. Check MySQL logs
3. Tham khảo `API_DOCUMENTATION.md` cho chi tiết
4. Tham khảo `CHANGELOG.md` cho troubleshooting

**Lưu ý:** Để test nhanh nhất, chỉ cần MySQL chạy với credentials mặc định (root/root), không cần config email!

