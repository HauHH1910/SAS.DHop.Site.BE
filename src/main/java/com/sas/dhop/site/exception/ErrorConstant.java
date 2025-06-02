package com.sas.dhop.site.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorConstant {
    UNCATEGORIZED_ERROR(1, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(2, "Không có quyền truy cập", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(3, "Token không hợp lệ", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND(4, "Không tìm thấy email", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(5, "Email hoặc mật khẩu không đúng", HttpStatus.BAD_REQUEST),
    INVALID_KEY(6, "Tham số không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(7, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXIST(8, "Email đã tồn tại", HttpStatus.BAD_REQUEST),
    INVALID_OTP(9, "Mã OTP không hợp lệ", HttpStatus.FORBIDDEN),
    DANCE_TYPE_NOT_FOUND(10, "Không tìm thấy thể loại nhảy", HttpStatus.NOT_FOUND),
    DANCE_TYPE_ALREADY_EXISTS(11, "Đã tồn tại thể loại nhảy này", HttpStatus.FORBIDDEN),
    SENT_EMAIL_ERROR(12, "Không thể gửi OTP, quá trình đăng ký bị hủy bỏ.", HttpStatus.FORBIDDEN),
    BOOKING_NOT_FOUND(13, "Không tìm thấy được booking.", HttpStatus.NOT_FOUND),
    AREA_NOT_FOUND(14, "Không tìm thấy khu vực.", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(15, "Vai trò không tồn tại", HttpStatus.NOT_FOUND),
    ROLE_ACCESS_DENIED(16, "từ chối quuyền truy cập", HttpStatus.NOT_FOUND),
    AREA_ALREADY_EXISTS(17,"Khu vực đã tồn tại hoặc bị trùng lặp", HttpStatus.CONFLICT);

    private final Integer code;

    private final String message;

    private final HttpStatusCode httpStatusCode;
}
