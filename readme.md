# city-flea 同城跳蚤市场系统

## 项目简介

city-flea 是一套基于 **Spring Boot + Vue 3** 构建的**同城二手跳蚤市场系统**，采用前后端分离架构，完整覆盖二手商品交易的全业务流程。系统包含平台管理端、买家/卖家用户端两大角色体系，实现了商品发布审核、下单支付、快递物流跟踪、资金结算等核心能力，为同城闲置物品交易提供了一站式解决方案。

**技术架构**：
- 后端：Spring Boot 3.x + MyBatis + MySQL + Redis
- 前端：Vue 3 + TypeScript + Element Plus + Vite
- 通信：RESTful API + WebSocket 实时推送

## 核心功能模块

### 1. 平台管理端（Admin）

- 员工管理：员工账号的增删改查、启用/禁用、登录权限控制
- 分类管理：商品分类的新增、编辑、删除、排序、上下架
- 商品审核：卖家发布的二手商品审核、驳回、状态管理
- 订单管理：全平台订单查询、取消、退款等全流程操作
- 运费配置：区域运费配置管理
- 快递公司：支持多快递公司接入
- 数据统计：营业额、订单、用户、商品销量的多维度数据报表
- 工作台：当日营业数据、订单状态概览、商品数据总览
- 报表导出：运营数据Excel报表一键导出

### 2. 用户端（User）

#### 买家能力

- 微信授权一键登录，JWT令牌鉴权
- 商品浏览、分类筛选、详情查看、购物车管理
- 收货地址管理、默认地址设置
- 订单提交、微信支付、订单状态查询、确认收货、取消订单
- 物流轨迹实时查看

#### 卖家能力

- 实名认证后可发布二手商品
- 商品管理：编辑、上下架、查看审核状态
- 发货地址管理、订单管理、交易资金自动结算
- 卖家发货：填写快递公司和运单号
- 资金流水记录、账户余额管理

### 3. 系统通用能力

- 订单超时自动取消：支付超时15分钟自动关闭订单
- 自动确认收货：送达24小时后买家未操作自动完成订单
- WebSocket实时消息推送：订单状态变更实时触达
- 微信支付API v3集成：支持支付、退款、支付结果回调
- 阿里云OSS文件存储：商品图片上传
- 快递物流集成：对接快递鸟API，支持多快递公司
- 区域运费计算：按收货区域智能计算运费
- 物流轨迹同步：定时任务自动同步快递状态
- 公共字段自动填充：创建/更新时间、操作人自动注入

## 技术栈

### 后端技术

| 技术/组件 | 版本/说明 |
|---|---|
| 开发语言 | Java 17 |
| 核心框架 | Spring Boot 3.x |
| 数据库 | MySQL 8.0+ |
| ORM框架 | MyBatis |
| 缓存数据库 | Redis 6.0+ |
| 数据库连接池 | Druid |
| 分页插件 | PageHelper |
| 接口文档 | Knife4j（OpenAPI 3） |
| 权限认证 | JWT |
| 实时通讯 | WebSocket |
| 支付集成 | 微信支付 API v3 |
| 文件存储 | 阿里云 OSS |
| 物流集成 | 快递鸟 API |
| 工具组件 | Lombok、FastJSON、Apache POI、HttpClient |
| 定时任务 | Spring Scheduled |
| 切面编程 | Spring AOP |

### 前端技术

| 技术/组件 | 版本/说明 |
|---|---|
| 核心框架 | Vue 3 |
| 构建工具 | Vite |
| 开发语言 | TypeScript |
| UI组件库 | Element Plus |
| 路由管理 | Vue Router 4 |
| 状态管理 | Pinia |
| HTTP客户端 | Axios |
| 图表库 | ECharts |
| 样式方案 | Tailwind CSS + SCSS |
| 图标库 | Lucide Icons |
| 代码规范 | ESLint + Prettier |

## 项目结构

