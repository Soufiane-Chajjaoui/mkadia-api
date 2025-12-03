CREATE TABLE favorites
(
    favorite_id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id    BIGINT    NOT NULL,
    product_id BIGINT    NOT NULL,

    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,


    CONSTRAINT fk_favoris_user FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_favoris_product FOREIGN KEY (product_id)
        REFERENCES products (product_id) ON DELETE CASCADE
);
