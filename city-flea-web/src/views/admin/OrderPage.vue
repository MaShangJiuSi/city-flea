<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElTag, ElInput, ElSelect, ElOption, ElPagination, ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, View, Close } from '@element-plus/icons-vue'
import request from '@/utils/request'

interface Order {
  id: number
  orderNumber: string
  userName: string
  goodsName: string
  totalAmount: number
  status: number
  payMethod: number
  createTime: string
}

const tableData = ref<Order[]>([])
const loading = ref(false)
const searchOrderNumber = ref('')
const searchStatus = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(() => {
  loadData()
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/order/page', {
      params: {
        page: currentPage.value,
        pageSize: pageSize.value,
        orderNumber: searchOrderNumber.value,
        status: searchStatus.value
      }
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const handleView = (row: Order) => {
  console.log('查看详情', row)
}

const handleCancel = async (row: Order) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.put(`/admin/order/cancel/${row.id}`)
    ElMessage.success('取消成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消失败:', error)
    }
  }
}

const handleRefund = async (row: Order) => {
  try {
    await ElMessageBox.confirm('确定要退款吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.put(`/admin/order/refund/${row.id}`)
    ElMessage.success('退款成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('退款失败:', error)
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
  const map: Record<number, 'info' | 'primary' | 'success' | 'warning' | 'danger'> = {
    1: 'warning',
    2: 'primary',
    3: 'info',
    4: 'success',
    5: 'danger'
  }
  return map[status] || 'info'
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  loadData()
}
</script>

<template>
  <div class="order-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
          <el-button :icon="Refresh" @click="loadData">刷新</el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input
          v-model="searchOrderNumber"
          placeholder="订单号"
          style="width: 200px"
          @keyup.enter="loadData"
        />
        <el-select v-model="searchStatus" placeholder="订单状态" style="width: 150px">
          <el-option label="全部" value="" />
          <el-option label="待付款" value="1" />
          <el-option label="待发货" value="2" />
          <el-option label="已发货" value="3" />
          <el-option label="已完成" value="4" />
          <el-option label="已取消" value="5" />
        </el-select>
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNumber" label="订单号" width="180" />
        <el-table-column prop="userName" label="买家" width="120" />
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
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">
              <el-icon><View /></el-icon>
            </el-button>
            <el-button
              v-if="row.status === 1 || row.status === 2"
              link
              type="danger"
              @click="handleCancel(row)"
            >
              <el-icon><Close /></el-icon>
            </el-button>
            <el-button
              v-if="row.status === 4 || row.status === 5"
              link
              type="warning"
              @click="handleRefund(row)"
            >
              退款
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.order-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
