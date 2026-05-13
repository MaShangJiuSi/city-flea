# 邮政寄送模块 Postman 测试文件生成计划

## 1. 需求分析

为邮政寄送模块生成完整的 Postman 测试集合，包含以下功能模块：
- 用户认证
- 订单管理
- 卖家发货
- 物流查询
- 管理端配置

## 2. Postman Collection 结构

### Collection 基本信息
- Collection 名称：`city-flea 同城跳蚤市场 - 邮政寄送模块`
- 基础 URL：`http://localhost:8080`
- 变量配置：
  - `baseUrl`: `http://localhost:8080`
  - `userToken`: 用户认证令牌（自动保存）
  - `adminToken`: 管理员认证令牌（自动保存）

## 3. 测试接口列表

### 3.1 认证模块
- 用户登录 (POST /user/user/login)
- 管理员登录 (POST /admin/employee/login)

### 3.2 管理端 - 基础配置
- 获取快递公司列表 (GET /admin/express/list)
- 获取运费配置列表 (GET /admin/freight/list)

### 3.3 用户端 - 订单流程
- 提交订单 (POST /user/order/submit)
- 订单支付 (POST /user/order/payment)
- 订单详情 (GET /user/order/{id})
- 订单列表 (GET /user/order/historyOrders)

### 3.4 卖家端 - 发货管理
- 获取快递公司列表 (GET /user/seller/order/express/companies)
- 卖家发货 (POST /user/seller/order/ship)

### 3.5 物流查询
- 获取快递信息 (GET /user/express/{orderId})
- 获取物流轨迹 (GET /user/express/track/{orderId})

### 3.6 买家端 - 确认收货
- 确认收货 (PUT /user/order/receive/{id})

## 4. 测试数据编造

### 4.1 用户数据
- 测试用户：手机号 13800138000，微信登录码 test123
- 卖家用户：手机号 13900139000
- 管理员：账号 admin，密码 123456

### 4.2 商品数据
- 测试商品ID：1（iPhone 14 Pro 全新）
- 商品重量：500克

### 4.3 地址数据
- 收货地址ID：1
- 发货地址ID：1

### 4.4 快递数据
- 顺丰速运：代码 SF
- 圆通速递：代码 YTO
- 邮政EMS：代码 EMS
- 测试运单号：SF1234567890、YTO0987654321、EMS1122334455

### 4.5 订单数据
- 测试订单ID：1、2、3

## 5. 文件结构

将生成以下文件：
- `docs/postman/city-flea-postman-collection.json` - Postman 集合文件

## 6. 实现步骤

1. 创建 Postman Collection 基础结构
2. 添加认证接口及变量保存脚本
3. 添加管理端配置接口
4. 添加用户订单流程接口
5. 添加卖家发货接口
6. 添加物流查询接口
7. 添加确认收货接口
8. 配置请求头和认证方式
9. 添加前置脚本和测试脚本
