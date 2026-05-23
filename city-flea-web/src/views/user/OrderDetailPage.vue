<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElCard, ElButton, ElMessage, ElMessageBox, ElSkeleton } from 'element-plus'
import { LocationInformation, Message, Van } from '@element-plus/icons-vue'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()

const orderId = parseInt(route.params.id as string)
const order = ref<any>(null)
const expressInfo = ref<any>(null)
const loading = ref(false)

onMounted(async () => {
  await loadOrderDetail()
  await loadExpressInfo()
})

const loadOrderDetail = async () => {
  loading.value = true
  try {
    const res = await request.get(`/user/order/${orderId}`)
    order.value = res.data
  } catch (error) {
    console.error('加载订单详情失败:', error)
  } finally {
    loading.value = false
  }
}

const loadExpressInfo = async () => {
  try {
    const res = await request.get(`/user/express/${orderId}`)
    expressInfo.value = res.data
  } catch (error) {
    console.error('加载快递信息失败:', error)
  }
}

const handleCancel = async () => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.put(`/user/order/cancel/${orderId}`)
    ElMessage.success('订单已取消')
    loadOrderDetail()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消失败:', error)
    }
  }
}

const handleReceive = async () => {
  try {
    await ElMessageBox.confirm('确认已收到商品?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.put(`/user/order/receive/${orderId}`)
    ElMessage.success('已确认收货')
    loadOrderDetail()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('确认收货失败:', error)
    }
  }
}

const getStatusText = (status: number) => {
  const map: Record<number, string> = {
    1: '待付款',
    2: '待发货',
    3: '已发货',
    4: '已完成',
    5: '已取消'
  }
  return map[status] || '未知'
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
  <div class="order-detail-page">
    <el-card v-loading="loading">
      <el-skeleton :rows="8" animated v-if="loading" />
      
      <div v-else-if="order" class="order-detail">
        <div class="status-bar" :class="'status-' + getStatusType(order.status)">
          <span class="status-text">{{ getStatusText(order.status) }}</span>
          <span class="status-desc">
            <template v-if="order.status === 1">请尽快完成支付</template>
            <template v-else-if="order.status === 2">商家待发货</template>
            <template v-else-if="order.status === 3">商品运输中，请耐心等待</template>
            <template v-else-if="order.status === 4">交易已完成</template>
            <template v-else-if="order.status === 5">订单已取消</template>
          </span>
        </div>

        <div class="section address-section">
          <h3><el-icon><LocationInformation /></el-icon> 收货信息</h3>
          <div class="address-info">
            <p><strong>收货人:</strong> {{ order.consignee }}</p>
            <p><strong>联系电话:</strong> {{ order.phone }}</p>
            <p><strong>收货地址:</strong> {{ order.address }}</p>
          </div>
        </div>

        <div class="section goods-section">
          <h3>商品信息</h3>
          <div class="goods-item">
            <img :src="order.goodsImage || '/placeholder.png'" :alt="order.goodsName" />
            <div class="goods-info">
              <h4>{{ order.goodsName }}</h4>
              <p class="goods-desc">{{ order.remark || '无备注' }}</p>
            </div>
            <div class="goods-price">
              <span>¥{{ order.goodsPrice }}</span>
              <span class="quantity">x{{ order.quantity || 1 }}</span>
            </div>
          </div>
        </div>

        <div class="section express-section" v-if="expressInfo">
          <h3><el-icon><Van /></el-icon> 物流信息</h3>
          <div class="express-info">
            <p><strong>快递公司:</strong> {{ expressInfo.expressName }}</p>
            <p><strong>运单号:</strong> {{ expressInfo.trackingNumber }}</p>
          </div>
        </div>

        <div class="section order-info">
          <h3><el-icon><Message /></el-icon> 订单信息</h3>
          <div class="info-list">
            <p><strong>订单编号:</strong> {{ order.orderNumber }}</p>
            <p><strong>下单时间:</strong> {{ order.createTime }}</p>
            <p><strong>商品金额:</strong> ¥{{ order.goodsPrice * (order.quantity || 1) }}</p>
            <p><strong>运费:</strong> ¥{{ order.deliveryFee || 0 }}</p>
            <p class="total"><strong>实付金额:</strong> ¥{{ order.totalAmount }}</p>
          </div>
        </div>

        <div class="actions">
          <el-button @click="router.push('/user/order')">返回订单列表</el-button>
          <template v-if="order.status === 1">
            <el-button type="danger" @click="handleCancel">取消订单</el-button>
          </template>
          <template v-else-if="order.status === 3">
            <el-button type="success" @click="handleReceive">确认收货</el-button>
          </template>
        </div>
      </div>
      
      <div v-else class="empty-state">
        <p>订单不存在</p>
        <el-button @click="router.push('/user/order')">返回订单列表</el-button>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.order-detail-page {
  padding: 20px 0;
}

.status-bar {
  padding: 30px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.status-warning { background: linear-gradient(135deg, #fdf6ec, #fee); }
.status-primary { background: linear-gradient(135deg, #ecf5ff, #e6f0ff); }
.status-info { background: linear-gradient(135deg, #f4f4f5, #eee); }
.status-success { background: linear-gradient(135deg, #f0f9ff, #e6fff0); }
.status-danger { background: linear-gradient(135deg, #fef0f0, #fee); }

.status-text {
  font-size: 24px;
  font-weight: bold;
}

.status-desc {
  color: #666;
}

.section {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.section h3 {
  margin: 0 0 15px;
  display: flex;
  align-items: center;
  gap: 8px;
  color: #333;
}

.address-info p, .express-info p, .info-list p {
  margin: 10px 0;
  color: #666;
}

.info-list .total {
  font-size: 18px;
  color: #f56c6c;
  font-weight: bold;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.goods-section .goods-item {
  display: flex;
  align-items: center;
  gap: 15px;
}

.goods-section .goods-item img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
}

.goods-section .goods-info {
  flex: 1;
}

.goods-section .goods-info h4 {
  margin: 0 0 8px;
}

.goods-section .goods-desc {
  margin: 0;
  color: #999;
  font-size: 14px;
}

.goods-section .goods-price {
  text-align: right;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.goods-section .goods-price span:first-child {
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
}

.goods-section .goods-price .quantity {
  font-size: 14px;
  color: #999;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px 0;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
  color: #999;
}
</style>
