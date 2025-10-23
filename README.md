# Health Services Booking API

Hệ thống API đặt lịch khám bệnh với Spring Boot 3.5.6, JWT Authentication, Email Verification, và Refresh Token.

## 🚀 Quick Start (Test ngay trong 5 phút)

### Bước 1: Tạo Database
```bash
mysql -u root -p -e "CREATE DATABASE healthservicesbooking;"
```

### Bước 2: Chạy Application
```bash
mvn spring-boot:run
```

### Bước 3: Test API
```bash
curl http://localhost:8080/api/auth/test
```

**Nếu thấy `"Auth API is working!"` → ✅ Bạn đã sẵn sàng test!**

---

## 📚 Documentation

- **[QUICK_START.md](QUICK_START.md)** - Hướng dẫn setup và test chi tiết
- **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** - API documentation đầy đủ
- **[CHANGELOG.md](CHANGELOG.md)** - Chi tiết tất cả thay đổi và cải tiến

---

## 🔧 Configuration

### Development (Mặc định)
Không cần config gì! Dùng luôn với:
- Database: `localhost:3306/healthservicesbooking`
- User: `root` / Password: `root`
- Email: Không cần (skip email sending)

### Production
Set environment variables trong `.env`:
```env
DB_URL=jdbc:mysql://localhost:3306/healthservicesbooking
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=your_secure_secret_key
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

---

## 🧪 Testing

### Option 1: Bash Script (Tự động)
```bash
chmod +x test-api.sh
./test-api.sh
```

### Option 2: Postman
Import file: `Health-Services-Booking.postman_collection.json`

### Option 3: Manual cURL
Xem chi tiết trong [QUICK_START.md](QUICK_START.md)

### Option 4: SQL Helpers
```bash
mysql -u root -p healthservicesbooking < test-helpers.sql
```

---

## 🎯 Các tính năng chính

- ✅ JWT Authentication với Refresh Token
- ✅ Email Verification (6-digit code)
- ✅ Forgot Password / Reset Password
- ✅ Role-Based Access Control (ADMIN, DOCTOR, PATIENT)
- ✅ Account Status Check (Active/Inactive)
- ✅ Auto Lock sau 3 violations
- ✅ Async Email Sending
- ✅ Environment Variables cho Security
- ✅ Auto Initialize Default Roles

---

## 📋 API Endpoints

| Method | Endpoint | Mô tả | Auth |
|--------|----------|-------|------|
| POST | `/api/auth/register` | Đăng ký | ❌ |
| POST | `/api/auth/verify-email` | Verify email | ❌ |
| POST | `/api/auth/resend-code` | Gửi lại code | ❌ |
| POST | `/api/auth/login` | Đăng nhập | ❌ |
| POST | `/api/auth/refresh-token` | Refresh token | ❌ |
| POST | `/api/auth/logout` | Đăng xuất | ✅ |
| POST | `/api/auth/forgot-password` | Quên mật khẩu | ❌ |
| POST | `/api/auth/reset-password` | Reset mật khẩu | ❌ |

Xem chi tiết trong [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Security + JWT**
- **MySQL 8.0**
- **Lombok**
- **Spring Mail**
- **Maven**

---

## 📝 Database Schema

Các bảng chính:
- `user` - Thông tin users (với email verification)
- `role` - Roles (ADMIN, DOCTOR, PATIENT)
- `user_role` - Many-to-many relationship
- `refresh_token` - Refresh tokens với expiration

---

## 🔒 Security Features

1. **Environment Variables** - Sensitive data không hardcode
2. **Email Verification** - Bắt buộc verify email
3. **Password Encryption** - BCrypt
4. **JWT Tokens** - Access Token (24h) + Refresh Token (7 ngày)
5. **Account Status Check** - Không cho login nếu bị khóa
6. **Code Expiration** - Verification/Reset codes hết hạn sau 5 phút
7. **Auto Logout** - Force logout tất cả devices sau reset password

---

## 💡 Quick Example

### Đăng ký và Login
```bash
# 1. Đăng ký
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo",
    "email": "demo@test.com",
    "password": "demo123",
    "fullname": "Demo User",
    "mobile": "0999999999"
  }'

# 2. Lấy verification code từ database
mysql -u root -p -e "SELECT code FROM healthservicesbooking.user WHERE username='demo';"

# 3. Verify email
curl -X POST http://localhost:8080/api/auth/verify-email \
  -H "Content-Type: application/json" \
  -d '{
    "email": "demo@test.com",
    "code": "CODE_FROM_STEP_2"
  }'

# 4. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo",
    "password": "demo123"
  }'
```

---

## 🎓 Best Practices Applied

- ✅ Clean Architecture (Controller → Service → Repository)
- ✅ DTO Pattern
- ✅ Global Exception Handling
- ✅ Bean Validation
- ✅ Transaction Management
- ✅ Lazy Loading với JOIN FETCH
- ✅ Async Processing
- ✅ Proper Logging
- ✅ Profile-based Configuration

---

## 🐛 Troubleshooting

### API không chạy?
```bash
# Check MySQL
sudo systemctl status mysql

# Check port 8080
netstat -ano | findstr :8080
```

### Email không gửi được?
- Không sao! API vẫn hoạt động bình thường
- Lấy code từ database để test
- Xem [QUICK_START.md](QUICK_START.md) section Email Configuration

### Database connection error?
- Check MySQL đang chạy
- Check credentials trong `application-dev.properties`
- Đảm bảo database `healthservicesbooking` đã được tạo

---

## 📊 Project Structure

```
src/main/java/com/example/HealthServicesBooking/
├── config/              # Configuration classes
├── constant/            # Constants
├── controller/          # REST Controllers
├── dto/                 # Data Transfer Objects
│   ├── Request/
│   └── Response/
├── entity/              # JPA Entities
├── exception/           # Exception Handling
├── repository/          # Data Access Layer
├── security/            # Security & JWT
│   └── jwt/
└── service/             # Business Logic
```

---

## 🔜 Recommended Next Steps

1. 🧪 **Unit Tests** - Viết tests cho services
2. 📊 **Swagger UI** - API documentation interface  
3. 🔄 **Flyway** - Database migration management
4. 🚦 **Rate Limiting** - Prevent brute force attacks
5. 📄 **Pagination** - For list endpoints
6. 💾 **Redis** - Caching layer

---

## 📞 Support

- Documentation: Xem các file `.md` trong project
- Issues: Check console logs và database
- Email: support@healthservices.com

---

## 📄 License

MIT License - Free to use and modify

---

## 🎉 Ready to Test!

Tất cả đã setup xong! Chạy:

```bash
mvn spring-boot:run
```

Sau đó test với:
```bash
curl http://localhost:8080/api/auth/test
```

**Chúc bạn code vui vẻ! 🚀**

---

*Được tạo với ❤️ bằng Spring Boot*

