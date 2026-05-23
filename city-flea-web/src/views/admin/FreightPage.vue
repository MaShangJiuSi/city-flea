<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElTag, ElDialog, ElForm, ElFormItem, ElInput, ElInputNumber, ElSwitch, ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'

interface FreightConfig {
  id: number
  region: string
  firstWeight: number
  firstPrice: number
  continueWeight: number
  continuePrice: number
  isEnabled: number
  createTime: string
}

const tableData = ref<FreightConfig[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增运费配置')
const formData = ref<Partial<FreightConfig>>({})

onMounted(() => {
  loadData()
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/freight/list')
    tableData.value = res.data
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增运费配置'
  formData.value = {
    region: '',
    firstWeight: 1,
    firstPrice: 0,
    continueWeight: 1,
    continuePrice: 0,
    isEnabled: 1
  }
  dialogVisible.value = true
}

const handleEdit = (row: FreightConfig) => {
  dialogTitle.value = '编辑运费配置'
  formData.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = async (row: FreightConfig) => {
  try {
    await ElMessageBox.confirm('确定要删除该运费配置吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/admin/freight/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    if (formData.value.id) {
      await request.put('/admin/freight', formData.value)
      ElMessage.success('更新成功')
    } else {
      await request.post('/admin/freight', formData.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('操作失败:', error)
  }
}
</script>

<template>
  <div class="freight-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>运费配置</span>
          <div>
            <el-button type="primary" :icon="Plus" @click="handleAdd">新增配置</el-button>
            <el-button :icon="Refresh" @click="loadData">刷新</el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="region" label="区域" />
        <el-table-column prop="firstWeight" label="首重(kg)" width="100" />
        <el-table-column prop="firstPrice" label="首重价格(元)" width="120" />
        <el-table-column prop="continueWeight" label="续重(kg)" width="100" />
        <el-table-column prop="continuePrice" label="续重价格(元)" width="120" />
        <el-table-column prop="isEnabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
              {{ row.isEnabled === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
            </el-button>
            <el-button link type="danger" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="formData" label-width="120px">
        <el-form-item label="区域名称">
          <el-input v-model="formData.region" placeholder="如：华东地区" />
        </el-form-item>
        <el-form-item label="首重(kg)">
          <el-input-number v-model="formData.firstWeight" :min="0.1" :step="0.1" />
        </el-form-item>
        <el-form-item label="首重价格(元)">
          <el-input-number v-model="formData.firstPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="续重(kg)">
          <el-input-number v-model="formData.continueWeight" :min="0.1" :step="0.1" />
        </el-form-item>
        <el-form-item label="续重价格(元)">
          <el-input-number v-model="formData.continuePrice" :min="0" :precision="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.freight-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
