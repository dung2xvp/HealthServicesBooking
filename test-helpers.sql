-- Health Services Booking - SQL Helper Commands
-- Các câu lệnh SQL hữu ích cho testing và debugging

-- ============================================
-- 1. DATABASE SETUP
-- ============================================

-- Tạo database (chạy lần đầu)
CREATE DATABASE IF NOT EXISTS healthservicesbooking;
USE healthservicesbooking;

-- ============================================
-- 2. CHECK DATA
-- ============================================

-- Xem tất cả users
SELECT 
    id,
    username,
    email,
    fullname,
    is_active,
    email_verified,
    created_at
FROM user
ORDER BY created_at DESC;

-- Xem tất cả roles
SELECT * FROM role;

-- Xem user-role mappings
SELECT 
    u.username,
    u.email,
    r.name as role_name
FROM user u
JOIN user_role ur ON u.id = ur.user_id
JOIN role r ON ur.role_id = r.id
ORDER BY u.username;

-- Xem refresh tokens
SELECT 
    rt.token,
    u.username,
    rt.expiry_date,
    rt.created_at,
    CASE 
        WHEN rt.expiry_date > NOW() THEN 'Valid'
        ELSE 'Expired'
    END as status
FROM refresh_token rt
JOIN user u ON rt.user_id = u.id
ORDER BY rt.created_at DESC;

-- ============================================
-- 3. GET VERIFICATION/RESET CODES
-- ============================================

-- Lấy verification code của user cụ thể
SELECT 
    username,
    email,
    code as verification_code,
    code_expiry_date,
    email_verified,
    CASE 
        WHEN code_expiry_date > NOW() THEN 'Valid'
        WHEN code_expiry_date IS NULL THEN 'No code'
        ELSE 'Expired'
    END as code_status
FROM user
WHERE username = 'YOUR_USERNAME_HERE';

-- Lấy code mới nhất (dùng khi test register)
SELECT 
    username,
    email,
    code as verification_code,
    code_expiry_date,
    created_at
FROM user
ORDER BY created_at DESC
LIMIT 1;

-- ============================================
-- 4. MANUAL VERIFICATION (For Testing Only)
-- ============================================

-- Verify email manually (bypass email verification cho test)
UPDATE user 
SET email_verified = true,
    code = NULL,
    code_expiry_date = NULL
WHERE username = 'YOUR_USERNAME_HERE';

-- Verify email bằng email
UPDATE user 
SET email_verified = true,
    code = NULL,
    code_expiry_date = NULL
WHERE email = 'YOUR_EMAIL_HERE';

-- Verify user mới nhất
UPDATE user 
SET email_verified = true,
    code = NULL,
    code_expiry_date = NULL
WHERE id = (SELECT id FROM (SELECT id FROM user ORDER BY created_at DESC LIMIT 1) as temp);

-- ============================================
-- 5. ACCOUNT MANAGEMENT
-- ============================================

-- Kích hoạt tài khoản bị khóa
UPDATE user 
SET is_active = true,
    bad_point = 0,
    delete_by = NULL
WHERE username = 'YOUR_USERNAME_HERE';

-- Khóa tài khoản
UPDATE user 
SET is_active = false,
    delete_by = 'ADMIN'
WHERE username = 'YOUR_USERNAME_HERE';

-- Reset bad point
UPDATE user 
SET bad_point = 0
WHERE username = 'YOUR_USERNAME_HERE';

-- ============================================
-- 6. PASSWORD MANAGEMENT
-- ============================================

-- Check user có code reset password không
SELECT 
    username,
    email,
    code,
    code_expiry_date,
    TIMESTAMPDIFF(MINUTE, NOW(), code_expiry_date) as minutes_until_expiry
FROM user
WHERE email = 'YOUR_EMAIL_HERE';

-- ============================================
-- 7. DEBUGGING & ANALYSIS
-- ============================================

-- Count users by role
SELECT 
    r.name as role_name,
    COUNT(ur.user_id) as user_count
FROM role r
LEFT JOIN user_role ur ON r.id = ur.role_id
GROUP BY r.id, r.name;

-- Tìm users chưa verify email
SELECT 
    username,
    email,
    created_at,
    TIMESTAMPDIFF(HOUR, created_at, NOW()) as hours_since_registration
FROM user
WHERE email_verified = false
ORDER BY created_at DESC;

-- Tìm users có code hết hạn
SELECT 
    username,
    email,
    code_expiry_date,
    TIMESTAMPDIFF(MINUTE, code_expiry_date, NOW()) as minutes_expired
