CREATE TABLE products
(
    product_id          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name                VARCHAR(255)     NOT NULL,
    description         TEXT             NOT NULL,
    price               NUMERIC(10, 2)   NOT NULL,
    stock               INT              NOT NULL,
    category_id         BIGINT              NOT NULL,
    sku                 VARCHAR(100)     NOT NULL UNIQUE,          -- ✅ SKU unique
    barcode             VARCHAR(100) UNIQUE,                       -- ✅ Barcode unique optionnel
    brand               VARCHAR(100),
    origin              VARCHAR(100),
    discount_percentage DOUBLE PRECISION,
    status              VARCHAR(20)   NOT NULL DEFAULT 'DRAFT', -- Enum
    is_featured         BOOLEAN                   DEFAULT FALSE,
    is_new              BOOLEAN                   DEFAULT FALSE,
    unit                VARCHAR(10)      NOT NULL DEFAULT 'PCS',
    quantity            DOUBLE PRECISION NOT NULL DEFAULT 1.0,
    expiration_date     DATE,
    slug                VARCHAR(255),
    meta_title          VARCHAR(255),
    meta_desc           TEXT,
    created_at          TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES categories (category_id)
);

-- 📝 Index pour optimiser les recherches fréquentes
CREATE INDEX IF NOT EXISTS idx_products_status ON products(status);
CREATE INDEX IF NOT EXISTS idx_products_category_id ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_products_price ON products(price);
CREATE INDEX IF NOT EXISTS idx_products_brand ON products(brand);
CREATE INDEX IF NOT EXISTS idx_products_slug ON products(slug);
CREATE INDEX IF NOT EXISTS idx_products_is_featured ON products(is_featured);
CREATE INDEX IF NOT EXISTS idx_products_is_new ON products(is_new);
