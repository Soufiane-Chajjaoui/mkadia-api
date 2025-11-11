-- Insert sample data for categories and products

-- Insert categories
INSERT INTO categories (name, url) VALUES
('Fruits & Légumes', 'fruits-legumes'),
('Viandes & Poissons', 'viandes-poissons'),
('Produits Laitiers & Œufs', 'produits-laitiers-oeufs'),
('Pain, Céréales & Pâtisseries', 'pain-cereales-patisseries'),
('Boissons', 'boissons'),
('Épicerie Salée', 'epicerie-salee'),
('Snacks & Sucreries', 'snacks-sucreries'),
('Produits Surgelés', 'produits-surgeles'),
('Produits pour Bébé', 'produits-bebe'),
('Hygiène & Entretien', 'hygiene-entretien');

-- Supposons que les catégories ont déjà été insérées avec leurs IDs 1..10
-- Ici, on crée 50 produits actifs répartis sur 5 catégories

INSERT INTO products
(name, description, sku, barcode, brand, origin, price, discount_percentage, stock,
 status, is_new, is_featured, unit, quantity, expiration_date,
 slug, meta_title, meta_desc, category_id, created_at, updated_at)
VALUES
-- 🥦 Fruits & Légumes (cat_id = 1)
('Pommes Golden', 'Pommes croquantes du Maroc', 'FRU-POM-GOLD', '111111111111', 'AgriMaroc', 'Maroc', 2.99, NULL, 120, 'ACTIVE', true, false, 'KG', 1.0, '2025-09-01', 'pommes-golden', 'Pommes Golden Maroc', 'Pommes fraîches et sucrées', 1, NOW(), NOW()),
('Bananes', 'Bananes douces d’Amérique Latine', 'FRU-BAN-001', '111111111112', 'TropiFresh', 'Équateur', 3.49, 5.0, 200, 'ACTIVE', true, true, 'KG', 1.0, '2025-09-01', 'bananes', 'Bananes fraîches', 'Riches en potassium et énergie', 1, NOW(), NOW()),
('Fraises Gariguette', 'Fraises parfumées d’Espagne', 'FRU-FRA-GARI', '111111111113', 'BerryFresh', 'Espagne', 5.99, NULL, 80, 'ACTIVE', false, true, 'KG', 0.5, '2025-07-01', 'fraises-gariguette', 'Fraises Gariguette', 'Saveur sucrée et intense', 1, NOW(), NOW()),
('Raisin Blanc', 'Sans pépins, idéal pour dessert', 'FRU-RAI-BLC', '111111111114', 'VignobleSud', 'France', 3.75, NULL, 95, 'ACTIVE', false, false, 'KG', 1.0, '2025-08-15', 'raisin-blanc', 'Raisin blanc', 'Frais et sucré', 1, NOW(), NOW()),
('Avocats Hass', 'Avocats crémeux importés du Kenya', 'FRU-AVO-HAS', '111111111115', 'TropiFresh', 'Kenya', 4.99, 10.0, 75, 'ACTIVE', false, true, 'PCS', 2.0, '2025-10-01', 'avocats-hass', 'Avocats Hass', 'Texture onctueuse pour salades', 1, NOW(), NOW()),

-- 🍗 Viandes & Poissons (cat_id = 2)
('Poulet Fermier', 'Poulet frais origine locale', 'MEA-POU-FER', '222222222221', 'BeldiFarm', 'Maroc', 45.00, NULL, 60, 'ACTIVE', true, true, 'KG', 1.0, '2025-08-10', 'poulet-fermier', 'Poulet Fermier', 'Poulet tendre et savoureux', 2, NOW(), NOW()),
('Saumon Frais', 'Filet de saumon d’Écosse', 'FIS-SAU-FIL', '222222222222', 'NordSea', 'Écosse', 89.99, 15.0, 45, 'ACTIVE', false, false, 'KG', 0.5, '2025-08-20', 'saumon-frais', 'Saumon frais', 'Riche en oméga 3', 2, NOW(), NOW()),
('Steak de Bœuf', 'Viande rouge premium', 'MEA-STK-BEF', '222222222223', 'AtlasMeat', 'Maroc', 120.00, NULL, 30, 'ACTIVE', true, false, 'KG', 1.0, '2025-08-05', 'steak-boeuf', 'Steak Bœuf Premium', 'Viande tendre de qualité', 2, NOW(), NOW()),
('Crevettes Roses', 'Crevettes décortiquées', 'FIS-CRV-ROS', '222222222224', 'OceanMar', 'Maroc', 99.90, 5.0, 90, 'ACTIVE', false, true, 'KG', 1.0, '2025-09-30', 'crevettes-roses', 'Crevettes Roses', 'Prêtes à cuire', 2, NOW(), NOW()),
('Merguez Fraîches', 'Saucisses de bœuf épicées', 'MEA-MER-FRA', '222222222225', 'HalalDelight', 'Maroc', 55.00, NULL, 110, 'ACTIVE', true, false, 'KG', 1.0, '2025-08-12', 'merguez-fraiches', 'Merguez Fraîches', 'Recette artisanale', 2, NOW(), NOW()),

