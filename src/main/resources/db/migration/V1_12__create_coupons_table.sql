CREATE TABLE coupons
(
    coupon_id           INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    code_coupon         VARCHAR(50) NOT NULL UNIQUE,
    discount_type       VARCHAR(50) NOT NULL,
    discount_value      NUMERIC(10, 2) NULL,
    min_order_amount    NUMERIC(10, 2),
    max_discount_amount NUMERIC(10, 2),
    start_date          DATE,
    end_date            DATE,
    usage_limit         INT,
    usage_count         INT       DEFAULT 0,
    active              BOOLEAN   DEFAULT TRUE,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



-- ⚠️ Supprimer les coupons existants si besoin
DELETE FROM coupons;

-- 🔸 Insérer 100 coupons de test
INSERT INTO coupons
(code_coupon, discount_type, discount_value, min_order_amount, max_discount_amount, start_date, end_date, usage_limit, usage_count, active, created_at, updated_at)
VALUES
-- 1 à 30 : PERCENTAGE
('PERCENT10', 'PERCENTAGE', 10.00, 100.00, 50.00, CURRENT_DATE, CURRENT_DATE + INTERVAL '30 days', 100, 0, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PERCENT15', 'PERCENTAGE', 15.00, 150.00, 60.00, CURRENT_DATE, CURRENT_DATE + INTERVAL '30 days', 80, 0, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PERCENT20', 'PERCENTAGE', 20.00, 200.00, 70.00, CURRENT_DATE, CURRENT_DATE + INTERVAL '30 days', 60, 0, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 4 à 33
('PERCENT25', 'PERCENTAGE', 25.00, 250.00, 80.00, CURRENT_DATE, CURRENT_DATE + INTERVAL '45 days', 50, 0, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PERCENT30', 'PERCENTAGE', 30.00, 300.00, 90.00, CURRENT_DATE, CURRENT_DATE + INTERVAL '45 days', 40, 0, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PERCENT35', 'PERCENTAGE', 35.00, 350.00, 100.00, CURRENT_DATE, CURRENT_DATE + INTERVAL '60 days', 30, 0, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 👉 Génération automatique pour le reste (4 → 100)
DO $$
DECLARE
i INT;
BEGIN
FOR i IN 4..100 LOOP
        INSERT INTO coupons (code_coupon, discount_type, discount_value, min_order_amount, max_discount_amount, start_date, end_date, usage_limit, usage_count, active, created_at, updated_at)
        VALUES (
            'COUPON_' || i,
            CASE
                WHEN i % 3 = 0 THEN 'PERCENTAGE'
                WHEN i % 3 = 1 THEN 'FIXED'
                ELSE 'FREE_DELIVERY'
            END,
            CASE
                WHEN i % 3 = 0 THEN (5 + (i % 20))       -- 5 à 25 %
                WHEN i % 3 = 1 THEN (10 + (i % 50))      -- 10 à 60 MAD
                ELSE NULL
            END,
            (50 + (i % 200)),                          -- min_order_amount entre 50 et 250
            (50 + (i % 100)),                          -- max_discount_amount entre 50 et 150
            CURRENT_DATE,
            CURRENT_DATE + (INTERVAL '30 days') + (i || ' days')::INTERVAL,
            (10 + (i % 100)),                          -- usage_limit
            0,
            TRUE,
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP
        );
END LOOP;
END $$;
