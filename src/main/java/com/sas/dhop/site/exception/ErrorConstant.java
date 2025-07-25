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
    NOT_FOUND_CHOREOGRAPHY(40, "Không tìm thấy biên đạo", HttpStatus.NOT_FOUND),
    PERFORMANCE_NOT_FOUND(41, "Không tìm thấy bằng chứng", HttpStatus.NOT_FOUND),
    DANCER_START_TIME_NOT_NULL(42, "Thời gian bắt đầu không được trống", HttpStatus.FORBIDDEN),
    DANCER_END_TIME_NOT_NULL(43, "Thời gian kết thúc không được trống", HttpStatus.FORBIDDEN),
    ARTICLE_NOT_FOUND(44, "Không tìm thấy bài báo", HttpStatus.NOT_FOUND),

    // Start dancer booking validate enum
    BOOKING_DATE_NOT_NULL(1, "ngày diễn không được để trống", HttpStatus.FORBIDDEN),
    BOOKING_DATE_NOT_BLANK(2, "ngày diễn không được để rỗng", HttpStatus.FORBIDDEN),
    END_TIME_NOT_NULL(3, "thời gian kết thúc không được trống", HttpStatus.FORBIDDEN),
    END_TIME_NOT_BLANK(4, "Thời gian kết thúc không được rỗng", HttpStatus.FORBIDDEN),
    ADDRESS_NOT_NULL(5, "Địa chỉ không được trống", HttpStatus.FORBIDDEN),
    ADDRESS_NOT_BLANK(6, "Địa chỉ không được rỗng", HttpStatus.FORBIDDEN),
    DETAIL_NOT_BLANK(7, "Thông tin chi tiết không được rỗng", HttpStatus.FORBIDDEN),
    DETAIL_NOT_NULL(7, "Thông tin chi tiết không được trống", HttpStatus.FORBIDDEN),
    DANCER_NOT_BLANK(8, "Nhóm nhảy không được để trống", HttpStatus.FORBIDDEN),
    DANCER_NOT_NULL(8, "Nhóm nhảy không được để rỗng", HttpStatus.FORBIDDEN),
    AREA_NOT_BLANK(9, "Khu vực không được để trống", HttpStatus.FORBIDDEN),
    AREA_NOT_NULL(9, "Khu vực không được để rỗng", HttpStatus.FORBIDDEN),
    DANCE_TYPE_NAME_NOT_BLANK(10, "Thể loại nhảy không được trống", HttpStatus.FORBIDDEN),
    DANCE_TYPE_NAME_NOT_NULL(10, "Thể loại nhảy không được rỗng", HttpStatus.FORBIDDEN),
    PRICE_NOT_BLANK(11, "Giá không được trống", HttpStatus.FORBIDDEN),
    PRICE_NOT_NULL(11, "Giá không được rỗng", HttpStatus.FORBIDDEN),
    DANCER_PHONE_NOT_BLANK(12, "Số điện thoại của nhóm nhảy không được trống", HttpStatus.FORBIDDEN),
    DANCER_PHONE_NOT_NULL(12, "Số điện thoại của nhóm nhảy không được rỗng", HttpStatus.FORBIDDEN),
    CUSTOMER_PHONE_NOT_BLANK(13, "Số điện thoại của người dùng không được trống", HttpStatus.FORBIDDEN),
    CUSTOMER_PHONE_NOT_NULL(13, "Số điện thoại của người dùng không được rỗng", HttpStatus.FORBIDDEN),
    NUMBER_OF_TEAM_MEMBER_NOT_BLANK(14, "Số lượng thành viên không được rỗng", HttpStatus.FORBIDDEN),
    NUMBER_OF_TEAM_MEMBER_NOT_NULL(14, "Số lượng thành viên không được trống", HttpStatus.FORBIDDEN),
    NUMBER_OF_TEAM_MEMBER_MIN_VALUE(14, "Số lượng thành viên ít nhất là {value}", HttpStatus.FORBIDDEN),
    // End dancer booking validate enum

    CAN_NOT_FEEDBACK(45, "Không thể đánh giá dịch vụ này", HttpStatus.NOT_ACCEPTABLE),
    INVALID_BOOKING(46, "Dịch vụ thuê không hợp lệ", HttpStatus.NOT_ACCEPTABLE),
    INVALID_MINIMUM_PRICE(47, "Số tiền tối thiểu không hợp lệ", HttpStatus.NOT_ACCEPTABLE),
    INVALID_USER(47, "Người dùng không hợp lệ", HttpStatus.NOT_ACCEPTABLE),
    DANCER_TEAM_SIZE_CONFLICT(48, "Xung đột về số lượng thành viên", HttpStatus.CONFLICT),
    CHOOREOGRAPHY_TIME_CONFLICT(49, "Xung đột thời gian làm việc của biên đạo", HttpStatus.CONFLICT),
    CAN_NOT_UPDATE_BOOKING(50, "Không thể cập nhập thông tin của dịch vụ", HttpStatus.FORBIDDEN),
    PERFORMANCE_CAN_NOT_BE_DELETED(51, "Không thể xóa được vì thuộc về một phần của hợp đồng", HttpStatus.FORBIDDEN),
    CAN_NOT_COMPLAIN(52, "Không thể khiếu nại", HttpStatus.NOT_ACCEPTABLE),
    BOOKING_STATUS_NOT_FOUND(53, "Không tìm thấy trạng thái", HttpStatus.NOT_FOUND),
    SUBSCRIPTION_ENDED(52, "Hết số lượt sử dụng thử", HttpStatus.FORBIDDEN),
    SUBSCRIPTION_EXPIRED(52, "Hết hạn gói thành viên", HttpStatus.FORBIDDEN),
    BOOKING_NUMBER_OF_TEAM_MEMBER(55, "Số thành viên không đủ", HttpStatus.FORBIDDEN),
    DANCER_CAN_NOT_BOOKING(56, "Dancer không thể đặt lịch", HttpStatus.FORBIDDEN),
    PAYMENT_NOT_PAY(57, "Họp đồng chưa thanh toán", HttpStatus.FORBIDDEN),
    PAYMENT_NOT_FOUND(58, "Không tìm thấy hóa đơn", HttpStatus.NOT_FOUND),
    BOOKING_NOT_PAY(55, "Hợp đồng chưa gửi biên lai", HttpStatus.FORBIDDEN),
    COMPLAIN_NOT_FOUND(59, "Không tìm thấy khiếu nại ", HttpStatus.NOT_FOUND),
    BOOKING_CAN_NOT_START(60, "Hợp đồng không thể bắt đầu", HttpStatus.FORBIDDEN),
    INVALID_DATE_RANGE(61, "Khoảng thời gian không hợp lệ", HttpStatus.BAD_REQUEST);

    private final Integer code;
    private final String message;
    private final HttpStatusCode httpStatusCode;
}
