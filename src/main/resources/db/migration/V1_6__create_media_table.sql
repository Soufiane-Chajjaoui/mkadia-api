CREATE TABLE media (
    media_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    media_type type_of_media,
    url TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE media
ADD product_id BIGINT NOT NULL;

ALTER TABLE media
ADD CONSTRAINT fk_media_product
FOREIGN KEY (product_id) REFERENCES products(product_id);