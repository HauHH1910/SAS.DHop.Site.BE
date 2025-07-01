CREATE TABLE training_session
(
    id               INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,

    booking_id       INT      NOT NULL,
    choreography_id  INT      NOT NULL,

    session_no       INT         NOT NULL,                       -- Thứ tự buổi tập trong toàn bộ lịch trình
    scheduled_time   TIMESTAMP,                                  -- Lịch dự kiến (nullable nếu chưa có lịch)
    duration_minutes INT                  DEFAULT 60,

    status           VARCHAR(30) NOT NULL DEFAULT 'NOT_STARTED', -- Enum tạm thời dưới dạng chuỗi

    note             TEXT,                                       -- Ghi chú buổi tập
    feedback         TEXT,                                       -- Đánh giá từ khách hàng hoặc biên đạo

    created_at       TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_training_session_booking FOREIGN KEY (booking_id)
        REFERENCES booking (id) ON DELETE CASCADE,

    CONSTRAINT fk_training_session_choreography FOREIGN KEY (choreography_id)
        REFERENCES choreography (id) ON DELETE CASCADE,

    CONSTRAINT uq_booking_session UNIQUE (booking_id, session_no)
);
