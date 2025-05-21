package com.sas.dhop.site.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorConstant {
    INVALID_ERROR(1, "Lỗi", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(2, "Không có quyền truy cấp", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(3, "Token không hợp lệ", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND(4, "Không tìm thấy email", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(5, "Email hoặc mật khẩu không đúng", HttpStatus.BAD_REQUEST),
    ;
    private final Integer code;

    private final String message;

    private final HttpStatusCode httpStatusCode;
}
