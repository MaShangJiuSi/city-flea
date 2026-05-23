<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElCard, ElButton, ElTable, ElTableColumn, ElTag, ElDialog, ElForm, ElFormItem, ElSelect, ElOption, ElMessage } from 'element-plus'
import { Van, Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'

interface Order {
  id: number
  orderNumber: string
  buyerName: string
  goodsName: string
  totalAmount: number
  status: number
  createTime: string
}

const orderList = ref<Order[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const expressCompanies = ref<any[]>([])
const shipForm = ref({
  orderId: 0,
  expressCode: '',
  trackingNumber: ''
})

onMounted(() => {
  loadOrders()
})

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await request.get('/user/seller/order/list')
    orderList.value = res.data.records || []
  } catch (error) {
    console.error('加载订单失败:', error)
  } finally {
    loading.value = false
  }
}

const handleShip = async (order: Order) => {
  shipForm.value = {
    orderId: order.id,
    expressCode: '',
    trackingNumber: ''
  }
  
  try {
    const res = await request.get('/user/seller/order/express/companies')
    expressCompanies.value = res.data || []
    dialogVisible.value = true
  } catch (error) {
    console.error('加载快递公司失败:', error)
  }
}

const handleSubmitShip = async () => {
  if (!shipForm.value.expressCode) {
    ElMessage.warning('请选择快递公司')
    return
  }
  if (!shipForm.value.trackingNumber) {
    ElMessage.warning('请输入运单号')
    return
  }

  try {
    await request.post('/user/seller/order/ship', shipForm.value)
    ElMessage.success('发货成功')
    dialogVisible.value = false
    loadOrders()
  } catch (error) {
    console.error('发货失败:', error)
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
  const map: Record<number, 'info' | 'primary' | 'success' | 'warning' | 'danger'> = {
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
  <div class="seller-order-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>卖家订单</span>
          <el-button :icon="Refresh" @click="loadOrders">刷新</el-button>
        </div>
      </template>

      <el-table :data="orderList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNumber" label="订单号" width="180" />
        <el-table-column prop="buyerName" label="买家" width="120" />
        <el-table-column prop="goodsName" label="商品" />
        <el-table-column prop="totalAmount" label="金额" width="100">
          <template #default="{ row }">
            ¥{{ row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 2"
              type="primary"
              size="small"
              @click="handleShip(row)"
            >
              <el-icon><Van /></el-icon>
              发货
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="orderList.length === 0 && !loading" class="empty-state">
        <p>暂无订单</p>
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" title="发货" width="500px">
      <el-form :model="shipForm" label-width="100px">
        <el-form-item label="快递公司">
          <el-select v-model="shipForm.expressCode" placeholder="请选择快递公司" style="width: 100%">
            <el-option
              v-for="company in expressCompanies"
              :key="company.code"
              :label="company.name"
              :value="company.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="运单号">
          <el-input v-model="shipForm.trackingNumber" placeholder="请输入运单号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitShip">确认发货</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.seller-order-page {
  padding: 20px 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
  color: #999;
}
</style>