```Plain Text

city-flea
├── city-flea-common       // 公共模块（后端）
│   ├── constant            // 常量类
│   ├── context             // 上下文工具（ThreadLocal）
│   ├── enumeration         // 枚举类
│   ├── exception           // 自定义异常与全局异常处理
│   ├── json                // JSON序列化配置
│   ├── properties          // 配置属性类
│   ├── result              // 统一响应结果封装
│   └── utils               // 工具类（JWT、HTTP、OSS、微信支付、快递鸟等）
├── city-flea-pojo          // 数据模型模块（后端）
│   ├── dto                 // 数据传输对象
│   ├── entity              // 数据库实体类
│   └── vo                  // 视图返回对象
├── city-flea-server        // 服务端核心模块（后端）
│   ├── annotation          // 自定义注解
│   ├── aspect              // AOP切面（公共字段自动填充）
│   ├── config              // 配置类（Web、Redis、WebSocket、Swagger等）
│   ├── controller          // 控制器（管理端/用户端/回调通知）
│   ├── handler             // 处理器（全局异常处理器）
│   ├── interceptor         // 拦截器（JWT令牌校验）
│   ├── mapper              // MyBatis数据访问层
│   ├── service             // 业务逻辑层
│   │   └── impl            // 业务实现类
│   ├── task                // 定时任务
│   ├── websocket           // WebSocket服务
│   └── resources           // 资源文件
│       ├── mapper          // MyBatis XML映射文件
│       ├── templates       // Excel报表模板
│       └── application.yml // 项目配置文件
└── city-flea-web           // 前端项目（Vue 3）
    ├── src/
    │   ├── api/            // API接口定义
    │   │   ├── admin/      // 管理端接口
    │   │   └── user/       // 用户端接口
    │   ├── layouts/        // 布局组件
    │   ├── router/         // 路由配置
    │   ├── stores/         // Pinia状态管理
    │   ├── utils/          // 工具函数
    │   ├── views/          // 页面组件
    │   │   ├── admin/      // 管理端页面
    │   │   └── user/       // 用户端页面
    │   └── main.ts         // 项目入口
    ├── public/             // 静态资源
    └── package.json        // 项目配置
```

## 环境要求

### 后端环境

- JDK 17 及以上版本
- MySQL 8.0 及以上版本
- Redis 6.0 及以上版本
- Maven 3.6 及以上版本
- 可选：Nginx（静态资源代理）、微信商户号/小程序账号（支付能力）、阿里云OSS账号（文件存储）、快递鸟账号（物流能力）

### 前端环境

- Node.js 18 及以上版本
- pnpm 8.x 或 npm 9.x 或 yarn 1.22+

## 快速开始

### 1. 克隆项目

```Bash

git clone <项目仓库地址>
cd city-flea
```

### 2. 后端部署

#### 2.1 数据库初始化

1. 新建MySQL数据库，字符集使用 `utf8mb4`

```SQL

CREATE DATABASE city_flea DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
```

2. 执行项目SQL脚本
   - 首先执行 `docs/sql/city_flea.sql` 完成基础表结构初始化
   - 然后执行 `docs/sql/migration_rider_to_express.sql` 完成快递物流模式改造

#### 2.2 配置文件修改

修改 `city-flea-server/src/main/resources/application.yml` 及配套的环境配置文件：

1. 数据库连接配置：修改MySQL的url、username、password

2. Redis配置：修改Redis的host、port、password

3. JWT配置：自定义admin/user端的秘钥、过期时间

4. 配送模式配置：
   ```yaml
   city:
     delivery:
       mode: express
       rider-enabled: false
   ```

5. 快递鸟配置（可选）：
   ```yaml
   city:
     express:
       ebusiness-id: your_ebusiness_id
       api-key: your_api_key
       push-url: your_callback_url
   ```

6. 可选配置：
   - 微信小程序/支付配置：填写appid、商户号、证书路径等
   - 阿里云OSS配置：填写endpoint、accessKey、bucketName等

#### 2.3 启动后端服务

1. 使用Maven编译项目

```Bash

cd city-flea
mvn clean install
```

2. 启动Spring Boot启动类 `com.city.CityApplication`

3. 项目默认启动端口：`8080`

### 3. 前端部署

#### 3.1 安装依赖

```Bash

cd city-flea-web
pnpm install
```

#### 3.2 配置环境变量

根据部署环境修改以下配置文件：

- `.env.development` - 开发环境配置
- `.env.production` - 生产环境配置

主要配置项：

```env

VITE_API_BASE_URL=http://localhost:8080
VITE_APP_TITLE=同城跳蚤市场
```

#### 3.3 启动前端服务

```Bash

# 开发模式
pnpm run dev

# 构建生产版本
pnpm run build
```

#### 3.4 访问应用

- 前端首页：`http://localhost:5173`
- 管理端：`http://localhost:5173/admin`
- 用户端：`http://localhost:5173/user`

