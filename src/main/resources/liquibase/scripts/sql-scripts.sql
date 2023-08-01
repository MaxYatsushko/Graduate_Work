-- liquibase formatted sql

-- changeset m.yatsushko:1
CREATE TABLE images
(
    id         SERIAL PRIMARY KEY,
    data       BYTEA,
    file_path  VARCHAR(255),
    file_size  BIGINT NOT NULL,
    media_type VARCHAR(255),
    preview    BYTEA
);

CREATE TABLE users
(
    id        SERIAL PRIMARY KEY,
    email     VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    password  VARCHAR(255),
    phone     VARCHAR(255),
    reg_date   DATE,
    image_id  INTEGER,
    role      VARCHAR(255),
    FOREIGN KEY (image_id) REFERENCES images (id)
);

CREATE TABLE comments
(
    id        SERIAL PRIMARY KEY,
    user_id   INTEGER NOT NULL,
    created_at TIMESTAMP,
    text      TEXT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE ads
(
    id       SERIAL PRIMARY KEY,
    user_id  INTEGER NOT NULL,
    image_id INTEGER,
    price    INTEGER,
    title    VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (image_id) REFERENCES images (id)
);








