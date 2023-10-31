CREATE TABLE users
(
    ID         serial PRIMARY KEY NOT NULL,
    username   VARCHAR(50)        NOT NULL UNIQUE,
    first_name VARCHAR(50)        NOT NULL,
    last_name  VARCHAR(50)        NOT NULL,
    city       VARCHAR(100),
    balance    NUMERIC(25, 5)
);

CREATE TABLE transactions
(
    ID               serial PRIMARY KEY NOT NULL,
    sender_user_id   INT                NOT NULL REFERENCES users (ID),
    receiver_user_id INT                NOT NULL REFERENCES users (ID),
    amount           NUMERIC(25, 5)     NOT NULL,
    timestamp        TIMESTAMP
)