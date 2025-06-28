CREATE TABLE payment
(
    id         INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_code BIGINT,
    status     VARCHAR(50),
    amount     INT
);