CREATE TABLE complain
(
    id                 INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    booking_id         INT NOT NULL,
    time               datetime,
    content            VARCHAR(255),
    user_id            INT NOT NULL,
    status_id          INT NOT NULL,
    previous_status_id INT NOT NULL,
    CONSTRAINT fk_booking FOREIGN KEY (booking_id) REFERENCES booking (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_status FOREIGN KEY (status_id) REFERENCES status (id),
    CONSTRAINT fk_previous_status FOREIGN KEY (previous_status_id) REFERENCES status (id)
);
