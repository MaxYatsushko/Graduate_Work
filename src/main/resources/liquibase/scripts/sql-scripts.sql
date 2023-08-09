-- liquibase formatted sql

-- changeset m.yatsushko:1
CREATE TABLE images
(
    id        SERIAL PRIMARY KEY,
    file_name VARCHAR(255),
    media_type VARCHAR(255)

);

CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    password   VARCHAR(255),
    phone      VARCHAR(255),
    reg_date   DATE,
    image_id   INTEGER,
    role       VARCHAR(255),

    FOREIGN KEY (image_id) REFERENCES images (id)
    ON DELETE CASCADE
);


CREATE TABLE ads
(
    id          SERIAL PRIMARY KEY,
    user_id     INTEGER NOT NULL,
    image_id    INTEGER,
    price       INT,
    title       VARCHAR(255),
    description VARCHAR(255),

    FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE,

    FOREIGN KEY (image_id) REFERENCES images (id)
    ON DELETE CASCADE
);


CREATE TABLE comments
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER NOT NULL,
    created_at BIGINT,
    text       TEXT,
    ad_id      INTEGER,
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE,

    FOREIGN KEY (ad_id) REFERENCES ads (id)
    ON DELETE CASCADE
);








