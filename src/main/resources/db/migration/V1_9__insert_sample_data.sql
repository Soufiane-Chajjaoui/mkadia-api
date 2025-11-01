-- Insert sample data for categories and products

-- Insert categories
INSERT INTO categories (name, url) VALUES
('Electronics', 'electronics'),
('Clothing', 'clothing'),
('Home & Kitchen', 'home-kitchen'),
('Beauty & Personal Care', 'beauty-personal-care'),
('Food & Beverages', 'food-beverages');

-- Insert products
-- Produits pour Fruits (Catégorie 1)
INSERT INTO products (name, description, price, stock, category_id, sku, barcode, brand, origin, discount_percentage, status, is_featured, is_new, unit, quantity, expiration_date, slug, meta_title, meta_desc) VALUES
('Pommes Golden', 'Pommes sucrées et croquantes, origine France', 2.99, 150, 1, 'FRUIT-POM-GOLD', '200000000001', 'Ferme du Val', 'France', NULL, 'ACTIVE', false, false, 'KG', 1.0, '2025-09-30', 'pommes-golden', 'Pommes Golden - Fraîches et croquantes', 'Dégustez nos pommes Golden directement de nos vergers français'),
('Bananes Bio', 'Bananes équitables certifiées agriculture biologique', 3.49, 200, 1, 'FRUIT-BAN-BIO', '200000000002', 'BioTrop', 'Côte d''Ivoire', 10.0, 'ACTIVE', true, false, 'KG', 1.0, '2025-08-20', 'bananes-bio', 'Bananes Bio - Équitable et durable', 'Bananes cultivées sans pesticides pour votre santé'),
('Fraises Gariguette', 'Fraises parfumées de saison, cueillies à maturité', 5.99, 80, 1, 'FRUIT-FRA-GARI', '200000000003', 'BerryFresh', 'Espagne', NULL, 'ACTIVE', false, true, 'KG', 0.5, '2025-08-15', 'fraises-gariguette', 'Fraises Gariguette - Le goût de l''été', 'Fraises juteuses avec leur parfum caractéristique'),
('Mangues Kent', 'Mangues crémeuses et sucrées, sans fibres', 4.25, 60, 1, 'FRUIT-MAN-KENT', '200000000004', 'TropicalFruit', 'Brésil', 15.0, 'ACTIVE', true, true, 'PCS', 1.0, '2025-09-10', 'mangues-kent', 'Mangues Kent - Douceur exotique', 'La variété de mangue la plus appréciée pour sa chair tendre'),
('Raisin Blanc', 'Raisin blanc sans pépin, grappes généreuses', 3.75, 90, 1, 'FRUIT-RAI-BLANC', '200000000005', 'VignobleSud', 'France', NULL, 'ACTIVE', false, false, 'KG', 1.0, '2025-08-25', 'raisin-blanc', 'Raisin blanc sans pépin - Frais et sucré', 'Idéal pour le snacking ou vos recettes'),
-- Produits pour Electronics (Catégorie 2)
('Smartphone X10 Pro', 'Écran 6.7" AMOLED, triple caméra 108MP', 999.99, 30, 2, 'ELEC-PH-X10P', '300000000001', 'TechPlus', 'Chine', NULL, 'ACTIVE', true, true, 'PCS', 1.0, NULL, 'smartphone-x10-pro', 'Smartphone X10 Pro - Performance ultime', 'Le flagship avec processeur dernier cri et autonomie record'),
('Casque Bluetooth NC', 'Réduction de bruit active, autonomie 30h', 179.99, 45, 2, 'ELEC-HS-NC50', '300000000002', 'SoundPro', 'Vietnam', 20.0, 'ACTIVE', true, false, 'PCS', 1.0, NULL, 'casque-bluetooth-nc', 'Casque sans fil avec réduction de bruit', 'Profitez de votre musique sans perturbations extérieures'),
('Tablette 10" 128GB', 'Tablette Android avec stylet inclus', 349.99, 25, 2, 'ELEC-TB-10A', '300000000003', 'TabTech', 'Corée du Sud', 15.0, 'ACTIVE', false, false, 'PCS', 1.0, NULL, 'tablette-10-128gb', 'Tablette polyvalente 10 pouces', 'Idéale pour le travail et les loisirs'),
('Montre Connectée', 'Suivi santé 24/7, étanche 5ATM', 129.99, 60, 2, 'ELEC-WS-SMART', '300000000004', 'WearTech', 'Taiwan', NULL, 'ACTIVE', false, true, 'PCS', 1.0, NULL, 'montre-connectee', 'Montre intelligente complète', 'Mesurez vos activités et votre sommeil avec précision'),
('Enceinte Portable', 'Son 360°, résistante à l''eau IPX7', 89.99, 40, 2, 'ELEC-SP-BT05', '300000000005', 'AudioMove', 'Chine', 10.0, 'ACTIVE', false, false, 'PCS', 1.0, NULL, 'enceinte-portable', 'Enceinte Bluetooth nomade', 'Emportez votre musique partout avec vous'),
-- Produits pour Clothing (Catégorie 3)
('Jeans Slim Noir', 'Jean slim stretch confortable, coupe moderne', 59.99, 100, 3, 'CLOTH-JN-SLIM', '400000000001', 'DenimCo', 'Bangladesh', 30.0, 'ACTIVE', true, false, 'PCS', 1.0, NULL, 'jeans-slim-noir', 'Jeans Slim Noir - Élégance urbaine', 'Le basique intemporel pour votre garde-robe'),
('T-Shirt Col V', '100% coton bio, coupe ajustée', 24.99, 150, 3, 'CLOTH-TS-VNECK', '400000000002', 'CottonPure', 'Portugal', NULL, 'ACTIVE', false, true, 'PCS', 1.0, NULL, 't-shirt-col-v', 'T-Shirt bio confortable', 'Respirable et doux contre la peau'),
('Veste en Cuir', 'Cuir véritable, doublure en soie', 299.99, 15, 3, 'CLOTH-JK-LEAT', '400000000003', 'LeatherLux', 'Italie', NULL, 'ACTIVE', true, true, 'PCS', 1.0, NULL, 'veste-en-cuir', 'Veste en cuir premium - Fabriquée en Italie', 'Pièce maîtresse intemporelle de qualité artisanale'),
('Robe d''Été', 'Imprimé floral, tissu léger et fluide', 45.99, 60, 3, 'CLOTH-DR-SUMM', '400000000004', 'SunnyStyle', 'Maroc', 15.0, 'ACTIVE', false, false, 'PCS', 1.0, NULL, 'robe-ete', 'Robe d''été légère et confortable', 'Parfaite pour les journées ensoleillées'),
('Chaussures de Sport', 'Confort optimal, semelle amortissante', 79.99, 80, 3, 'CLOTH-SH-SPORT', '400000000005', 'StepRight', 'Vietnam', 25.0, 'ACTIVE', true, false, 'PCS', 1.0, NULL, 'chaussures-sport', 'Chaussures de sport performantes', 'Support et confort pour vos entraînements'),

