import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/user'
  },
  {
    path: '/user',
    component: () => import('@/layouts/UserLayout.vue'),
    children: [
      {
        path: '',
        name: 'UserHome',
        component: () => import('@/views/user/HomePage.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'login',
        name: 'UserLogin',
        component: () => import('@/views/user/LoginPage.vue'),
        meta: { title: '登录', guest: true }
      },
      {
        path: 'goods',
        name: 'GoodsList',
        component: () => import('@/views/user/GoodsListPage.vue'),
        meta: { title: '商品列表' }
      },
      {
        path: 'goods/:id',
        name: 'GoodsDetail',
        component: () => import('@/views/user/GoodsDetailPage.vue'),
        meta: { title: '商品详情' }
      },
      {
        path: 'cart',
        name: 'Cart',
        component: () => import('@/views/user/CartPage.vue'),
        meta: { title: '购物车', requiresAuth: true }
      },
      {
        path: 'order',
        name: 'OrderList',
        component: () => import('@/views/user/OrderListPage.vue'),
        meta: { title: '我的订单', requiresAuth: true }
      },
      {
        path: 'order/:id',
        name: 'OrderDetail',
        component: () => import('@/views/user/OrderDetailPage.vue'),
        meta: { title: '订单详情', requiresAuth: true }
      },
      {
        path: 'address',
        name: 'AddressList',
        component: () => import('@/views/user/AddressListPage.vue'),
        meta: { title: '地址管理', requiresAuth: true }
      },
      {
        path: 'seller',
        name: 'SellerDashboard',
        component: () => import('@/views/user/SellerDashboard.vue'),
        meta: { title: '卖家中心', requiresAuth: true }
      },
      {
        path: 'seller/goods',
        name: 'SellerGoods',
        component: () => import('@/views/user/SellerGoodsPage.vue'),
        meta: { title: '我的商品', requiresAuth: true }
      },
      {
        path: 'seller/goods/publish',
        name: 'PublishGoods',
        component: () => import('@/views/user/PublishGoodsPage.vue'),
        meta: { title: '发布商品', requiresAuth: true }
      },
      {
        path: 'seller/goods/edit/:id',
        name: 'EditGoods',
        component: () => import('@/views/user/EditGoodsPage.vue'),
        meta: { title: '编辑商品', requiresAuth: true }
      },
      {
        path: 'seller/order',
        name: 'SellerOrder',
        component: () => import('@/views/user/SellerOrderPage.vue'),
        meta: { title: '卖家订单', requiresAuth: true }
      },
      {
        path: 'seller/account',
        name: 'SellerAccount',
        component: () => import('@/views/user/SellerAccountPage.vue'),
        meta: { title: '资金账户', requiresAuth: true }
      }
    ]
  },
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'login',
        name: 'AdminLogin',
        component: () => import('@/views/admin/LoginPage.vue'),
        meta: { title: '管理员登录', guest: true }
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/admin/DashboardPage.vue'),
        meta: { title: '工作台', requiresAuth: true, role: 'admin' }
      },
      {
        path: 'employee',
        name: 'Employee',
        component: () => import('@/views/admin/EmployeePage.vue'),
        meta: { title: '员工管理', requiresAuth: true, role: 'admin' }
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('@/views/admin/CategoryPage.vue'),
        meta: { title: '分类管理', requiresAuth: true, role: 'admin' }
      },
      {
        path: 'goods',
        name: 'Goods',
        component: () => import('@/views/admin/GoodsPage.vue'),
        meta: { title: '商品管理', requiresAuth: true, role: 'admin' }
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('@/views/admin/OrderPage.vue'),
        meta: { title: '订单管理', requiresAuth: true, role: 'admin' }
      },
      {
        path: 'freight',
        name: 'Freight',
        component: () => import('@/views/admin/FreightPage.vue'),
        meta: { title: '运费配置', requiresAuth: true, role: 'admin' }
      },
      {
        path: 'express',
        name: 'Express',
        component: () => import('@/views/admin/ExpressPage.vue'),
        meta: { title: '快递公司', requiresAuth: true, role: 'admin' }
      },
      {
        path: 'report',
        name: 'Report',
        component: () => import('@/views/admin/ReportPage.vue'),
        meta: { title: '数据统计', requiresAuth: true, role: 'admin' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const title = to.meta.title as string
  if (title) {
    document.title = `${title} - 同城跳蚤市场`
  }

  const token = localStorage.getItem('token')
  const userInfo = localStorage.getItem('userInfo')

  if (to.meta.requiresAuth && !token) {
    next({
      path: to.path.startsWith('/admin') ? '/admin/login' : '/user/login',
      query: { redirect: to.fullPath }
    })
  } else if (to.meta.guest && token) {
    next({
      path: to.path.startsWith('/admin') ? '/admin/dashboard' : '/user'
    })
  } else {
    next()
  }
})

export default router
