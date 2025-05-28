CREATE TABLE choreography_dance_type
(
    choreography_id INT NOT NULL,
    dance_type_id   INT NOT NULL,
    PRIMARY KEY (choreography_id, dance_type_id),
    FOREIGN KEY (choreography_id) REFERENCES choreography (id) ON DELETE CASCADE,
    FOREIGN KEY (dance_type_id) REFERENCES dance_type (id) ON DELETE CASCADE
);

CREATE TABLE dancer_dance_type
(
    dancer_id INT NOT NULL,
    dance_type_id INT NOT NULL,
    PRIMARY KEY (dancer_id, dance_type_id),
    FOREIGN KEY (dancer_id) REFERENCES dancer(id) ON DELETE CASCADE ,
    FOREIGN KEY (dance_type_id) REFERENCES dance_type(id) ON DELETE CASCADE
);