## 接口文档

后端项目集成了Knife4j接口文档，后端启动成功后可访问以下地址：

- 文档首页：`http://localhost:8080/doc.html`
- OpenAPI规范地址：`http://localhost:8080/v3/api-docs`

## 核心数据库表说明

| 表名 | 说明 |
|---|---|
| address_book | 地址簿（收货/发货地址） |
| category | 商品分类表 |
| employee | 平台员工表 |
| goods | 二手商品表 |
| orders | 订单主表 |
| order_detail | 订单明细表 |
| order_delivery_track | 订单物流轨迹表 |
| express_company | 快递公司配置表 |
| freight_config | 区域运费配置表 |
| shopping_cart | 购物车表 |
| user | C端用户表（买家/卖家） |
| user_account_flow | 用户资金流水表 |

## 项目亮点

1. **前后端分离架构**：采用 Spring Boot + Vue 3 前后端分离设计，接口标准化，开发效率高

2. **多角色体系完整**：一套系统覆盖平台管理、买家、卖家三大角色，业务闭环完整

3. **多快递物流集成**：对接快递鸟API，支持多快递公司运单创建、轨迹查询

4. **区域智能运费计算**：根据收货地址自动匹配区域运费配置

5. **物流状态实时同步**：定时任务+推送双重机制确保物流信息及时更新

6. **自动化业务处理**：超时订单自动取消、超时自动收货、交易完成自动资金结算

7. **实时消息推送**：基于WebSocket实现订单状态变更的实时触达

8. **企业级代码规范**：统一响应封装、全局异常处理、自定义注解+AOP实现公共字段自动填充、多环境配置隔离

9. **高扩展性**：模块化分层架构，业务逻辑与数据访问解耦，便于功能迭代与扩展

10. **安全可靠**：JWT无状态鉴权、微信支付合规接入、敏感数据隔离、参数校验与异常兜底

11. **现代化前端技术**：Vue 3 Composition API + TypeScript + Pinia 状态管理，开发体验优秀

## 默认账户

### 后台管理端

- **管理员账号**：admin
- **管理员密码**：123456

### 用户端

- 支持微信授权登录（模拟环境）
- 可通过手机号+验证码登录

## 配送模式说明

本系统已从骑手即时配送模式改造为快递物流模式：

- **订单流程**：下单 → 支付 → 卖家发货（填快递） → 物流运输 → 买家确认收货
- **运费计算**：按收货省份区域自动计算，支持首重+续重模式
- **物流追踪**：集成快递鸟API，支持轨迹查询和状态推送
- **配置管理**：管理端可配置快递公司和区域运费

原骑手配送模块代码已保留但禁用，通过配置 `city.delivery.rider-enabled=false` 控制。

## 开发指南

### 前端开发

#### 添加新页面

1. 在 `city-flea-web/src/views/admin/` 或 `city-flea-web/src/views/user/` 目录下创建页面组件
2. 在 `city-flea-web/src/router/index.ts` 中添加路由配置
3. 如需布局，使用 AdminLayout 或 UserLayout 作为父组件

#### 添加 API 接口

1. 在 `city-flea-web/src/api/admin/index.ts` 或 `city-flea-web/src/api/user/index.ts` 中添加接口方法
2. 使用封装的 request 方法发送请求

#### 状态管理

使用 Pinia 进行状态管理：

```typescript

import { useUserStore } from '@/stores'

const userStore = useUserStore()
userStore.setToken(token)
userStore.setUserInfo(userInfo)
```

### 后端开发

#### 代码规范

- 使用 Lombok 简化代码
- 统一响应格式使用 Result 封装
- 业务异常使用自定义异常类
- 公共字段使用 @AutoFill 注解自动填充

#### 接口开发

1. 在 pojo 模块创建 DTO/VO 类
2. 在 mapper 模块创建 Mapper 接口和 XML
3. 在 service 模块实现业务逻辑
4. 在 controller 模块暴露 RESTful 接口
5. 使用 Knife4j 注解完善接口文档

## 注意事项

1. 所有需要登录的路由都设置了 `requiresAuth` 元信息
2. API 请求自动携带 Token
3. 错误处理和响应拦截已在 request.ts 中配置
4. 确保后端服务运行在配置的端口上
5. 前端开发环境默认连接 `http://localhost:8080`
