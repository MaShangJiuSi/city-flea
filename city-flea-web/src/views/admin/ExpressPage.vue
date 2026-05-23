<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElTag, ElDialog, ElForm, ElFormItem, ElInput, ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'

interface ExpressCompany {
  id: number
  name: string
  code: string
  logo?: string
  isEnabled: number
  createTime: string
}

const tableData = ref<ExpressCompany[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增快递公司')
const formData = ref<Partial<ExpressCompany>>({})

onMounted(() => {
  loadData()
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/express/list')
    tableData.value = res.data
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增快递公司'
  formData.value = {
    name: '',
    code: '',
    logo: '',
    isEnabled: 1
  }
  dialogVisible.value = true
}

const handleEdit = (row: ExpressCompany) => {
  dialogTitle.value = '编辑快递公司'
  formData.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = async (row: ExpressCompany) => {
  try {
    await ElMessageBox.confirm('确定要删除该快递公司吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/admin/express/${row.id}`)
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
      await request.put('/admin/express', formData.value)
      ElMessage.success('更新成功')
    } else {
      await request.post('/admin/express', formData.value)
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
  <div class="express-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>快递公司管理</span>
          <div>
            <el-button type="primary" :icon="Plus" @click="handleAdd">新增快递公司</el-button>
            <el-button :icon="Refresh" @click="loadData">刷新</el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="快递公司名称" />
        <el-table-column prop="code" label="快递代码" width="150" />
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
      <el-form :model="formData" label-width="100px">
        <el-form-item label="公司名称">
          <el-input v-model="formData.name" placeholder="如：顺丰速运" />
        </el-form-item>
        <el-form-item label="快递代码">
          <el-input v-model="formData.code" placeholder="如：SF" />
        </el-form-item>
        <el-form-item label="Logo URL">
          <el-input v-model="formData.logo" placeholder="可选" />
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
.express-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