-- Produits pour Home & Kitchen (Catégorie 4)
('Machine à Café', 'Broyeur intégré, 15 bars de pression', 249.99, 20, 4, 'HOME-CF-ESPR', '500000000001', 'BrewMaster', 'Allemagne', NULL, 'ACTIVE', true, false, 'PCS', 1.0, NULL, 'machine-cafe', 'Machine à café professionnelle', 'Savourez un café barista chez vous'),
('Set de Casseroles', '6 pièces en acier inoxydable 18/10', 129.99, 35, 4, 'HOME-PN-SET6', '500000000002', 'CookPro', 'France', 20.0, 'ACTIVE', false, true, 'PACK', 1.0, NULL, 'set-casseroles', 'Set de cuisine complet', 'Qualité professionnelle pour vos préparations'),
('Robot Mixeur', '1000W, 5 vitesses, fonction pulse', 89.99, 40, 4, 'HOME-BL-PRO', '500000000003', 'BlendTech', 'Chine', 15.0, 'ACTIVE', false, false, 'PCS', 1.0, NULL, 'robot-mixeur', 'Robot mixeur multifonction', 'Préparations variées en quelques secondes'),
('Serviettes de Bain', '100% coton égyptien, 600g/m²', 29.99, 100, 4, 'HOME-TW-SET4', '500000000004', 'CottonLux', 'Egypte', NULL, 'ACTIVE', false, false, 'PACK', 4.0, NULL, 'serviettes-bain', 'Serviettes de bain haut de gamme', 'Doux et absorbant pour un confort optimal'),
('Wok Antiadhésif', 'Diamètre 34cm, revêtement céramique', 49.99, 30, 4, 'HOME-WK-CERA', '500000000005', 'WokMaster', 'Thaïlande', 10.0, 'ACTIVE', true, true, 'PCS', 1.0, NULL, 'wok-antiadhesif', 'Wok professionnel antiadhésif', 'Cuisson saine et uniforme'),

