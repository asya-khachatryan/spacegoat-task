CREATE TABLE users
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username   VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    city       VARCHAR(100),
    balance    NUMERIC(25, 5)
);

CREATE TABLE transactions
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sender_user_id   BIGINT         NOT NULL REFERENCES users (id),
    receiver_user_id BIGINT         NOT NULL REFERENCES users (id),
    amount           NUMERIC(25, 5) NOT NULL,
    creation_time    TIMESTAMP      NOT NULL DEFAULT now()
)