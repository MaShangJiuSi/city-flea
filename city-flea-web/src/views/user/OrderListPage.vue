<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElCard, ElTabs, ElTabPane, ElButton, ElEmpty } from 'element-plus'
import { View } from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()

const orderList = ref<any[]>([])
const loading = ref(false)
const activeTab = ref('all')

const statusMap: Record<number, string> = {
  1: '待付款',
  2: '待发货',
  3: '已发货',
  4: '已完成',
  5: '已取消'
}

onMounted(() => {
  loadOrders()
})

const loadOrders = async () => {
  loading.value = true
  try {
    const params: any = {}
    if (activeTab.value !== 'all') {
      params.status = parseInt(activeTab.value)
    }
    
    const res = await request.get('/user/order/historyOrders', { params })
    orderList.value = res.data.records || []
  } catch (error) {
    console.error('加载订单失败:', error)
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  loadOrders()
}

const goToOrderDetail = (orderId: number) => {
  router.push(`/user/order/${orderId}`)
}

const getStatusType = (status: number) => {
  const map: Record<number, string> = {
    1: 'warning',
    2: 'primary',
    3: 'info',
    4: 'success',
    5: 'danger'
  }
  return map[status] || 'info'
}
</script>

<template>
  <div class="order-list-page">
    <el-card v-loading="loading">
      <template #header>
        <span>我的订单</span>
      </template>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="待付款" name="1" />
        <el-tab-pane label="待发货" name="2" />
        <el-tab-pane label="已发货" name="3" />
        <el-tab-pane label="已完成" name="4" />
        <el-tab-pane label="已取消" name="5" />
      </el-tabs>

      <div v-if="orderList.length > 0">
        <div
          v-for="order in orderList"
          :key="order.id"
          class="order-card"
          @click="goToOrderDetail(order.id)"
        >
          <div class="order-header">
            <span class="order-number">订单号: {{ order.orderNumber }}</span>
            <span
              class="order-status"
              :class="'status-' + getStatusType(order.status)"
            >
              {{ statusMap[order.status] || '未知' }}
            </span>
          </div>
          
          <div class="order-body">
            <div class="order-item">
              <img
                :src="order.goodsImage || '/placeholder.png'"
                :alt="order.goodsName"
                class="item-image"
              />
              <div class="item-info">
                <h4>{{ order.goodsName }}</h4>
                <p class="item-desc">{{ order.remark || '无备注' }}</p>
              </div>
              <div class="item-price">
                <span>¥{{ order.totalAmount }}</span>
                <span class="quantity">x{{ order.quantity || 1 }}</span>
              </div>
            </div>
          </div>
          
          <div class="order-footer">
            <span class="order-time">{{ order.createTime }}</span>
            <el-button link type="primary" @click.stop="goToOrderDetail(order.id)">
              <el-icon><View /></el-icon>
              查看详情
            </el-button>
          </div>
        </div>
      </div>

      <el-empty v-else description="暂无订单" />
    </el-card>
  </div>
</template>

<style scoped>
.order-list-page {
  padding: 20px 0;
}

.order-card {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
  cursor: pointer;
  transition: box-shadow 0.3s;
}

.order-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 10px;
  border-bottom: 1px solid #f5f5f5;
}

.order-number {
  color: #666;
  font-size: 14px;
}

.order-status {
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
}

.status-warning {
  background: #fdf6ec;
  color: #e6a23c;
}

.status-primary {
  background: #ecf5ff;
  color: #409eff;
}

.status-info {
  background: #f4f4f5;
  color: #909399;
}

.status-success {
  background: #f0f9ff;
  color: #67c23a;
}

.status-danger {
  background: #fef0f0;
  color: #f56c6c;
}

.order-body {
  padding: 15px 0;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 15px;
}

.item-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.item-info {
  flex: 1;
}

.item-info h4 {
  margin: 0 0 8px;
  font-size: 16px;
}

.item-desc {
  margin: 0;
  color: #999;
  font-size: 14px;
}

.item-price {
  text-align: right;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.item-price span:first-child {
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
}

.quantity {
  font-size: 14px;
  color: #999;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #f5f5f5;
}

.order-time {
  color: #999;
  font-size: 14px;
}
</style>
