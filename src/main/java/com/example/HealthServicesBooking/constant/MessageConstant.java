package com.example.HealthServicesBooking.constant;

public class MessageConstant {

    private MessageConstant() {
        throw new IllegalStateException("Constant class");
    }

    // ============ Authentication Messages ============
    public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
    public static final String LOGIN_FAILED = "Đăng nhập thất bại";
    public static final String INVALID_CREDENTIALS = "Email/Username hoặc mật khẩu không đúng";
    public static final String ACCOUNT_DISABLED = "Tài khoản đã bị vô hiệu hóa";

    // ============ Registration Messages ============
    public static final String REGISTER_SUCCESS = "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản";
    public static final String EMAIL_ALREADY_EXISTS = "Email đã tồn tại trong hệ thống";
    public static final String USERNAME_ALREADY_EXISTS = "Username đã tồn tại trong hệ thống";

    // ============ Email Verification Messages ============
    public static final String VERIFICATION_CODE_SENT = "Mã xác thực đã được gửi đến email của bạn";
    public static final String VERIFICATION_SUCCESS = "Xác thực email thành công";
    public static final String VERIFICATION_FAILED = "Mã xác thực không đúng hoặc đã hết hạn";
    public static final String EMAIL_ALREADY_VERIFIED = "Email đã được xác thực trước đó";

    // ============ Password Messages ============
    public static final String PASSWORD_RESET_SENT = "Link đặt lại mật khẩu đã được gửi đến email của bạn";
    public static final String PASSWORD_RESET_SUCCESS = "Đặt lại mật khẩu thành công";
    public static final String PASSWORD_RESET_FAILED = "Không thể đặt lại mật khẩu";
    public static final String PASSWORD_CHANGED = "Đổi mật khẩu thành công";

    // ============ User Messages ============
    public static final String USER_NOT_FOUND = "Không tìm thấy người dùng";
    public static final String USER_UPDATED = "Cập nhật thông tin thành công";
    public static final String USER_DELETED = "Xóa người dùng thành công";

    // ============ Role Messages ============
    public static final String ROLE_NOT_FOUND = "Không tìm thấy vai trò";
    public static final String UNAUTHORIZED_ACCESS = "Bạn không có quyền truy cập";

    // ============ Validation Messages ============
    public static final String INVALID_EMAIL_FORMAT = "Định dạng email không hợp lệ";
    public static final String INVALID_PHONE_FORMAT = "Định dạng số điện thoại không hợp lệ";
    public static final String PASSWORD_TOO_SHORT = "Mật khẩu phải có ít nhất 6 ký tự";
    public static final String REQUIRED_FIELD = "Trường này là bắt buộc";
}
