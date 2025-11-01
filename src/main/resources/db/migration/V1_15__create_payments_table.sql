CREATE TABLE payments
(
    payment_id     INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    order_id       INT            NOT NULL,
    amount         NUMERIC(10, 2) NOT NULL CHECK (amount >= 0),
    payment_method VARCHAR(50)    NOT NULL,
    payment_status VARCHAR(50)    NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_order
        FOREIGN KEY (order_id) REFERENCES orders (order_id)
            ON DELETE CASCADE
);
