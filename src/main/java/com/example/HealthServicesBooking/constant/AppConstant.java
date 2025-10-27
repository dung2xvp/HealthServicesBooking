package com.example.HealthServicesBooking.constant;

public class AppConstant {
    
    // Application Name
    public static final String APP_NAME = "Health Services Booking";
    
    // Email Templates
    public static final String EMAIL_VERIFICATION_SUBJECT = "Xác thực tài khoản - " + APP_NAME;
    public static final String PASSWORD_RESET_SUBJECT = "Khôi phúc mật khẩu - " + APP_NAME;
    public static final String APPOINTMENT_CONFIRMATION_SUBJECT = "Xác nhận lịch hẹn - " + APP_NAME;
    public static final String APPOINTMENT_REMINDER_SUBJECT = "Nhắc nhở lịch hẹn - " + APP_NAME;
    
    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "DESC";
    
    // Date Format
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
    
    // Verification Code Length
    public static final int VERIFICATION_CODE_LENGTH = 6;
    
    private AppConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

