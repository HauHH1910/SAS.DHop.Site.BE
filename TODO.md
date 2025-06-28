# TODO

---
Công việc cho nô lệ:

- Làm booking hoạt động được luồng
- Set được lịch cho 2 bên có thể thấy được
- Trong end booking có thể đi được luồng thanh toán và xác nhận thanh toán từ bên phía khách hàng lẫn bên được thuê.
- Xử lý được conflit lịch
- Xử lý xong phần dashboard

Đặt booking -> Điền booking detail, bao gồm thời gian booking kết thúc, thời gian diễm (ngày, giờ diễn giờ kết thúc,
tiền) + Booking form sẽ hiện tổng tiền bao gồm tiền trã cho dancer và tiền hoa hồng -> Thanh toán tiền hoa hồng trước để
đảm bảo giao dịch -> Thanh toán xong booking chuyển sang trạnh thái BOOKING_PENDING -> Dancers approve công việc,
booking chuyển sang trạng thái BOOKING_ACTIVATE -> Dancer nhấn nút start_work, booking chuyển sang trạng thái
IN_PROGRESS -> DANCER xong việc thì cung cấp hình ảnh về công việc của họ và nhấn nút end_work sau đó, booking sẽ chuyển
sang trạng thái WORK_DONE -> Khách hàng sẽ kiểm tra và sau đó thanh toán cho DANCERS bằng stk hoặc QR gì đó, sau đó gửi
ảnh chứng minh giao dịch cho bên phía DANCERS xác nhận -> DANCERS sau khi xác nhận thì sẽ nhấn nút hoàn thành, trạng
thái của booking sẽ chuyển thành BOOKING_COMPLETE.