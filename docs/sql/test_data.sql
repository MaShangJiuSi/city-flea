/*
 * 同城跳蚤市场 - 测试数据
 * 生成日期：2026-05-18
 * 说明：用于测试和演示的虚拟数据
 */

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ================================
-- 1. 员工数据（管理员）
-- ================================
INSERT INTO `employee` 
(`Name`, `username`, `password`, `phone`, `sex`, `id_number`, `status`, `create_time`) 
VALUES 
('管理员', 'admin', '123456', '13800138000', '1', '110101199001011234', 1, NOW()),
('运营专员小王', 'operator1', '123456', '13800138001', '1', '110101199001011235', 1, NOW()),
('运营专员小李', 'operator2', '123456', '13800138002', '1', '110101199001011236', 1, NOW());

-- ================================
-- 2. 用户数据（C端用户）
-- ================================
INSERT INTO `user` 
(`openid`, `name`, `real_name`, `phone`, `sex`, `balance`, `auth_status`, `user_status`, `create_time`) 
VALUES 
('test_openid_buyer_001', '张三', '张三', '13900139001', '1', 5000.00, 2, 1, NOW()),
('test_openid_buyer_002', '李四', '李四', '13900139002', '1', 3000.00, 2, 1, NOW()),
('test_openid_buyer_003', '王五', '王五', '13900139003', '1', 8000.00, 2, 1, NOW()),
('test_openid_seller_001', '李老板', '李明', '13900139004', '1', 15000.00, 2, 1, NOW()),
('test_openid_seller_002', '张老板', '张强', '13900139005', '1', 20000.00, 2, 1, NOW()),
('test_openid_seller_003', '王老板', '王芳', '13900139006', '1', 12000.00, 2, 1, NOW());

-- ================================
-- 3. 商品分类
-- ================================
INSERT INTO `category` 
(`type`, `name`, `sort`, `status`, `create_time`) 
VALUES 
(1, '电子产品', 1, 1, NOW()),
(1, '图书文具', 2, 1, NOW()),
(1, '服装鞋帽', 3, 1, NOW()),
(1, '家居用品', 4, 1, NOW()),
(1, '运动户外', 5, 1, NOW()),
(1, '玩具乐器', 6, 1, NOW()),
(1, '数码配件', 7, 1, NOW()),
(1, '其他', 8, 1, NOW());

-- ================================
-- 4. 商品数据
-- ================================
INSERT INTO `goods` 
(`Name`, `category_id`, `seller_id`, `price`, `original_price`, `image`, `description`, `goods_status`, `condition`, `delivery_type`, `create_time`) 
VALUES 
('iPhone 14 Pro Max 256G 暗紫色', 1, 4, 6999.00, 8999.00, '/images/iphone14.jpg', '99新无划痕，配件齐全在保，喜欢的可以来看实物', 1, '99成新', 1, NOW()),
('MacBook Air M2 8GB+256GB 星光色', 1, 4, 7500.00, 9999.00, '/images/macbook.jpg', '2023年购买，使用不到半年，因换工作需要windows电脑，故出售', 1, '9成新', 1, NOW()),
('AirPods Pro 2 代', 1, 4, 1299.00, 1899.00, '/images/airpods.jpg', '全新未拆封，国行在保', 1, '全新', 1, NOW()),
('iPad Pro 11寸 256G WiFi版', 1, 4, 5000.00, 6999.00, '/images/ipad.jpg', '99新，附带Apple Pencil和妙控键盘', 1, '95成新', 1, NOW()),
('高等数学同济第七版上下册', 2, 5, 45.00, 89.00, '/images/math.jpg', '二手教材，八成新，有少量笔记', 1, '8成新', 1, NOW()),
('线性代数教材', 2, 5, 25.00, 45.00, '/images/linear.jpg', '七成新，笔记少，适合考研复习', 1, '7成新', 1, NOW()),
('雅思剑桥真题全套 4-16', 2, 5, 200.00, 598.00, '/images/ielts.jpg', '全套16本，九成新，只做过两套', 1, '9成新', 1, NOW()),
('卡西欧计算器 FX-991CN X', 2, 5, 120.00, 168.00, '/images/calculator.jpg', '大学生必备，研究生考试可用，功能齐全', 1, '9成新', 1, NOW()),
('Nike Air Max 270 React 42码', 3, 6, 499.00, 1099.00, '/images/nike.jpg', '穿了3次，鞋子太大穿不下，43码应该合适', 1, '9成新', 1, NOW()),
('Adidas三叶草卫衣 XL码', 3, 6, 199.00, 399.00, '/images/adidas.jpg', '全新带吊牌，换季清仓', 1, '全新', 1, NOW()),
('优衣库联名T恤 L码', 3, 6, 59.00, 99.00, '/images/uniqlo.jpg', '买大了，吊牌还在', 1, '全新', 1, NOW()),
('宜家MALM马尔姆六斗柜 白色', 4, 6, 599.00, 1299.00, '/images/ikea.jpg', '用了2年，搬家出，柜子状态良好，需自提', 1, '7成新', 1, NOW()),
('小米台灯1S 智能护眼灯', 4, 6, 129.00, 199.00, '/images/xiaomi-lamp.jpg', '99新，使用次数极少', 1, '99成新', 1, NOW()),
('迪卡侬瑜伽垫 10mm加厚款', 5, 6, 89.00, 199.00, '/images/yoga.jpg', '买来跳操用，买了更大尺寸的，这个出', 1, '9成新', 1, NOW()),
('索尼 WH-1000XM4 降噪耳机', 1, 6, 1599.00, 2299.00, '/images/sony.jpg', '黑色，95新，箱说全，附送耳机包', 1, '95成新', 1, NOW());

