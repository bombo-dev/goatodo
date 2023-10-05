DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `todos`;
DROP TABLE IF EXISTS `levels`;
DROP TABLE IF EXISTS `tags`;
DROP TABLE IF EXISTS `affiliations`;
DROP TABLE IF EXISTS `occupations`;
DROP TABLE IF EXISTS `user_affiliations`;
DROP TABLE IF EXISTS `affiliation_types`;
DROP TABLE IF EXISTS `slack_infos`;
DROP TABLE IF EXISTS `retrospects`;

CREATE TABLE `users`
(
    `id`             BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `level_id`       BIGINT      NOT NULL,
    `occupations_id` BIGINT      NOT NULL,
    `slack_info_id`  BIGINT      NULL,
    `email`          VARCHAR(50) NOT NULL,
    `password`       VARCHAR(50) NOT NULL,
    `nickname`       VARCHAR(15) NOT NULL,
    `role`           VARCHAR(10) NOT NULL,
    `experience`     INT         NOT NULL DEFAULT 0,
    `created_at`     DATETIME    NOT NULL,
    `updated_at`     DATETIME    NOT NULL,
    CONSTRAINT UK_EMAIL UNIQUE (email),
    CONSTRAINT UK_NICKNAME UNIQUE (nickname)
);

CREATE TABLE `todos`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`     BIGINT       NOT NULL,
    `tag_id`      BIGINT       NOT NULL,
    `title`       VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL,
    `difficulty`  VARCHAR(20)  NOT NULL,
    `created_at`  DATETIME     NOT NULL,
    `updated_at`  DATETIME     NOT NULL
);

CREATE TABLE `levels`
(
    `id`                  BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `level`               INT      NOT NULL,
    `required_experience` INT      NOT NULL,
    `created_at`          DATETIME NOT NULL,
    `updated_at`          DATETIME NOT NULL
);

CREATE TABLE `tags`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`    BIGINT      NOT NULL,
    `name`       VARCHAR(15) NOT NULL,
    `created_at` DATETIME    NOT NULL,
    `updated_at` DATETIME    NOT NULL,
    CONSTRAINT UK_USER_AND_NAME unique (user_id, name)
);

CREATE TABLE `affiliations`
(
    `id`                  BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `occupation_id`       BIGINT      NOT NULL,
    `affiliation_type_id` BIGINT      NOT NULL,
    `name`                VARCHAR(30) NOT NULL,
    `year`                INT         NULL,
    `created_at`          DATETIME    NOT NULL,
    `updated_at`          DATETIME    NOT NULL
);

CREATE TABLE `occupations`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(20) NOT NULL,
    `created_at` DATETIME    NOT NULL,
    `updated_at` DATETIME    NOT NULL,
    CONSTRAINT UK_NAME unique (name)
);

CREATE TABLE `user_affiliations`
(
    `id`             BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`        BIGINT   NOT NULL,
    `affiliation_id` BIGINT   NOT NULL,
    `created_at`     DATETIME NOT NULL,
    `updated_at`     DATETIME NOT NULL
);

CREATE TABLE `affiliation_types`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `type`       VARCHAR(20) NOT NULL,
    `created_at` DATETIME    NOT NULL,
    `updated_at` DATETIME    NOT NULL
);

CREATE TABLE `slack_infos`
(
    `id`          BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `slack_email` BIGINT   NOT NULL,
    `start_time`  TIME     NOT NULL DEFAULT '09:00:00',
    `end_time`    TIME     NOT NULL DEFAULT '21:00:00',
    `is_active`   BOOLEAN  NOT NULL DEFAULT 0,
    `created_at`  DATETIME NOT NULL,
    `updated_at`  DATETIME NOT NULL,
    CONSTRAINT UK_SLACK_EMAIL unique (slack_email)
);

CREATE TABLE `retrospects`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`    BIGINT      NOT NULL,
    `title`      VARCHAR(35) NOT NULL,
    `content`    TEXT        NOT NULL,
    `write_date` DATE        NOT NULL,
    `created_at` DATETIME    NOT NULL,
    `updated_at` DATETIME    NOT NULL,
    CONSTRAINT UK_USER_AND_WRITE_DATE unique (write_date)
);

