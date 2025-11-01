CREATE TABLE cart_items
(
    cart_item_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    cart_id      INT NOT NULL,
    product_id   INT NULL,
    quantity     INT NOT NULL CHECK (quantity > 0),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES carts (cart_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE,
    UNIQUE (cart_id, product_id) -- un produit unique par panier
);