-- 待审核商品
INSERT INTO `goods` 
(`Name`, `category_id`, `seller_id`, `price`, `original_price`, `image`, `description`, `goods_status`, `condition`, `delivery_type`, `create_time`) 
VALUES 
('Apple Watch Series 8 45mm', 1, 4, 2999.00, 3999.00, '/images/watch.jpg', '个人闲置，GPS版，午夜色，9成新', 0, '9成新', 1, NOW()),
('任天堂Switch OLED 日版', 1, 5, 1899.00, 2599.00, '/images/switch.jpg', '包含主机+健身环+舞力全开，破解版', 0, '9成新', 1, NOW());

-- 已拒绝商品
INSERT INTO `goods` 
(`Name`, `category_id`, `seller_id`, `price`, `original_price`, `image`, `description`, `goods_status`, `condition`, `delivery_type`, `reject_reason`, `create_time`) 
VALUES 
('二手手机（无IMEI）', 1, 6, 200.00, 500.00, '/images/phone.jpg', '无法提供合法来源证明，故被下架', 2, '7成新', 1, '无法核实商品来源，拒绝上架', NOW());

-- ================================
-- 5. 收货地址
-- ================================
INSERT INTO `address_book` 
(`user_id`, `consignee`, `sex`, `phone`, `province_name`, `city_name`, `district_name`, `detail`, `is_default`, `address_type`)
VALUES 
(1, '张三', '1', '13900139001', '北京市', '市辖区', '朝阳区', '建国路88号SOHO现代城1号楼1501', 1, 2),
(1, '张三', '1', '13900139001', '北京市', '市辖区', '海淀区', '中关村大街1号院', 0, 2),
(2, '李四', '1', '13900139002', '上海市', '市辖区', '浦东新区', '张江高科技园区碧波路690号', 1, 2),
(3, '王五', '1', '13900139003', '广东省', '深圳市', '南山区', '科技园南区高新南七道R2-B栋', 1, 2),
(4, '李老板', '1', '13900139004', '浙江省', '杭州市', '西湖区', '文三路398号东信大厦', 1, 1),
(5, '张老板', '1', '13900139005', '江苏省', '南京市', '玄武区', '中山东路301号', 1, 1),
(6, '王老板', '1', '13900139006', '四川省', '成都市', '武侯区', '天府大道中段666号', 1, 1);

-- ================================
-- 6. 订单数据
-- ================================
-- 订单1：已完成
INSERT INTO `orders` 
(`user_id`, `address_book_id`, `number`, `order_status`, `seller_id`, `buyer_id`, `order_type`, 
 `send_address_id`, `receive_address_id`, `order_time`, `pay_time`, `ship_time`, 
 `receive_time`, `amount`, `goods_amount`, `delivery_fee`, `remark`, `pay_method`, `pay_status`,
 `consignee`, `phone`, `address`, `cancel_reason`, `rejection_reason`, `update_time`)
VALUES 
(1, 1, 'ORD202605180001', 6, 4, 1, 1, 4, 1, 
 '2026-05-15 10:30:00', '2026-05-15 10:31:00', '2026-05-15 14:00:00', 
 '2026-05-16 09:30:00',
 7087.00, 6999.00, 88.00, '尽快发货谢谢', 1, 1,
 '张三', '13900139001', '北京市市辖区朝阳区建国路88号SOHO现代城1号楼1501', '', '', NOW());

INSERT INTO `order_detail` 
(`order_id`, `goods_id`, `name`, `image`, `number`, `price`, `amount`)
VALUES 
(1, 1, 'iPhone 14 Pro Max 256G 暗紫色', '/images/iphone14.jpg', 1, 6999.00, 6999.00);

-- 订单2：已发货
INSERT INTO `orders` 
(`user_id`, `address_book_id`, `number`, `order_status`, `seller_id`, `buyer_id`, `order_type`, 
 `send_address_id`, `receive_address_id`, `order_time`, `pay_time`, `ship_time`,
 `amount`, `goods_amount`, `delivery_fee`, `remark`, `pay_method`, `pay_status`,
 `consignee`, `phone`, `address`, `cancel_reason`, `rejection_reason`, `update_time`)
