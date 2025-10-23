package com.example.HealthServicesBooking.constant;

public class AppConstant {

    private AppConstant() {
        throw new IllegalStateException("Constant class");
    }

    // ============ JWT Constants ============
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    public static final String JWT_CLAIM_ROLES = "roles";
    public static final String JWT_CLAIM_USER_ID = "userId";

    // ============ Verification Code Constants ============
    public static final Integer CODE_LENGTH = 6;
    public static final Long CODE_EXPIRATION_MINUTES = 15L; // 15 phút

    // ============ Pagination Constants ============
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "DESC";

    // ============ Gender Constants ============
    public static final Integer GENDER_FEMALE = 0;
    public static final Integer GENDER_MALE = 1;
    public static final Integer GENDER_OTHER = 2;

    // ============ Date Format Constants ============
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    // ============ Email Constants ============
    public static final String EMAIL_FROM = "noreply@healthservices.com";
    public static final String EMAIL_VERIFICATION_SUBJECT = "Xác thực tài khoản - Health Services";
    public static final String EMAIL_RESET_PASSWORD_SUBJECT = "Đặt lại mật khẩu - Health Services";
}
