CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(64) NOT NULL UNIQUE,
    password   VARCHAR(64) NOT NULL,
    role       VARCHAR(32) NOT NULL,
    first_name VARCHAR(64) NOT NULL,
    last_name  VARCHAR(64) NOT NULL,
    enabled    BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
CREATE TABLE hobbies
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(64) NOT NULL
);

CREATE TABLE user_hobbies
(
    id         SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users (id),
    hobby_id   INTEGER NOT NULL REFERENCES hobbies (id)
);

INSERT INTO users
    (email, password, role, first_name, last_name, enabled, created_at, updated_at)
VALUES ('sumkin.gpb@gmail.com',
        '$2a$12$zhIO9HXcdj2uBL.dEAI2te2c2TIAta7U0yLaySWFCTqlUMxFdhobW',
        'ADMIN',
        'Peter',
        'Sumkin',
        true,
        '2023-09-04T01:05:09.0358756',
        '2023-09-04T01:05:09.0358756'),

       ('player@yandex.ru',
        '$2a$12$MDUCZUfwPn3xEYi/SIFCt.HmjzuDt5Q7/EzNc2vhHkrVj4wwzLij.',
        'PLAYER',
        'Ivan',
        'Nikiforov',
        true,
        '2023-09-04T01:05:09.0358756',
        '2023-09-04T01:05:09.0358756'),

       ('parent@yandex.ru',
        '$2a$12$MyqFe.MxS/fwYdnxjmjyvuaM.hN6MpEDmERsnyUyODhEsEeDVE1Mm',
        'PARENT',
        'Pyotr',
        'Alekseev',
        true,
        '2023-09-04T01:05:09.0358756',
        '2023-09-04T01:05:09.0358756'),

       ('coach@yandex.ru',
        '$2a$12$ZlmCJKNk9iU.azqkfty4deWzK3lkvOvNzo84RcETmB0jXezSutVZG',
        'COACH',
        'Aleksey',
        'Sigizmundov',
        true,
        '2023-09-04T01:05:09.0358756',
        '2023-09-04T01:05:09.0358756'),

       ('doctor@yandex.ru',
        '$2a$12$CBBYa1ss/qFLo.OknSkQW.Cme/cmK3sCRxZ.akWbfd7UITD0gbdtO',
        'DOCTOR',
        'Sergey',
        'Ezhov',
        true,
        '2023-09-04T01:05:09.0358756',
        '2023-09-04T01:05:09.0358756')
;

INSERT INTO hobbies
    (name)
VALUES ('вязание'),
       ('плавание'),
       ('программирование'),
       ('футбол'),
       ('фотография')
;

INSERT INTO user_hobbies
    (user_id, hobby_id)
VALUES (1, 1),
       (1, 2),
       (1, 5),
       (2, 5),
       (3, 1),
       (3, 2),
       (3, 3),
       (3, 4),
       (3, 5),
       (4, 1),
       (4, 5),
       (5, 1),
       (5, 2)
;