ALTER TABLE booking
    DROP FOREIGN KEY `booking_ibfk_4`;

ALTER TABLE booking
    DROP COLUMN `dance_type_id`;

CREATE TABLE booking_dance_type
(
    booking_id    INT NOT NULL,
    dance_type_id INT NOT NULL,
    PRIMARY KEY (booking_id, dance_type_id),
    FOREIGN KEY (booking_id) REFERENCES booking (id) ON DELETE CASCADE,
    FOREIGN KEY (dance_type_id) REFERENCES dance_type (id) ON DELETE CASCADE
);
