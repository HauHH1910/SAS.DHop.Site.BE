-- Xóa khóa ngoại booking_ibfk_7 nếu tồn tại
ALTER TABLE `booking`
    DROP FOREIGN KEY `booking_ibfk_7`;

-- Xóa cột performance_id
ALTER TABLE `booking`
    DROP COLUMN `performance_id`;