-- Produits pour Beauty & Personal Care (Catégorie 5)
('Crème Visage Bio', 'Hydratation intense, certifiée Ecocert', 34.99, 80, 5, 'BEAU-CR-BIO', '600000000001', 'BioCare', 'France', NULL, 'ACTIVE', true, false, 'PCS', 1.0, '2026-06-30', 'creme-visage-bio', 'Crème hydratante bio', 'Soin naturel pour une peau nourrie et protégée'),
('Shampoing Réparateur', 'Aux kératines et huile d''argan', 14.99, 120, 5, 'BEAU-SH-REPA', '600000000002', 'HairLux', 'Espagne', 15.0, 'ACTIVE', false, true, 'PCS', 1.0, '2026-03-31', 'shampoing-reparateur', 'Shampoing réparateur intensif', 'Redonne force et brillance à vos cheveux'),
('Parfum Signature', 'Eau de parfum 100ml, notes boisées', 79.99, 25, 5, 'BEAU-PF-SIGN', '600000000003', 'ScentMaster', 'France', NULL, 'ACTIVE', true, true, 'PCS', 1.0, '2027-12-31', 'parfum-signature', 'Parfum signature élégant', 'Un parfum unique qui vous caractérise'),
('Masque Argile', 'Purifiant et matifiant, peau grasse', 12.99, 90, 5, 'BEAU-MS-ARG', '600000000004', 'ClayPure', 'Italie', 20.0, 'ACTIVE', false, false, 'PCS', 1.0, '2025-12-31', 'masque-argile', 'Masque à l''argile purifiant', 'Élimine les impuretés et resserre les pores'),
('Brosse à Dents Éco', 'Bambou naturel, poils en nylon recyclé', 4.99, 200, 5, 'BEAU-BD-ECO', '600000000005', 'EcoSmile', 'Allemagne', NULL, 'ACTIVE', false, false, 'PCS', 1.0, NULL, 'brosse-dents-eco', 'Brosse à dents écologique', 'Alternative durable pour votre hygiène buccale'),

-- Produits pour Food & Beverages (Catégorie 6)
('Café Grain Arabica', '100% Arabica, torréfaction moyenne', 8.99, 50, 5, 'FOOD-CF-ARAB', '700000000001', 'CoffeeMasters', 'Colombie', 10.0, 'ACTIVE', true, false, 'PACK', 0.5, '2026-01-31', 'cafe-arabica', 'Café grain Arabica premium', 'Arômes complexes et équilibrés pour les connaisseurs'),
('Miel de Lavande', 'Miel cru non pasteurisé, origine Provence', 12.99, 40, 5, 'FOOD-MI-LAV', '700000000003', 'MielFrance', 'France', NULL, 'ACTIVE', false, false, 'G', 500.0, '2026-06-30', 'miel-lavande', 'Miel de lavande artisanal', 'Saveurs florales et propriétés apaisantes'),
('Pâtes Artisanales', 'Semoule de blé dur, séchage lent', 5.99, 80, 5, 'FOOD-PA-ART', '700000000004', 'PastaMamma', 'Italie', 15.0, 'ACTIVE', false, true, 'PACK', 0.5, '2026-03-31', 'pates-artisanales', 'Pâtes artisanales italiennes', 'Texture ferme et goût authentique'),
('Huile d''Olive Vierge', 'Extra vierge, première pression à froid', 18.99, 60, 5, 'FOOD-OO-EVOO', '700000000005', 'OlioPuro', 'Grèce', NULL, 'ACTIVE', true, false, 'L', 0.75, '2026-01-31', 'huile-olive-extra', 'Huile d''olive extra vierge premium', 'Fruitée et légèrement poivrée, idéale pour vos plats');
