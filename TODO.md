# TODO

---

- [x] Firebase
- [x] Video
- [ ] Notification
- [x] Bank PayOS
- [x] Thêm field cancel reason vào booking
- [x] Chọn nhiều thể loại dance type
- [x] Hàm endwork cần ảnh, optional
- [ ] thanh toán subscription
- [x] Chọn nhiều thể loại dance type
- [x] Hàm endwork cần ảnh, optional

Subscription membership liên quan đến dancers và choreographer:
<br/>
Sẽ có gói free dành cho người dùng sau khi tạo chỉ có 3 lần được add booking của khách hàng.
<br/>
Gói nâng cấp 1 dành cho các user có được 10 lần accept booking trong 1 tháng. ( mở khóa được các tính năng liên quan như
xem các số liệu thống kê và tính năng được nhận booking khẩn cấp đến từ khách hàng.) (250.000 VNĐ)
<br/>
Gói nâng cấp 2 dành cho các user có được 20 lần accept booking trong 3 tháng. ( mở khóa được các tính năng liên quan như
xem các số liệu thống kê và tính năng được nhận booking khẩn cấp đến từ khách hàng.) (550.000VNĐ)
<br/>
Gói nâng cấp 3 dành cho các user có được vô hạn lần accept booking trong 1 năm. ( mở khóa được các tính năng liên quan
như xem các số liệu thống kê và tính năng được nhận booking khẩn cấp đến từ khách hàng.) (1.750.000VNĐ)



Activate: Trạng thái khi người dùng đã kích hoạt gói và đã và đang sử dụng gói này.
Expired: Trạng thái khi người dùng đã sử dụng hết hạn gói và cần gia hạn thêm để sử dụng
Free_Trial: Trạng thái này là trạng thái khi lần đầu tiên khi người dùng kích hoạt tài khoản và sử dụng chúng, hoặc là được khuyến mãi trong những dịp event đặc biệt.
Pending: Trạng thái này là trạng thái khi người dùng thực hiện thanh toán nhưng mà có vấn đề về thanh toán hoặc những vấn đề gây ra khiến hệ thống bị trì trệ về việc giao dịch khiến cho hệ thống vẫn chưa biết là gói đó đã được thanh toán hay chưa.
Renewing: Trạng thái này chỉ xuất hiện khi hệ thống có chức năng tự động thanh toán định kỳ cho các tài khoản và khi có status này nghĩa là người dùng sẽ được tự động gia hạn khi tới hạn thanh toán. 
