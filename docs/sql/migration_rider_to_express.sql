-- ============================================
-- city-flea 骑手配送改邮政寄送模式 - 数据库改造脚本
-- 执行前请备份数据库！
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 改造说明
-- 1. 修改 orders 表：删除骑手字段，新增快递字段
-- 2. 修改 order_delivery_track 表：适配快递物流轨迹
-- 3. 修改 goods 表：新增重量字段用于运费计算
-- 4. 新增 freight_config 表：区域运费配置
-- 5. 新增 express_company 表：快递公司配置
-- ----------------------------

-- ----------------------------
-- Step 1: 修改 orders 表结构
-- 删除：rider_id, take_time, delivery_complete_time 等骑手相关字段
-- 新增：express_company, tracking_number, ship_time 等快递相关字段
-- 注意：delivery_fee 字段保留作为运费
-- ----------------------------
ALTER TABLE `orders`
  DROP COLUMN IF EXISTS `rider_id`,
  DROP COLUMN IF EXISTS `take_time`,
  DROP COLUMN IF EXISTS `delivery_complete_time`,
  ADD COLUMN IF NOT EXISTS `express_company` VARCHAR(32) DEFAULT NULL COMMENT '快递公司编码' AFTER `buyer_id`,
  ADD COLUMN IF NOT EXISTS `tracking_number` VARCHAR(64) DEFAULT NULL COMMENT '运单号' AFTER `express_company`,
  ADD COLUMN IF NOT EXISTS `ship_time` DATETIME DEFAULT NULL COMMENT '发货时间' AFTER `pay_time`,
  MODIFY COLUMN `order_status` tinyint(0) DEFAULT NULL COMMENT '订单状态: 1待支付 2待发货 3已发货 4物流运输中 5待收货 6已完成 7已取消 8退款中 9已退款',
  COMMENT = '订单表（已改造为快递配送模式）';

-- ----------------------------
-- Step 2: 修改 order_delivery_track 表结构
-- 删除：rider_id 字段
-- 修改：track_status 重新定义（0系统事件 1快递轨迹）
-- 新增：express_status, location 字段
-- ----------------------------
ALTER TABLE `order_delivery_track`
  DROP COLUMN IF EXISTS `rider_id`,
  DROP COLUMN IF EXISTS `lng`,
  DROP COLUMN IF EXISTS `lat`,
  MODIFY COLUMN `track_status` tinyint(0) NOT NULL COMMENT '轨迹类型: 0系统事件 1快递物流轨迹',
  ADD COLUMN IF NOT EXISTS `express_status` VARCHAR(32) DEFAULT NULL COMMENT '快递状态编码' AFTER `track_desc`,
  ADD COLUMN IF NOT EXISTS `location` VARCHAR(255) DEFAULT NULL COMMENT '物流发生地点' AFTER `express_status`,
  COMMENT = '订单物流轨迹表（已改造为快递物流模式）';

-- ----------------------------
-- Step 3: 修改 goods 表，新增 weight 字段
-- 用于按重量计算运费
-- ----------------------------
ALTER TABLE `goods`
  ADD COLUMN IF NOT EXISTS `weight` INT DEFAULT 500 COMMENT '商品重量(克)，默认500克' AFTER `condition`,
  MODIFY COLUMN `condition` VARCHAR(20) DEFAULT NULL COMMENT '物品成色，如：全新、9成新、8成新等',
  COMMENT = '二手商品表（新增重量字段用于运费计算）';

-- ----------------------------
-- Step 4: 新增 freight_config 表 - 区域运费配置表
-- ----------------------------
DROP TABLE IF EXISTS `freight_config`;
CREATE TABLE `freight_config` (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `province_code` varchar(16) DEFAULT NULL COMMENT '省份编码',
  `province_name` varchar(32) NOT NULL COMMENT '省份名称',
  `first_fee` decimal(10,2) NOT NULL DEFAULT 8.00 COMMENT '首重价格(元)',
  `continued_fee` decimal(10,2) NOT NULL DEFAULT 3.00 COMMENT '续重价格(元/500g)',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用: 0禁用 1启用',
  `sort` int(0) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_province_code`(`province_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='区域运费配置表';

-- ----------------------------
-- Step 5: 新增 express_company 表 - 快递公司配置表
-- ----------------------------
DROP TABLE IF EXISTS `express_company`;
CREATE TABLE `express_company` (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '快递公司编码（如：SF、YTO、ZTO、EMS）',
  `name` varchar(64) NOT NULL COMMENT '快递公司名称',
  `logo` varchar(255) DEFAULT NULL COMMENT 'Logo URL',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '客服电话',
  `sort` int(0) NOT NULL DEFAULT 0 COMMENT '排序',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用: 0禁用 1启用',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认: 0否 1是',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_company_code`(`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='快递公司配置表';

-- ----------------------------
-- Step 6: 初始化快递公司数据
-- ----------------------------
INSERT INTO `express_company` (`code`, `name`, `logo`, `contact_phone`, `sort`, `enabled`, `is_default`, `create_time`, `update_time`) VALUES
('SF', '顺丰速运', NULL, '95338', 1, 1, 0, NOW(), NOW()),
('YTO', '圆通速递', NULL, '95554', 2, 1, 0, NOW(), NOW()),
('ZTO', '中通快递', NULL, '95720', 3, 1, 0, NOW(), NOW()),
('STO', '申通快递', NULL, '95543', 4, 1, 0, NOW(), NOW()),
('YD', '韵达快递', NULL, '95546', 5, 1, 0, NOW(), NOW()),
('EMS', '邮政EMS', NULL, '11183', 6, 1, 1, NOW(), NOW()),
('YZPY', '邮政平邮', NULL, '11185', 7, 1, 0, NOW(), NOW());

-- ----------------------------
-- Step 7: 初始化运费配置数据（示例）
-- ----------------------------
INSERT INTO `freight_config` (`province_code`, `province_name`, `first_fee`, `continued_fee`, `enabled`, `sort`, `create_time`, `update_time`) VALUES
('110000', '北京市', 8.00, 2.00, 1, 1, NOW(), NOW()),
('310000', '上海市', 8.00, 2.00, 1, 2, NOW(), NOW()),
('440000', '广东省', 10.00, 3.00, 1, 3, NOW(), NOW()),
('440100', '广州市', 10.00, 3.00, 1, 4, NOW(), NOW()),
('440300', '深圳市', 10.00, 3.00, 1, 5, NOW(), NOW()),
('330000', '浙江省', 10.00, 3.00, 1, 6, NOW(), NOW()),
('320000', '江苏省', 10.00, 3.00, 1, 7, NOW(), NOW()),
('510000', '四川省', 12.00, 4.00, 1, 8, NOW(), NOW()),
('610000', '陕西省', 12.00, 4.00, 1, 9, NOW(), NOW()),
('230000', '黑龙江省', 15.00, 5.00, 1, 10, NOW(), NOW()),
('620000', '甘肃省', 15.00, 5.00, 1, 11, NOW(), NOW()),
('540000', '西藏自治区', 20.00, 8.00, 1, 12, NOW(), NOW());

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 数据库改造完成
-- ============================================
