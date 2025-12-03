CREATE TABLE addresses
(
    address_id    BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id       BIGINT NOT NULL,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city          VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    code_postal   INTEGER NOT NULL,
    label         VARCHAR(255),
    is_default    BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_address_user
        FOREIGN KEY (user_id) REFERENCES users (user_id)
            ON DELETE CASCADE
);
