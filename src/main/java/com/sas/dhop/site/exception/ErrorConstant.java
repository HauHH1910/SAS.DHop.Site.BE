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
    ROLE_ACCESS_DENIED(16, "Từ chối quyền truy cập", HttpStatus.FORBIDDEN),
    AREA_ALREADY_EXISTS(17, "Khu vực đã tồn tại hoặc bị trùng lặp", HttpStatus.CONFLICT),
    ROOM_NOT_FOUND(18, "Không tìm thấy phòng", HttpStatus.FORBIDDEN),
    STATUS_NOT_FOUND(19, "Không tìm thấy trạng thái", HttpStatus.NOT_FOUND),

    EMAIL_NOT_NULL(20, "Email không được trống", HttpStatus.FORBIDDEN),
    EMAIL_NOT_BLANK(21, "Email không được trống", HttpStatus.FORBIDDEN),
    EMAIL_NOT_EMPTY(22, "Email không được rỗng", HttpStatus.FORBIDDEN),

    NAME_NOT_BLANK(23, "Tên không được trống", HttpStatus.FORBIDDEN),
    NAME_NOT_EMPTY(24, "Tên không được bỏ", HttpStatus.FORBIDDEN),
    NAME_NOT_NULL(25, "Tên không được rỗng", HttpStatus.FORBIDDEN),

    PASSWORD_NOT_BLANK(26, "Mật khẩu không được rỗng", HttpStatus.FORBIDDEN),
    PASSWORD_NOT_EMPTY(27, "Mật khẩu không được bỏ", HttpStatus.FORBIDDEN),
    PASSWORD_NOT_NULL(28, "Mật khẩu không được trống", HttpStatus.FORBIDDEN),

    NOT_DANCER(29, "Người được chọn không phải dancer", HttpStatus.FORBIDDEN),
    MOT_CHOREOGRAPHY(30, "Người được chọn không phải choreography", HttpStatus.FORBIDDEN),
    NOT_FOUND_DANCE_TYPE(31, "Không tìm được thể loại nhảy phù hợp", HttpStatus.NOT_FOUND),

    SUBSCRIPTION_EXIST(32, "Gói dịch vụ đã tồn tại", HttpStatus.FORBIDDEN),
    SUBSCRIPTION_NOT_FOUND(33, "Không tìm thấy gói dịch vụ", HttpStatus.NOT_FOUND),
    PARTICIPANT_NOT_FOUND(34, "Không tìm thấy người dùng", HttpStatus.FORBIDDEN),

    BOOKING_NOT_ACCEPTABLE(35, "Đơn thuê không được chấp nhận", HttpStatus.NOT_ACCEPTABLE),
    BOOKING_INACTIVATE(36, "Đơn thuê chưa được xác nhận", HttpStatus.NOT_ACCEPTABLE),
    BOOKING_CAN_NOT_CANCEL(37, "Không thể hủy đơn thuê", HttpStatus.FORBIDDEN),
    BOOKING_CAN_NOT_COMPLETE(38, "Đơn thuê không thể hoàn thành ", HttpStatus.FORBIDDEN),
    BOOKING_CAN_NOT_END_WORK(39, "Không thể kết thúc được công việc", HttpStatus.FORBIDDEN),

    ARTICLE_NOT_FOUND(40, "Không tìm thấy bài báo", HttpStatus.NOT_FOUND),

    ;

    private final Integer code;
    private final String message;
    private final HttpStatusCode httpStatusCode;
}
