<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElCard, ElButton, ElTable, ElTableColumn, ElTag, ElMessage } from 'element-plus'
import { Plus, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()

interface Goods {
  id: number
  name: string
  categoryName: string
  price: number
  image: string
  status: number
  sales: number
  createTime: string
}

const goodsList = ref<Goods[]>([])
const loading = ref(false)

onMounted(() => {
  loadGoods()
})

const loadGoods = async () => {
  loading.value = true
  try {
    const res = await request.get('/user/seller/goods/list')
    goodsList.value = res.data.records || []
  } catch (error) {
    console.error('加载商品失败:', error)
  } finally {
    loading.value = false
  }
}

const handlePublish = () => {
  router.push('/user/seller/goods/publish')
}

const handleEdit = (row: Goods) => {
  router.push(`/user/seller/goods/edit/${row.id}`)
}

const handleDelete = async (row: Goods) => {
  try {
    await request.delete(`/user/seller/goods/${row.id}`)
    ElMessage.success('删除成功')
    loadGoods()
  } catch (error) {
    console.error('删除失败:', error)
  }
}

const handleToggleStatus = async (row: Goods) => {
  try {
    const newStatus = row.status === 1 ? 0 : 1
    await request.put(`/user/seller/goods/status/${row.id}`, { status: newStatus })
    ElMessage.success('状态修改成功')
    loadGoods()
  } catch (error) {
    console.error('修改状态失败:', error)
  }
}

const getStatusText = (status: number) => {
  const map: Record<number, string> = {
    0: '下架',
    1: '上架',
    2: '待审核'
  }
  return map[status] || '未知'
}

const getStatusType = (status: number) => {
  const map: Record<number, 'info' | 'primary' | 'success' | 'warning' | 'danger'> = {
    0: 'info',
    1: 'success',
    2: 'warning'
  }
  return map[status] || 'info'
}
</script>

<template>
  <div class="seller-goods-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的商品</span>
          <div>
            <el-button type="primary" :icon="Plus" @click="handlePublish">
              发布商品
            </el-button>
            <el-button :icon="Refresh" @click="loadGoods">刷新</el-button>
          </div>
        </div>
      </template>

      <el-table :data="goodsList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="sales" label="销量" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
            </el-button>
            <el-button
              link
              :type="row.status === 1 ? 'warning' : 'success'"
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
            <el-button link type="danger" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="goodsList.length === 0 && !loading" class="empty-state">
        <p>暂无商品</p>
        <el-button type="primary" @click="handlePublish">发布第一个商品</el-button>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.seller-goods-page {
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
