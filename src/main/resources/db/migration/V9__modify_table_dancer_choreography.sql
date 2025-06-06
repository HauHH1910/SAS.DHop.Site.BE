ALTER TABLE dancer
    DROP FOREIGN KEY dancer_ibfk_1;
ALTER TABLE choreography
    DROP FOREIGN KEY choreography_ibfk_1;
ALTER TABLE dancer
    DROP COLUMN dance_type_id;
ALTER TABLE choreography
    DROP COLUMN dance_type_id;