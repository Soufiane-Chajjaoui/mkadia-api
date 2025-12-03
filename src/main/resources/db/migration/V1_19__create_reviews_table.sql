CREATE TABLE reviews
(
    id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id    BIGINT    NOT NULL,
    product_id BIGINT    NOT NULL,
    comment    TEXT,
    rating     INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL,
    CONSTRAINT fk_review_product FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE
);
