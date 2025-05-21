package com.sas.dhop.site.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {
    AUTHENTICATION_LOGIN("Đăng nhập thành công"),
    ;
    private final String message;
}
