CREATE TABLE `status`
(
    `id`          INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `status_name` VARCHAR(100),
    `status_type` ENUM ('active', 'inactive', 'pending'),
    `description` TEXT,
    `created_at`  DATETIME,
    `updated_at`  DATETIME
);

CREATE TABLE `area`
(
    `id`         INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `district`   VARCHAR(100),
    `ward`       VARCHAR(100),
    `city`       VARCHAR(100),
    `created_at` DATETIME,
    `updated_at` DATETIME
);

CREATE TABLE `permission`
(
    `id`   INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100)
);

CREATE TABLE `role`
(
    `id`   INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100)
);

CREATE TABLE `users`
(
    `id`         INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `avatar`     VARCHAR(255),
    `age`        INT,
    `name`       VARCHAR(100),
    `email`      VARCHAR(150),
    `phone`      VARCHAR(20),
    `password`   VARCHAR(255),
    `created_at` DATETIME,
    `updated_at` DATETIME,
    `area_id`    INT             NOT NULL,
    `status_id`  INT             NOT NULL
);

CREATE TABLE `users_roles`
(
    `id`      INT PRIMARY KEY AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `role_id` INT NOT NULL
);

CREATE TABLE `roles_permissions`
(
    `id`            INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `role_id`       INT             NOT NULL,
    `permission_id` INT             NOT NULL
);

CREATE TABLE `performance`
(
    `id`          INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `user_id`     INT             NOT NULL,
    `media_url`   VARCHAR(255),
    `media_type`  ENUM ('video', 'image', 'audio'),
    `purpose`     ENUM ('training', 'showcase', 'competition'),
    `description` TEXT,
    `created_at`  DATETIME,
    `updated_at`  DATETIME,
    `status_id`   INT             NOT NULL
);

CREATE TABLE `dance_type`
(
    `id`          INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `type`        VARCHAR(100),
    `description` TEXT,
    `created_at`  DATETIME,
    `updated_at`  DATETIME
);

CREATE TABLE `subscription`
(
    `id`         INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(100),
    `duration`   INT,
    `content`    TEXT,
    `price`      DECIMAL(10, 2),
    `created_at` DATETIME,
    `updated_at` DATETIME,
    `status_id`  INT             NOT NULL
);

CREATE TABLE `dancer`
(
    `id`               INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `dance_type_id`    INT             NOT NULL,
    `dancer_nick_name` VARCHAR(100),
    `user_id`          INT             NOT NULL,
    `about`            TEXT,
    `year_experience`  INT,
    `team_size`        INT,
    `price`            DECIMAL(10, 2),
    `subscription_id`  INT             NOT NULL,
    `status_id`        INT             NOT NULL
);

CREATE TABLE `choreography`
(
    `id`              INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `dance_type_id`   INT             NOT NULL,
    `user_id`         INT             NOT NULL,
    `subscription_id` INT             NOT NULL,
    `about`           TEXT,
    `year_experience` INT,
    `price`           DECIMAL(10, 2),
    `status_id`       INT             NOT NULL
);

CREATE TABLE `booking`
(
    `id`                  INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `customer_id`         INT             NOT NULL,
    `dancer_id`           INT             NOT NULL,
    `choreography_id`     INT             NOT NULL,
    `dance_type_id`       INT             NOT NULL,
    `area_id`             INT             NOT NULL,
    `status_id`           INT             NOT NULL,
    `booking_date`        DATETIME,
    `start_time`          DATETIME,
    `end_time`            DATETIME,
    `address`             VARCHAR(255),
    `detail`              TEXT,
    `update_booking_date` DATETIME,
    `booking_status`      ENUM ('pending', 'confirmed', 'cancelled', 'completed'),
    `customer_phone`      VARCHAR(20),
    `performance_id`      INT             NOT NULL
);

