-- 1. Création du type enum pour status
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'product_status') THEN
CREATE TYPE product_status AS ENUM ('ACTIVE', 'INACTIVE', 'DRAFT');
END IF;
END
$$;

-- 2. Ajout des colonnes à la table products

ALTER TABLE products
ADD COLUMN sku VARCHAR(100) NOT NULL DEFAULT '' ;

ALTER TABLE products
    ADD CONSTRAINT uq_products_sku UNIQUE (sku);

ALTER TABLE products
    ADD COLUMN barcode VARCHAR(100);

ALTER TABLE products
    ADD CONSTRAINT uq_products_barcode UNIQUE (barcode);

ALTER TABLE products
    ADD COLUMN brand VARCHAR(100);

ALTER TABLE products
    ADD COLUMN origin VARCHAR(100);

ALTER TABLE products
    ADD COLUMN discount_percentage DOUBLE PRECISION;

ALTER TABLE products
    ADD COLUMN status product_status NOT NULL DEFAULT 'DRAFT';

ALTER TABLE products
    ADD COLUMN is_featured BOOLEAN DEFAULT FALSE;

ALTER TABLE products
    ADD COLUMN is_new BOOLEAN DEFAULT FALSE;

ALTER TABLE products
    ADD COLUMN unit VARCHAR(10) NOT NULL DEFAULT 'PCS';

ALTER TABLE products
    ADD COLUMN quantity DOUBLE PRECISION NOT NULL DEFAULT 1.0;

ALTER TABLE products
    ADD COLUMN expiration_date DATE;

ALTER TABLE products
    ADD COLUMN slug VARCHAR(255);

ALTER TABLE products
    ADD COLUMN meta_title VARCHAR(255);

ALTER TABLE products
    ADD COLUMN meta_desc TEXT;

-- 3. Création des indexes pour optimiser les recherches

CREATE INDEX IF NOT EXISTS idx_products_status ON products(status);
CREATE INDEX IF NOT EXISTS idx_products_category_id ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_products_price ON products(price);
