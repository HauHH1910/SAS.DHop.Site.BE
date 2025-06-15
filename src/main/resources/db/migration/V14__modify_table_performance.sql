ALTER TABLE performance
    ADD COLUMN booking_id INT NULL,
    ADD CONSTRAINT fk_performance_booking
        FOREIGN KEY (booking_id) REFERENCES booking (id);