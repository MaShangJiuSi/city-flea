# 同城跳蚤市场 - 前端项目

## 项目介绍

本项目是同城跳蚤市场系统的 Vue 3 前端应用，提供管理端和用户端界面。

## 技术栈

- **框架**: Vue 3
- **构建工具**: Vite
- **语言**: TypeScript
- **UI 组件库**: Element Plus
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **HTTP 客户端**: Axios
- **图表库**: ECharts
- **样式**: Tailwind CSS + SCSS

## 项目结构

```
city-flea-web/
├── src/
│   ├── api/                 # API 接口
│   │   ├── admin/          # 管理端接口
│   │   └── user/          # 用户端接口
│   ├── layouts/            # 布局组件
│   │   ├── AdminLayout.vue  # 管理端布局
│   │   └── UserLayout.vue   # 用户端布局
│   ├── router/             # 路由配置
│   ├── stores/             # Pinia 状态管理
│   │   ├── user.ts        # 用户状态
│   │   ├── cart.ts        # 购物车状态
│   │   └── app.ts         # 应用状态
│   ├── utils/              # 工具函数
│   │   └── request.ts     # Axios 封装
│   ├── views/
│   │   ├── admin/         # 管理端页面
│   │   └── user/          # 用户端页面
│   └── main.ts
└── package.json
```

## 快速开始

### 安装依赖

```bash
pnpm install
```

### 开发模式

```bash
pnpm run dev
```

访问 http://localhost:5173

### 构建生产版本

```bash
pnpm run build
```

### 类型检查

```bash
pnpm run check
```

## 功能模块

### 管理端 (`/admin`)

- 登录认证
- 工作台（数据概览）
- 员工管理
- 分类管理
- 商品管理
- 订单管理
- 运费配置
- 快递公司
- 数据统计

### 用户端 (`/user`)

#### 买家功能

- 商品浏览
- 商品搜索
- 购物车
- 订单管理
- 地址管理
- 物流查询

#### 卖家功能

- 卖家中心
- 商品管理
- 发布商品
- 订单发货
- 资金账户

## 接口配置

环境变量配置在 `.env.development` 和 `.env.production` 文件中：

```env
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_TITLE=同城跳蚤市场
```

## 默认账户

- **管理员**: admin / 123456
- **用户**: 可通过微信授权登录（模拟）

## 开发指南

### 添加新页面

1. 在 `src/views/admin/` 或 `src/views/user/` 目录下创建页面组件
2. 在 `src/router/index.ts` 中添加路由配置
3. 如需布局，使用 AdminLayout 或 UserLayout 作为父组件

### 添加 API 接口

1. 在 `src/api/admin/index.ts` 或 `src/api/user/index.ts` 中添加接口方法
2. 使用封装的 request 方法发送请求

### 状态管理

使用 Pinia 进行状态管理：

```typescript
import { useUserStore } from '@/stores'

const userStore = useUserStore()
userStore.setToken(token)
userStore.setUserInfo(userInfo)
```

## 注意事项

1. 所有需要登录的路由都设置了 `requiresAuth` 元信息
2. API 请求自动携带 Token
3. 错误处理和响应拦截已在 request.ts 中配置
4. 确保后端服务运行在配置的端口上

## 许可证

MIT License
