-- ============================================
-- 🔗 TABLE MANY-TO-MANY : PRODUITS ASSOCIÉS
-- ============================================
CREATE TABLE IF NOT EXISTS product_related_products (
                                                        product_id INT NOT NULL,
                                                        related_id INT NOT NULL,

                                                        PRIMARY KEY (product_id, related_id),

    CONSTRAINT fk_related_product
    FOREIGN KEY (product_id)
    REFERENCES products(product_id)
    ON DELETE CASCADE,

    CONSTRAINT fk_related_related
    FOREIGN KEY (related_id)
    REFERENCES products(product_id)
    ON DELETE CASCADE
    );

-- Index pour optimiser les requêtes
CREATE INDEX IF NOT EXISTS idx_related_product_id
    ON product_related_products(product_id);

CREATE INDEX IF NOT EXISTS idx_related_related_id
    ON product_related_products(related_id);
