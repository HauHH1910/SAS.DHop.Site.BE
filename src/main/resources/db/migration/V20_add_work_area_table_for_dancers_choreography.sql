CREATE TABLE choreography_work_area_list
(
    choreography_id INT NOT NULL,
    area_id   INT NOT NULL,
    PRIMARY KEY (choreography_id, area_id),
    FOREIGN KEY (choreography_id) REFERENCES choreography (id) ON DELETE CASCADE,
    FOREIGN KEY (area_id) REFERENCES area (id) ON DELETE CASCADE
);

CREATE TABLE dancers_work_area_list
(
    dancer_id INT NOT NULL,
    area_id   INT NOT NULL,
    PRIMARY KEY (dancer_id, area_id),
    FOREIGN KEY (dancer_id) REFERENCES dancer (id) ON DELETE CASCADE,
    FOREIGN KEY (area_id) REFERENCES area (id) ON DELETE CASCADE
);