CREATE TABLE `chat`
(
    `id`         INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `booking_id` INT             NOT NULL,
    `created_at` DATETIME,
    `updated_at` DATETIME,
    `status_id`  INT             NOT NULL
);

CREATE TABLE `booking_feedback`
(
    `id`           INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `from_user_id` INT             NOT NULL,
    `to_user_id`   INT             NOT NULL,
    `rating`       INT,
    `comment`      TEXT,
    `created_at`   DATETIME,
    `updated_at`   DATETIME,
    `status_id`    INT             NOT NULL,
    `booking_id`   INT             NOT NULL
);

CREATE TABLE `article`
(
    `id`          INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `title`       VARCHAR(255),
    `content`     TEXT,
    `author_name` VARCHAR(100),
    `thumbnail`   TEXT,
    `created_at`  DATETIME,
    `updated_at`  DATETIME,
    `status_id`   INT             NOT NULL
);

CREATE TABLE `user_subscription`
(
    `id`              INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `user_id`         INT             NOT NULL,
    `subscription_id` INT             NOT NULL,
    `from_date`       DATETIME,
    `to_date`         DATETIME,
    `status_id`       INT             NOT NULL
);

ALTER TABLE `users`
    ADD FOREIGN KEY (`area_id`) REFERENCES `area` (`id`);
ALTER TABLE `users`
    ADD FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);
ALTER TABLE `users_roles`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `users_roles`
    ADD FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
ALTER TABLE `roles_permissions`
    ADD FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
ALTER TABLE `roles_permissions`
    ADD FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`);
ALTER TABLE `performance`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `performance`
    ADD FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);
ALTER TABLE `subscription`
    ADD FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);
ALTER TABLE `dancer`
    ADD FOREIGN KEY (`dance_type_id`) REFERENCES `dance_type` (`id`);
ALTER TABLE `dancer`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `dancer`
    ADD FOREIGN KEY (`subscription_id`) REFERENCES `subscription` (`id`);
ALTER TABLE `dancer`
    ADD FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);
ALTER TABLE `choreography`
    ADD FOREIGN KEY (`dance_type_id`) REFERENCES `dance_type` (`id`);
ALTER TABLE `choreography`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `choreography`
    ADD FOREIGN KEY (`subscription_id`) REFERENCES `subscription` (`id`);
ALTER TABLE `choreography`
    ADD FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);
ALTER TABLE `booking`
    ADD FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`);
ALTER TABLE `booking`
    ADD FOREIGN KEY (`dancer_id`) REFERENCES `dancer` (`id`);
ALTER TABLE `booking`
    ADD FOREIGN KEY (`choreography_id`) REFERENCES `choreography` (`id`);
ALTER TABLE `booking`
    ADD FOREIGN KEY (`dance_type_id`) REFERENCES `dance_type` (`id`);
ALTER TABLE `booking`
    ADD FOREIGN KEY (`area_id`) REFERENCES `area` (`id`);
ALTER TABLE `booking`
    ADD FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);
ALTER TABLE `booking`
    ADD FOREIGN KEY (`performance_id`) REFERENCES `performance` (`id`);
ALTER TABLE `chat`
    ADD FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`);
ALTER TABLE `chat`
    ADD FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);
ALTER TABLE `booking_feedback`
    ADD FOREIGN KEY (`from_user_id`) REFERENCES `users` (`id`);
ALTER TABLE `booking_feedback`
    ADD FOREIGN KEY (`to_user_id`) REFERENCES `users` (`id`);
ALTER TABLE `booking_feedback`
    ADD FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);
ALTER TABLE `booking_feedback`
    ADD FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`);
ALTER TABLE `article`
    ADD FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);
ALTER TABLE `user_subscription`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `user_subscription`
    ADD FOREIGN KEY (`subscription_id`) REFERENCES `subscription` (`id`);
ALTER TABLE `user_subscription`
    ADD FOREIGN KEY (`status_id`) REFERENCES `status` (`id`);
