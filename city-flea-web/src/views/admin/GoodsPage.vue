<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElTag, ElInput, ElSelect, ElOption, ElPagination, ElMessage } from 'element-plus'
import { Refresh, View, Check, Close } from '@element-plus/icons-vue'
import request from '@/utils/request'

interface Goods {
  id: number
  name: string
  categoryName: string
  price: number
  image: string
  status: number
  auditStatus: number
  createTime: string
}

const tableData = ref<Goods[]>([])
const loading = ref(false)
const searchName = ref('')
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
    const res = await request.get('/admin/goods/page', {
      params: {
        page: currentPage.value,
        pageSize: pageSize.value,
        name: searchName.value,
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

const handleView = (row: Goods) => {
  console.log('查看详情', row)
}

const handleAudit = async (row: Goods, status: number) => {
  try {
    await request.put(`/admin/goods/audit/${row.id}`, { status })
    ElMessage.success(status === 2 ? '审核通过' : '审核驳回')
    loadData()
  } catch (error) {
    console.error('审核失败:', error)
  }
}

const getStatusText = (status: number) => {
  const map: Record<number, string> = {
    0: '待审核',
    1: '已通过',
    2: '已驳回',
    3: '上架',
    4: '下架'
  }
  return map[status] || '未知'
}

const getStatusType = (status: number) => {
  const map: Record<number, 'info' | 'primary' | 'success' | 'warning' | 'danger'> = {
    0: 'warning',
    1: 'success',
    2: 'danger',
    3: 'success',
    4: 'info'
  }
  return map[status] || 'info'
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  loadData()
}
</script>

<template>
  <div class="goods-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>商品管理</span>
          <el-button :icon="Refresh" @click="loadData">刷新</el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input
          v-model="searchName"
          placeholder="商品名称"
          style="width: 200px"
          @keyup.enter="loadData"
        />
        <el-select v-model="searchStatus" placeholder="状态" style="width: 150px">
          <el-option label="全部" value="" />
          <el-option label="待审核" value="0" />
          <el-option label="已通过" value="1" />
          <el-option label="已驳回" value="2" />
          <el-option label="上架" value="3" />
          <el-option label="下架" value="4" />
        </el-select>
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="auditStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.auditStatus)">
              {{ getStatusText(row.auditStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">
              <el-icon><View /></el-icon>
            </el-button>
            <el-button
              v-if="row.auditStatus === 0"
              link
              type="success"
              @click="handleAudit(row, 1)"
            >
              <el-icon><Check /></el-icon>
            </el-button>
            <el-button
              v-if="row.auditStatus === 0"
              link
              type="danger"
              @click="handleAudit(row, 2)"
            >
              <el-icon><Close /></el-icon>
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
.goods-page {
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
