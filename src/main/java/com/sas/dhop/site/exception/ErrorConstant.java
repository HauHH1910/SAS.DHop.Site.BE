package com.sas.dhop.site.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorConstant {
    INVALID_ERROR(1, "Lỗi", HttpStatus.INTERNAL_SERVER_ERROR),

    ;
    private final Integer code;

    private final String message;

    private final HttpStatusCode httpStatusCode;
}
