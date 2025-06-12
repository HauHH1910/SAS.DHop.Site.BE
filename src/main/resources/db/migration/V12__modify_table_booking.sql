ALTER TABLE booking
    MODIFY dancer_id INT NULL;
ALTER TABLE booking
    MODIFY performance_id INT NULL;
ALTER TABLE booking
    MODIFY choreography_id INT NULL;
ALTER TABLE booking
    MODIFY price DECIMAL NOT NULL;
ALTER TABLE booking
    MODIFY start_time DATETIME;
ALTER TABLE booking
    MODIFY end_time DATETIME;