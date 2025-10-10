CREATE TABLE coupons
(
    id                  INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    code                VARCHAR(50)        NOT NULL UNIQUE,
    discount_type       VARCHAR(50)        NOT NULL,
    discount_value      NUMERIC(10, 2)     NULL,
    min_order_amount    NUMERIC(10, 2),
    max_discount_amount NUMERIC(10, 2),
    start_date          DATE,
    end_date            DATE,
    usage_limit         INT,
    usage_count         INT     DEFAULT 0,
    active              BOOLEAN DEFAULT TRUE
);
