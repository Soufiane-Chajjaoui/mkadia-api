CREATE TABLE deliveries
(
    delivery_id      INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    order_id         INT         NOT NULL,
    delivery_status  VARCHAR(50) NOT NULL,
    delivery_address INT         NOT NULL,
    delivery_mode VARCHAR(50) NOT NULL,
    assigned_to       INT,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_delivery_order
        FOREIGN KEY (order_id)
            REFERENCES orders (order_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_delivery_address
        FOREIGN KEY (delivery_address)
            REFERENCES addresses (address_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_delivery_user
        FOREIGN KEY (assigned_to)
            REFERENCES users (user_id)
            ON DELETE SET NULL
);
