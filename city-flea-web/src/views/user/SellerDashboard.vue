<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElCard, ElRow, ElCol, ElStatistic } from 'element-plus'
import { Goods, ShoppingCart, Wallet, Sell } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { useUserStore } from '@/stores'

const router = useRouter()
const userStore = useUserStore()

const stats = ref({
  goodsCount: 0,
  orderCount: 0,
  salesAmount: 0
})

onMounted(async () => {
  await loadStats()
})

const loadStats = async () => {
  try {
    const res = await request.get('/user/seller/account/info')
    stats.value = {
      goodsCount: res.data.goodsCount || 0,
      orderCount: res.data.orderCount || 0,
      salesAmount: res.data.salesAmount || 0
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const menuItems = [
  {
    title: '我的商品',
    desc: '管理我的商品',
    icon: Goods,
    path: '/user/seller/goods',
    color: '#409eff'
  },
  {
    title: '卖家订单',
    desc: '处理订单和发货',
    icon: ShoppingCart,
    path: '/user/seller/order',
    color: '#67c23a'
  },
  {
    title: '资金账户',
    desc: '查看账户余额和流水',
    icon: Wallet,
    path: '/user/seller/account',
    color: '#e6a23c'
  }
]

const handleMenuClick = (path: string) => {
  router.push(path)
}
</script>

<template>
  <div class="seller-dashboard">
    <el-card>
      <template #header>
        <div class="header">
          <span>卖家中心</span>
          <span class="user-info">欢迎, {{ userStore.userInfo?.name || '卖家' }}</span>
        </div>
      </template>

      <el-row :gutter="20" class="stats-row">
        <el-col :span="8">
          <el-statistic title="商品数量" :value="stats.goodsCount">
            <template #prefix>
              <el-icon size="24" color="#409eff"><Goods /></el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="8">
          <el-statistic title="订单数量" :value="stats.orderCount">
            <template #prefix>
              <el-icon size="24" color="#67c23a"><ShoppingCart /></el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="8">
          <el-statistic title="销售总额" :value="stats.salesAmount">
            <template #prefix>
              <el-icon size="24" color="#e6a23c"><Wallet /></el-icon>
            </template>
            <template #suffix>元</template>
          </el-statistic>
        </el-col>
      </el-row>

      <div class="menu-grid">
        <div
          v-for="item in menuItems"
          :key="item.path"
          class="menu-card"
          @click="handleMenuClick(item.path)"
        >
          <div class="menu-icon" :style="{ background: item.color }">
            <el-icon size="40" color="#fff">
              <component :is="item.icon" />
            </el-icon>
          </div>
          <div class="menu-info">
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.seller-dashboard {
  padding: 20px 0;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  color: #999;
  font-size: 14px;
}

.stats-row {
  margin-bottom: 40px;
  padding: 20px 0;
}

.menu-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.menu-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.menu-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-4px);
}

.menu-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.menu-info h3 {
  margin: 0 0 8px;
  font-size: 18px;
}

.menu-info p {
  margin: 0;
  color: #999;
  font-size: 14px;
}

@media (max-width: 768px) {
  .menu-grid {
    grid-template-columns: 1fr;
  }
}
</style>