FROM user
WHERE code IS NOT NULL 
  AND code_expiry_date < NOW()
ORDER BY code_expiry_date DESC;

-- Tìm refresh tokens hết hạn
SELECT 
    u.username,
    rt.token,
    rt.expiry_date,
    TIMESTAMPDIFF(DAY, rt.expiry_date, NOW()) as days_expired
FROM refresh_token rt
JOIN user u ON rt.user_id = u.id
WHERE rt.expiry_date < NOW()
ORDER BY rt.expiry_date DESC;

-- Active users trong 24h qua
SELECT 
    username,
    email,
    created_at
FROM user
WHERE created_at >= DATE_SUB(NOW(), INTERVAL 24 HOUR)
ORDER BY created_at DESC;

-- ============================================
-- 8. CLEANUP (Development Only)
-- ============================================

-- Xóa refresh tokens hết hạn
DELETE FROM refresh_token 
WHERE expiry_date < NOW();

-- Xóa codes hết hạn
UPDATE user 
SET code = NULL,
    code_expiry_date = NULL
WHERE code_expiry_date < NOW();

-- Xóa test user (cẩn thận!)
DELETE FROM user 
WHERE username LIKE 'test%' 
   OR email LIKE 'test%';

-- Xóa tất cả users (NGUY HIỂM - Chỉ dùng trong development!)
-- DELETE FROM refresh_token;
-- DELETE FROM user_role;
-- DELETE FROM user;

-- ============================================
-- 9. STATISTICS
-- ============================================

-- Tổng quan hệ thống
SELECT 
    (SELECT COUNT(*) FROM user) as total_users,
    (SELECT COUNT(*) FROM user WHERE email_verified = true) as verified_users,
    (SELECT COUNT(*) FROM user WHERE is_active = true) as active_users,
    (SELECT COUNT(*) FROM user WHERE is_active = false) as inactive_users,
    (SELECT COUNT(*) FROM refresh_token) as active_sessions,
    (SELECT COUNT(*) FROM role) as total_roles;

-- Users mới trong 7 ngày
SELECT 
    DATE(created_at) as date,
    COUNT(*) as new_users
FROM user
WHERE created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
GROUP BY DATE(created_at)
ORDER BY date DESC;

-- ============================================
-- 10. QUICK TEST SETUP
-- ============================================

-- Tạo admin user nhanh (cho testing)
-- Lưu ý: Password đã encode nên không dùng được, phải register qua API
/*
INSERT INTO user (id, username, email, password, fullname, is_active, email_verified, created_at, update_at, bad_point)
VALUES (
    UUID(),
    'admin',
    'admin@example.com',
    '$2a$10$...',  -- Phải dùng BCrypt encoded password
    'Administrator',
    true,
    true,
    NOW(),
    NOW(),
    0
);
*/

-- Assign ADMIN role (sau khi user được tạo qua API)
/*
INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM user u, role r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';
*/

-- ============================================
-- 11. COMMON QUERIES FOR TESTING
-- ============================================

-- Get user details with all info
SELECT 
    u.id,
    u.username,
    u.email,
    u.fullname,
    u.mobile,
    u.gender,
    u.birthday,
    u.age,
    u.is_active,
    u.email_verified,
    u.bad_point,
    u.code,
    u.code_expiry_date,
    u.created_at,
    GROUP_CONCAT(r.name) as roles
FROM user u
LEFT JOIN user_role ur ON u.id = ur.user_id
LEFT JOIN role r ON ur.role_id = r.id
WHERE u.username = 'YOUR_USERNAME_HERE'
GROUP BY u.id;

-- Check if username or email exists
SELECT 
    CASE 
        WHEN EXISTS (SELECT 1 FROM user WHERE username = 'test_username') 
        THEN 'Username exists' 
        ELSE 'Username available' 
    END as username_status,
    CASE 
        WHEN EXISTS (SELECT 1 FROM user WHERE email = 'test@example.com') 
        THEN 'Email exists' 
        ELSE 'Email available' 
    END as email_status;

-- ============================================
-- NOTES:
-- ============================================
-- 1. Thay 'YOUR_USERNAME_HERE' hoặc 'YOUR_EMAIL_HERE' bằng giá trị thực tế
-- 2. Các câu lệnh DELETE/UPDATE phải cẩn thận trong production
-- 3. Verification code có hiệu lực 5 phút (300000ms)
-- 4. Refresh token có hiệu lực 7 ngày (604800000ms)
-- ============================================

