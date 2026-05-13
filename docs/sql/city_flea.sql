/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80200
 Source Host           : localhost:3306
 Source Schema         : city-flea

 Target Server Type    : MySQL
 Target Server Version : 80200
 File Encoding         : 65001

 Date: 2026-05-12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address_book
-- ----------------------------
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `consignee` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '收货人',
  `sex` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '性别',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '手机号',
  `province_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '省级区划编号',
  `province_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '省级名称',
  `city_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '市级区划编号',
  `city_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '市级名称',
  `district_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '区级区划编号',
  `district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '区级名称',
  `detail` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '详细地址',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标签',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '默认 0 否 1是',
  `address_type` tinyint(0) DEFAULT NULL COMMENT '地址类型 1发货 2收货',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '地址簿' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` int(0) DEFAULT NULL COMMENT '类型 1商品分类',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '分类名称',
  `sort` int(0) NOT NULL DEFAULT 0 COMMENT '顺序',
  `status` int(0) DEFAULT NULL COMMENT '分类状态 0:禁用，1:启用',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint(0) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(0) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_category_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '商品分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '姓名',
  `username` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '密码',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '手机号',
  `sex` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '性别',
  `id_number` varchar(18) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '身份证号',
  `status` int(0) NOT NULL DEFAULT 1 COMMENT '状态 0:禁用，1:启用',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint(0) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(0) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '员工信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for express_company
-- ----------------------------
DROP TABLE IF EXISTS `express_company`;
CREATE TABLE `express_company`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '快递公司编码',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '快递公司名称',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Logo URL',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '客服电话',
  `sort` int(0) NOT NULL DEFAULT 0 COMMENT '排序',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用: 0禁用 1启用',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认: 0否 1是',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_company_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '快递公司配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for freight_config
-- ----------------------------
DROP TABLE IF EXISTS `freight_config`;
CREATE TABLE `freight_config`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `province_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '省份编码',
  `province_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '省份名称',
  `first_fee` decimal(10,2) NOT NULL DEFAULT 8.00 COMMENT '首重价格(元)',
  `continued_fee` decimal(10,2) NOT NULL DEFAULT 3.00 COMMENT '续重价格(元/500g)',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用: 0禁用 1启用',
  `sort` int(0) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_province_code`(`province_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '区域运费配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint(0) NOT NULL COMMENT '分类id',
  `seller_id` bigint(0) DEFAULT NULL COMMENT '卖家用户ID',
  `send_address_id` bigint(0) DEFAULT NULL COMMENT '发货地址ID',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '商品名称',
  `image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '图片',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '描述信息',
  `condition` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '物品成色',
  `weight` int(0) DEFAULT 500 COMMENT '商品重量(克)，默认500克',
  `reject_reason` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '驳回原因',
  `price` decimal(10,2) DEFAULT NULL COMMENT '商品价格',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `goods_status` tinyint(0) DEFAULT NULL COMMENT '物品状态 0待审核 1审核通过 2审核驳回 3已下架 4已售出',
  `delivery_type` tinyint(0) DEFAULT NULL COMMENT '配送方式 1仅自提 2仅配送 3两者都支持',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint(0) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(0) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '二手商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_delivery_track
-- ----------------------------
DROP TABLE IF EXISTS `order_delivery_track`;
CREATE TABLE `order_delivery_track`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint(0) NOT NULL COMMENT '订单ID',
  `track_status` tinyint(0) NOT NULL COMMENT '轨迹类型: 0系统事件 1快递物流轨迹',
  `track_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '轨迹描述',
  `express_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '快递状态编码',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '物流发生地点',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_track_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单物流轨迹表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '商品名称',
  `image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '图片',
  `order_id` bigint(0) NOT NULL COMMENT '订单id',
  `goods_id` bigint(0) DEFAULT NULL COMMENT '商品id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '单价',
  `number` int(0) NOT NULL DEFAULT 1 COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '订单号',
  `order_status` tinyint(0) DEFAULT NULL COMMENT '订单状态: 1待支付 2待发货 3已发货 4物流运输中 5待收货 6已完成 7已取消 8退款中 9已退款',
  `seller_id` bigint(0) DEFAULT NULL COMMENT '卖家用户ID',
  `buyer_id` bigint(0) DEFAULT NULL COMMENT '买家用户ID',
  `express_company` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '快递公司编码',
  `tracking_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '运单号',
  `order_type` tinyint(0) DEFAULT NULL COMMENT '订单类型 1自提 2配送',
  `send_address_id` bigint(0) DEFAULT NULL COMMENT '发货地址ID',
  `receive_address_id` bigint(0) DEFAULT NULL COMMENT '收货地址ID',
  `order_time` datetime(0) NOT NULL COMMENT '下单时间',
  `pay_time` datetime(0) DEFAULT NULL COMMENT '支付时间',
  `ship_time` datetime(0) DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime(0) DEFAULT NULL COMMENT '确认收货时间',
  `cancel_time` datetime(0) DEFAULT NULL COMMENT '订单取消时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `pay_method` int(0) NOT NULL DEFAULT 1 COMMENT '支付方式 1微信',
  `pay_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '支付状态 0未支付 1已支付 2退款',
  `amount` decimal(10,2) NOT NULL COMMENT '实收金额',
  `goods_amount` decimal(10,2) DEFAULT NULL COMMENT '物品金额',
  `delivery_fee` decimal(10,2) DEFAULT NULL COMMENT '运费',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '备注',
  `seller_consignee` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '发货联系人',
  `seller_phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '发货联系电话',
  `send_address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '发货地址快照',
  `buyer_consignee` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '收货联系人',
  `buyer_phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '收货联系电话',
  `receive_address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '收货地址快照',
  `cancel_reason` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '订单取消原因',
  `rejection_reason` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '订单拒绝原因',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rider
-- ----------------------------
DROP TABLE IF EXISTS `rider`;
CREATE TABLE `rider`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `real_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '骑手真实姓名',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录密码',
  `work_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '工作状态 0休息 1接单中',
  `audit_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '审核状态 0待审核 1通过 2驳回',
  `rider_status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '账号状态 0禁用 1正常',
  `id_card_front` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '身份证正面照',
  `id_card_back` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '身份证反面照',
  `health_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '健康证',
  `balance` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
  `create_time` datetime(0) DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_rider_phone`(`phone`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '骑手信息表（已停用）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '商品名称',
  `image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '图片',
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `goods_id` bigint(0) DEFAULT NULL COMMENT '商品id',
  `seller_id` bigint(0) DEFAULT NULL COMMENT '卖家ID',
  `number` int(0) NOT NULL DEFAULT 1 COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '购物车' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `openid` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '微信用户唯一标识',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '姓名',
  `real_name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '手机号',
  `sex` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '性别',
  `id_card` varchar(18) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '身份证号',
  `avatar` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '头像',
  `auth_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '实名认证状态',
  `user_status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '用户状态',
  `balance` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '用户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_account_flow
-- ----------------------------
DROP TABLE IF EXISTS `user_account_flow`;
CREATE TABLE `user_account_flow`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `flow_type` tinyint(0) NOT NULL COMMENT '流水类型 1收入 2支出 3冻结 4解冻',
  `flow_amount` decimal(10,2) NOT NULL COMMENT '流水金额',
  `order_id` bigint(0) DEFAULT NULL COMMENT '关联订单ID',
  `flow_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水描述',
  `balance_after` decimal(10,2) NOT NULL COMMENT '流水后余额',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_account_flow_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户资金流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 初始化数据：快递公司
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
-- 初始化数据：运费配置
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

-- ----------------------------
-- 初始化数据：管理员账号
-- ----------------------------
INSERT INTO `employee` (`name`, `username`, `password`, `phone`, `sex`, `id_number`, `status`, `create_time`, `update_time`) VALUES
('管理员', 'admin', '$2a$10$8.aA1m7p3rY7z8h9j8jK1OXqQqQqQqQqQqQqQqQqQqQqQqQqQqQqQq', '13800138000', '男', '110101199001011234', 1, NOW(), NOW());

SET FOREIGN_KEY_CHECKS = 1;