-- 🥛 Produits Laitiers & Œufs (cat_id = 3)
('Lait Demi-Écrémé', 'Lait stérilisé UHT 1L', 'DAI-LAI-UHT', '333333333331', 'Central Laitière', 'Maroc', 8.50, NULL, 200, 'ACTIVE', true, false, 'L', 1.0, '2025-11-01', 'lait-uht', 'Lait Demi-Écrémé', 'Source naturelle de calcium', 3, NOW(), NOW()),
('Yaourt Nature', 'Yaourt nature sans sucre', 'DAI-YAO-NAT', '333333333332', 'Danone', 'Maroc', 4.00, 5.0, 300, 'ACTIVE', false, false, 'PCS', 1.0, '2025-09-15', 'yaourt-nature', 'Yaourt Nature', 'Crémeux et léger', 3, NOW(), NOW()),
('Fromage Gouda', 'Fromage hollandais doux', 'DAI-FRO-GOU', '333333333333', 'HollandCheese', 'Pays-Bas', 39.90, 10.0, 80, 'ACTIVE', true, true, 'KG', 0.5, '2025-09-30', 'fromage-gouda', 'Fromage Gouda', 'Saveur douce et fondante', 3, NOW(), NOW()),
('Beurre Doux', 'Beurre pur lait de vache', 'DAI-BEU-DOU', '333333333334', 'Président', 'France', 25.00, NULL, 150, 'ACTIVE', false, false, 'PCS', 1.0, '2025-10-15', 'beurre-doux', 'Beurre Doux', 'Idéal pour tartines et pâtisseries', 3, NOW(), NOW()),
('Œufs Plein Air', 'Boîte de 6 œufs fermiers', 'DAI-OEU-PLA', '333333333335', 'FermeAtlas', 'Maroc', 12.00, NULL, 400, 'ACTIVE', false, true, 'PACK', 6.0, '2025-07-30', 'oeufs-plein-air', 'Œufs Plein Air', 'Œufs frais de poules élevées en plein air', 3, NOW(), NOW()),

-- 🥖 Pain, Céréales & Pâtisseries (cat_id = 4)
('Pain Complet', 'Pain complet artisanal', 'BAK-PAN-COM', '444444444441', 'Boulangerie Atlas', 'Maroc', 5.00, NULL, 150, 'ACTIVE', true, false, 'PCS', 1.0, '2025-06-01', 'pain-complet', 'Pain complet', 'Source de fibres naturelles', 4, NOW(), NOW()),
('Croissants au Beurre', 'Pur beurre croustillant', 'BAK-CRO-BEU', '444444444442', 'Boulangerie Atlas', 'Maroc', 9.00, 10.0, 100, 'ACTIVE', true, true, 'PACK', 4.0, '2025-06-15', 'croissants-beurre', 'Croissants au Beurre', 'Recette française authentique', 4, NOW(), NOW()),
('Céréales Miel & Avoine', 'Céréales riches en fibres', 'BAK-CER-MAV', '444444444443', 'NutriFit', 'Espagne', 25.00, 15.0, 80, 'ACTIVE', false, false, 'PACK', 0.5, '2026-02-01', 'cereales-miel-avoine', 'Céréales Miel et Avoine', 'Petit déjeuner nutritif', 4, NOW(), NOW()),
('Pain de Mie', 'Pain tranché moelleux', 'BAK-PAN-MIE', '444444444444', 'Boulangerie Atlas', 'Maroc', 7.50, NULL, 120, 'ACTIVE', false, false, 'PACK', 0.5, '2025-09-01', 'pain-de-mie', 'Pain de Mie', 'Idéal pour sandwichs', 4, NOW(), NOW()),
('Gâteaux au Chocolat', 'Gâteaux moelleux tout chocolat', 'BAK-GAT-CHO', '444444444445', 'SweetDelight', 'Maroc', 20.00, 10.0, 60, 'ACTIVE', false, true, 'PACK', 0.4, '2025-10-01', 'gateaux-chocolat', 'Gâteaux au Chocolat', 'Délicieux et fondants', 4, NOW(), NOW()),

-- 🧴 Hygiène & Entretien (cat_id = 10)
('Savon Liquide', 'Savon liquide main aloe vera', 'HYG-SAV-LIQ', '555555555551', 'CleanTouch', 'France', 15.00, NULL, 120, 'ACTIVE', false, false, 'PCS', 0.5, '2026-05-01', 'savon-liquide', 'Savon Liquide Aloe Vera', 'Nettoie et hydrate les mains', 10, NOW(), NOW()),
('Dentifrice Fraîcheur', 'Goût menthe, protection complète', 'HYG-DEN-FRA', '555555555552', 'SmilePro', 'Maroc', 12.00, 5.0, 140, 'ACTIVE', false, true, 'PCS', 0.1, '2026-07-01', 'dentifrice-fraicheur', 'Dentifrice Fraîcheur Menthe', 'Pour une haleine fraîche', 10, NOW(), NOW()),
('Lessive Liquide', 'Formule concentrée 3L', 'HYG-LES-LIQ', '555555555553', 'AquaClean', 'France', 49.90, 20.0, 70, 'ACTIVE', true, false, 'L', 3.0, '2027-01-01', 'lessive-liquide', 'Lessive Liquide Concentrée', 'Propreté impeccable et parfum frais', 10, NOW(), NOW()),
('Essuie-Tout', 'Rouleaux absorbants, pack de 6', 'HYG-ESS-TOU', '555555555554', 'SoftCare', 'Italie', 25.00, NULL, 90, 'ACTIVE', false, true, 'PACK', 6.0, '2026-03-01', 'essuie-tout', 'Essuie-Tout absorbant', 'Résistant et pratique', 10, NOW(), NOW()),
('Gel Douche', 'Gel douche hydratant coco', 'HYG-GEL-COC', '555555555555', 'BodyFresh', 'Maroc', 22.00, 10.0, 100, 'ACTIVE', true, true, 'PCS', 0.4, '2026-02-01', 'gel-douche', 'Gel Douche Coco', 'Hydratation et douceur tropicale', 10, NOW(), NOW());
