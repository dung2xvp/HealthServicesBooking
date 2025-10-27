package com.example.HealthServicesBooking.constant;

public class MessageConstant {
    
    // Authentication Messages
    public static final String REGISTER_SUCCESS = "Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.";
    public static final String LOGIN_SUCCESS = "Đăng nhập thành công!";
    public static final String LOGOUT_SUCCESS = "Đăng xuất thành công!";
    public static final String EMAIL_VERIFIED = "Email đã được xác thực thành công!";
    public static final String EMAIL_ALREADY_VERIFIED = "Email đã được xác thực trước đó.";
    public static final String VERIFICATION_CODE_SENT = "Mã xác thực đã được gửi đến email của bạn.";
    public static final String PASSWORD_RESET_EMAIL_SENT = "Email khôi phục mật khẩu đã được gửi.";
    public static final String PASSWORD_RESET_SUCCESS = "Mật khẩu đã được thay đổi thành công!";
    
    // Error Messages
    public static final String EMAIL_ALREADY_EXISTS = "Email đã tồn tại trong hệ thống.";
    public static final String PHONE_ALREADY_EXISTS = "Số điện thoại đã tồn tại trong hệ thống.";
    public static final String INVALID_CREDENTIALS = "Email hoặc mật khẩu không chính xác.";
    public static final String INVALID_VERIFICATION_CODE = "Mã xác thực không hợp lệ hoặc đã hết hạn.";
    public static final String INVALID_RESET_TOKEN = "Token khôi phục mật khẩu không hợp lệ hoặc đã hết hạn.";
    public static final String USER_NOT_FOUND = "Không tìm thấy người dùng.";
    public static final String ROLE_NOT_FOUND = "Không tìm thấy vai trò.";
    public static final String EMAIL_NOT_VERIFIED = "Email chưa được xác thực. Vui lòng xác thực email trước khi đăng nhập.";
    public static final String ACCOUNT_INACTIVE = "Tài khoản đã bị vô hiệu hóa.";
    public static final String UNAUTHORIZED = "Bạn không có quyền truy cập tài nguyên này.";
    public static final String INVALID_TOKEN = "Token không hợp lệ.";
    public static final String TOKEN_EXPIRED = "Token đã hết hạn.";
    
    // Facility Messages
    public static final String FACILITY_NOT_FOUND = "Không tìm thấy cơ sở y tế.";
    public static final String FACILITY_CREATED = "Tạo cơ sở y tế thành công.";
    public static final String FACILITY_UPDATED = "Cập nhật cơ sở y tế thành công.";
    public static final String FACILITY_DELETED = "Xóa cơ sở y tế thành công.";
    
    // Service Messages
    public static final String SERVICE_NOT_FOUND = "Không tìm thấy dịch vụ y tế.";
    public static final String SERVICE_CREATED = "Tạo dịch vụ y tế thành công.";
    public static final String SERVICE_UPDATED = "Cập nhật dịch vụ y tế thành công.";
    public static final String SERVICE_DELETED = "Xóa dịch vụ y tế thành công.";
    
    // Doctor Messages
    public static final String DOCTOR_NOT_FOUND = "Không tìm thấy bác sĩ.";
    public static final String DOCTOR_CREATED = "Tạo hồ sơ bác sĩ thành công.";
    public static final String DOCTOR_UPDATED = "Cập nhật hồ sơ bác sĩ thành công.";
    public static final String DOCTOR_DELETED = "Xóa hồ sơ bác sĩ thành công.";
    public static final String DOCTOR_SCHEDULE_CREATED = "Tạo lịch làm việc thành công.";
    public static final String DOCTOR_SCHEDULE_UPDATED = "Cập nhật lịch làm việc thành công.";
    public static final String DOCTOR_SCHEDULE_DELETED = "Xóa lịch làm việc thành công.";
    
    // Patient Messages
    public static final String PATIENT_NOT_FOUND = "Không tìm thấy bệnh nhân.";
    public static final String PATIENT_CREATED = "Tạo hồ sơ bệnh nhân thành công.";
    public static final String PATIENT_UPDATED = "Cập nhật hồ sơ bệnh nhân thành công.";
    
    // Appointment Messages
    public static final String APPOINTMENT_NOT_FOUND = "Không tìm thấy lịch hẹn.";
    public static final String APPOINTMENT_CREATED = "Đặt lịch hẹn thành công.";
    public static final String APPOINTMENT_UPDATED = "Cập nhật lịch hẹn thành công.";
    public static final String APPOINTMENT_CANCELLED = "Hủy lịch hẹn thành công.";
    public static final String APPOINTMENT_CONFIRMED = "Xác nhận lịch hẹn thành công.";
    public static final String APPOINTMENT_COMPLETED = "Hoàn thành lịch hẹn thành công.";
    public static final String APPOINTMENT_TIME_CONFLICT = "Thời gian đặt lịch bị trùng với lịch hẹn khác.";
    public static final String APPOINTMENT_CANNOT_CANCEL = "Không thể hủy lịch hẹn này.";
    public static final String APPOINTMENT_ALREADY_COMPLETED = "Lịch hẹn đã hoàn thành.";
    
    // Payment Messages
    public static final String PAYMENT_NOT_FOUND = "Không tìm thấy thông tin thanh toán.";
    public static final String PAYMENT_CREATED = "Tạo thông tin thanh toán thành công.";
    public static final String PAYMENT_UPDATED = "Cập nhật thông tin thanh toán thành công.";
    public static final String PAYMENT_COMPLETED = "Thanh toán thành công.";
    public static final String PAYMENT_FAILED = "Thanh toán thất bại.";
    
    // Medical Record Messages
    public static final String MEDICAL_RECORD_NOT_FOUND = "Không tìm thấy hồ sơ khám bệnh.";
    public static final String MEDICAL_RECORD_CREATED = "Tạo hồ sơ khám bệnh thành công.";
    public static final String MEDICAL_RECORD_UPDATED = "Cập nhật hồ sơ khám bệnh thành công.";
    
    private MessageConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

