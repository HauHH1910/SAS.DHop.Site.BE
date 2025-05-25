package com.sas.dhop.site.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {
    AUTHENTICATION_LOGIN("Đăng nhập thành công"),
    GET_ALL_USER("Lấy ra tất cả người dùng thành công"),
    GET_USER_INFO("Xem hồ sơ cá nhân của người dùng thành công"),
    CREATE_USER("Tạo người dùng thành công"),
    DELETE_USER("Xóa người dùng thành công"),
    UPDATE_USER("Cập nhật người dùng thành công"),
    GET_USER("Lấy ra người dùng thành công"),
    INTROSPECT_TOKEN("Kiểm tra token thành công"),
    REFRESH_TOKEN("Gia hạn token thành công"),
    GET_ALL_ROLE("Lấy tất cả vai trò thành công"),
    CREATE_ROLE("Tạo vai trò mới thành công"),
    DELETE_ROLE("Xóa vai trò thành công"),
    REGISTER_SUCCESS("Đăng ký thành công vui lòng xác nhận OTP qua email"),
    VERIFICATION_OTP_SUCCESS("Xác thực tài khoản thành công");
    private final String message;
}
