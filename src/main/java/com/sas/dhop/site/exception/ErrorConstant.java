package com.sas.dhop.site.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorConstant {
    UNCATEGORIZED_ERROR(1, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(2, "Không có quyền truy cấp", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(3, "Token không hợp lệ", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND(4, "Không tìm thấy email", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(5, "Email hoặc mật khẩu không đúng", HttpStatus.BAD_REQUEST),
    INVALID_KEY(6, "Tham số không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(7, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXIST(8, "Email đã tồn tại", HttpStatus.BAD_REQUEST),
    INVALID_OTP(9, "Mã OTP không hợp lệ", HttpStatus.FORBIDDEN),
    ;
    private final Integer code;

    private final String message;

    private final HttpStatusCode httpStatusCode;
}
