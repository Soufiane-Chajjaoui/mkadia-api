CREATE TABLE media (
    media_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    media_type type_of_media,
    url VARCHAR(255) NOT NULL
);

ALTER TABLE media
ADD product_id INT NOT NULL;

ALTER TABLE media
ADD CONSTRAINT fk_media_product
FOREIGN KEY (product_id) REFERENCES products(product_id);