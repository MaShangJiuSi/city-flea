<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTree, ElButton, ElCard, ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'

interface Category {
  id: number
  name: string
  type: number
  sort: number
  status: number
  children?: Category[]
}

const treeData = ref<Category[]>([])
const loading = ref(false)
const defaultProps = {
  children: 'children',
  label: 'name'
}

onMounted(() => {
  loadData()
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/category/list')
    treeData.value = res.data
  } catch (error) {
    console.error('加载分类失败:', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  console.log('新增分类')
}

const handleEdit = (data: Category) => {
  console.log('编辑分类', data)
}

const handleDelete = async (data: Category) => {
  try {
    await ElMessageBox.confirm('确定要删除该分类吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/admin/category/${data.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}
</script>

<template>
  <div class="category-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>分类管理</span>
          <div>
            <el-button type="primary" :icon="Plus" @click="handleAdd">新增分类</el-button>
            <el-button :icon="Refresh" @click="loadData">刷新</el-button>
          </div>
        </div>
      </template>

      <el-tree
        :data="treeData"
        :props="defaultProps"
        v-loading="loading"
        default-expand-all
        node-key="id"
      >
        <template #default="{ node, data }">
          <span class="custom-tree-node">
            <span>{{ node.label }}</span>
            <span>
              <el-button link type="primary" @click="handleEdit(data)">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button link type="danger" @click="handleDelete(data)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </span>
          </span>
        </template>
      </el-tree>
    </el-card>
  </div>
</template>

<style scoped>
.category-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.custom-tree-node {
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding-right: 20px;
}
</style>
