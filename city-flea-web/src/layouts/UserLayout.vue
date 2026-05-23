<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMenu, ElMenuItem, ElBadge, ElIcon } from 'element-plus'
import { HomeFilled, ShoppingCart, List, User, Sell, Goods, Wallet } from '@element-plus/icons-vue'
import { useUserStore, useCartStore } from '@/stores'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const cartStore = useCartStore()

const activeIndex = computed(() => route.path)

const handleCommand = (command: string) => {
  switch (command) {
    case 'logout':
      userStore.logout()
      router.push('/user')
      break
    case 'seller':
      router.push('/user/seller')
      break
    case 'order':
      router.push('/user/order')
      break
    case 'address':
      router.push('/user/address')
      break
  }
}

const menuItems = [
  { path: '/user', title: '首页', icon: HomeFilled },
  { path: '/user/goods', title: '商品', icon: Goods }
]

const userMenuItems = [
  { command: 'seller', title: '卖家中心', icon: Sell },
  { command: 'order', title: '我的订单', icon: List },
  { command: 'address', title: '地址管理', icon: User }
]

const handleMenuItemClick = (path: string) => {
  router.push(path)
}
</script>

<template>
  <div class="user-layout">
    <header class="header">
      <div class="header-content">
        <div class="logo">
          <h1 @click="router.push('/user')">同城跳蚤市场</h1>
        </div>
        
        <el-menu
          mode="horizontal"
          :default-active="activeIndex"
          class="header-menu"
        >
          <el-menu-item
            v-for="item in menuItems"
            :key="item.path"
            :index="item.path"
            @click="handleMenuItemClick(item.path)"
          >
            <el-icon><component :is="item.icon" /></el-icon>
            {{ item.title }}
          </el-menu-item>
        </el-menu>

        <div class="header-actions">
          <el-badge :value="cartStore.totalCount" :hidden="cartStore.totalCount === 0">
            <el-button
              text
              @click="router.push('/user/cart')"
            >
              <el-icon size="20"><ShoppingCart /></el-icon>
            </el-button>
          </el-badge>

          <template v-if="userStore.isLoggedIn">
            <el-dropdown @command="handleCommand">
              <span class="user-dropdown">
                <el-avatar :size="32" />
                <span class="username">{{ userStore.userInfo?.name || '用户' }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    v-for="item in userMenuItems"
                    :key="item.command"
                    :command="item.command"
                  >
                    <el-icon><component :is="item.icon" /></el-icon>
                    {{ item.title }}
                  </el-dropdown-item>
                  <el-dropdown-item command="logout" divided>
                    <el-icon><Logout /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="router.push('/user/login')">
              登录
            </el-button>
          </template>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>

    <footer class="footer">
      <p>&copy; 2024 同城跳蚤市场. All rights reserved.</p>
    </footer>
  </div>
</template>

<style scoped>
.user-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  padding: 0 20px;
  height: 60px;
}

.logo {
  margin-right: 40px;
}

.logo h1 {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  cursor: pointer;
  margin: 0;
}

.header-menu {
  flex: 1;
  border-bottom: none;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.username {
  font-size: 14px;
  color: #333;
}

.main-content {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 20px;
}

.footer {
  background: #fff;
  padding: 20px;
  text-align: center;
  color: #666;
  font-size: 14px;
  margin-top: auto;
}

.footer p {
  margin: 0;
}
</style>