VALUES 
(2, 2, 'ORD202605180002', 3, 5, 2, 1, 5, 2,
 '2026-05-17 16:20:00', '2026-05-17 16:21:00', '2026-05-17 18:00:00',
 245.00, 200.00, 45.00, '', 1, 1,
 '李四', '13900139002', '上海市市辖区浦东新区张江高科技园区碧波路690号', '', '', NOW());

INSERT INTO `order_detail` 
(`order_id`, `goods_id`, `name`, `image`, `number`, `price`, `amount`)
VALUES 
(2, 7, '雅思剑桥真题全套 4-16', '/images/ielts.jpg', 1, 200.00, 200.00);

-- 订单3：待发货
INSERT INTO `orders` 
(`user_id`, `address_book_id`, `number`, `order_status`, `seller_id`, `buyer_id`, `order_type`, 
 `send_address_id`, `receive_address_id`, `order_time`, `pay_time`,
 `amount`, `goods_amount`, `delivery_fee`, `remark`, `pay_method`, `pay_status`,
 `consignee`, `phone`, `address`, `cancel_reason`, `rejection_reason`, `update_time`)
VALUES 
(1, 1, 'ORD202605180003', 2, 4, 1, 1, 4, 1,
 '2026-05-18 09:15:00', '2026-05-18 09:16:00',
 1387.00, 1299.00, 88.00, '送礼用，包装好一些', 1, 1,
 '张三', '13900139001', '北京市市辖区朝阳区建国路88号SOHO现代城1号楼1501', '', '', NOW());

INSERT INTO `order_detail` 
(`order_id`, `goods_id`, `name`, `image`, `number`, `price`, `amount`)
VALUES 
(3, 3, 'AirPods Pro 2 代', '/images/airpods.jpg', 1, 1299.00, 1299.00);

-- 订单4：待付款
INSERT INTO `orders` 
(`user_id`, `address_book_id`, `number`, `order_status`, `seller_id`, `buyer_id`, `order_type`, 
 `send_address_id`, `receive_address_id`, `order_time`,
 `amount`, `goods_amount`, `delivery_fee`, `remark`, `pay_method`, `pay_status`,
 `consignee`, `phone`, `address`, `cancel_reason`, `rejection_reason`, `update_time`)
VALUES 
(3, 3, 'ORD202605180004', 1, 6, 3, 1, 6, 3,
 '2026-05-18 11:30:00',
 1618.00, 1599.00, 19.00, '', 1, 0,
 '王五', '13900139003', '广东省深圳市南山区科技园南区高新南七道R2-B栋', '', '', NOW());

INSERT INTO `order_detail` 
(`order_id`, `goods_id`, `name`, `image`, `number`, `price`, `amount`)
VALUES 
(4, 15, '索尼 WH-1000XM4 降噪耳机', '/images/sony.jpg', 1, 1599.00, 1599.00);

-- ================================
-- 7. 用户账户流水
-- ================================
INSERT INTO `user_account_flow` 
(`user_id`, `flow_type`, `flow_amount`, `order_id`, `flow_desc`, `balance_after`, `create_time`)
VALUES 
(4, 1, 6999.00, 1, '订单ORD202605180001交易收入', 22000.00, NOW()),
(5, 1, 200.00, 2, '订单ORD202605180002交易收入', 20200.00, NOW());

-- ================================
-- 8. 订单配送轨迹
-- ================================
INSERT INTO `order_delivery_track` 
(`order_id`, `track_type`, `track_status`, `track_desc`, `location`, `create_time`)
VALUES 
(1, 1, 0, '卖家已发货，快递公司：顺丰速运，运单号：SF1234567890', '杭州', '2026-05-15 14:00:00'),
(1, 1, 1, '快件已到达【杭州转运中心】', '杭州', '2026-05-15 16:30:00'),
(1, 1, 1, '快件已到达【北京运转中心】', '北京', '2026-05-16 06:00:00'),
(1, 1, 1, '正在派送途中，请您准备签收', '北京', '2026-05-16 08:30:00'),
(1, 1, 1, '已签收，签收人：本人', '北京', '2026-05-16 09:30:00'),
(2, 1, 0, '卖家已发货，快递公司：圆通速递，运单号：YT9876543210', '南京', '2026-05-17 18:00:00'),
(2, 1, 1, '快件已揽收，正在前往南京转运中心', '南京', '2026-05-17 18:30:00');

SET FOREIGN_KEY_CHECKS = 1;

-- ================================
-- 数据导入验证
-- ================================
SELECT '员工总数' as 项目, COUNT(*) as 数量 FROM employee
UNION ALL
SELECT '用户总数', COUNT(*) FROM user
UNION ALL
SELECT '商品总数(已审核)', COUNT(*) FROM goods WHERE goods_status = 1
UNION ALL
SELECT '待审核商品', COUNT(*) FROM goods WHERE goods_status = 0
UNION ALL
SELECT '订单总数', COUNT(*) FROM orders
UNION ALL
SELECT '分类总数', COUNT(*) FROM category;
