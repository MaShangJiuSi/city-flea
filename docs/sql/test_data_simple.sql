-- ========================================
-- City Flea Market - Test Data (English)
-- ========================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ========================================
-- Categories
-- ========================================
INSERT INTO category (type, name, sort, status, create_time) VALUES
(1, 'Electronics', 1, 1, NOW()),
(1, 'Books & Stationery', 2, 1, NOW()),
(1, 'Clothing & Shoes', 3, 1, NOW()),
(1, 'Home & Garden', 4, 1, NOW()),
(1, 'Sports & Outdoors', 5, 1, NOW()),
(1, 'Toys & Music', 6, 1, NOW()),
(1, 'Digital Accessories', 7, 1, NOW()),
(1, 'Other', 8, 1, NOW());

SET FOREIGN_KEY_CHECKS = 1;

-- ========================================
-- Verification
-- ========================================
SELECT 'Category Count' as 'Item', COUNT(*) as 'Count' FROM category;
