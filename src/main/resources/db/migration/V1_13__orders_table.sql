CREATE TABLE orders
(
    order_id     BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id      BIGINT NOT NULL,
    coupon_id    BIGINT ,
    sub_total NUMERIC(10, 2) NOT NULL CHECK (sub_total >= 0),
    total_amount NUMERIC(10, 2) NOT NULL CHECK (total_amount >= 0),
    discount_amount NUMERIC(10, 2) NOT NULL CHECK (discount_amount >= 0),
    status       VARCHAR(50)    NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_user
        FOREIGN KEY (user_id) REFERENCES users (user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_order_coupon
        FOREIGN KEY (coupon_id) REFERENCES coupons (coupon_id)
            ON DELETE CASCADE
);
