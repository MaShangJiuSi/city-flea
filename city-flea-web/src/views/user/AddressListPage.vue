<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElCard, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElRadioGroup, ElRadio, ElMessage, ElMessageBox, ElTag } from 'element-plus'
import { Plus, Edit, Delete, Location } from '@element-plus/icons-vue'
import request from '@/utils/request'

interface Address {
  id: number
  consignee: string
  phone: string
  province: string
  city: string
  district: string
  detail: string
  isDefault: number
}

const addressList = ref<Address[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增地址')
const formData = ref<Partial<Address>>({})

onMounted(() => {
  loadAddresses()
})

const loadAddresses = async () => {
  loading.value = true
  try {
    const res = await request.get('/user/addressBook/list')
    addressList.value = res.data || []
  } catch (error) {
    console.error('加载地址失败:', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增地址'
  formData.value = {
    consignee: '',
    phone: '',
    province: '',
    city: '',
    district: '',
    detail: '',
    isDefault: 0
  }
  dialogVisible.value = true
}

const handleEdit = (address: Address) => {
  dialogTitle.value = '编辑地址'
  formData.value = { ...address }
  dialogVisible.value = true
}

const handleDelete = async (address: Address) => {
  try {
    await ElMessageBox.confirm('确定要删除该地址吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/user/addressBook/${address.id}`)
    ElMessage.success('删除成功')
    loadAddresses()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handleSetDefault = async (address: Address) => {
  try {
    await request.put(`/user/addressBook/default/${address.id}`)
    ElMessage.success('设置成功')
    loadAddresses()
  } catch (error) {
    console.error('设置默认地址失败:', error)
  }
}

const handleSubmit = async () => {
  try {
    if (formData.value.id) {
      await request.put('/user/addressBook', formData.value)
      ElMessage.success('更新成功')
    } else {
      await request.post('/user/addressBook', formData.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadAddresses()
  } catch (error) {
    console.error('操作失败:', error)
  }
}
</script>

<template>
  <div class="address-list-page">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>收货地址管理</span>
          <el-button type="primary" :icon="Plus" @click="handleAdd">
            新增地址
          </el-button>
        </div>
      </template>

      <div v-if="addressList.length > 0">
        <div
          v-for="address in addressList"
          :key="address.id"
          class="address-card"
        >
          <div class="address-content">
            <div class="address-main">
              <div class="address-detail">
                <p class="consignee">
                  {{ address.consignee }}
                  <el-tag v-if="address.isDefault === 1" size="small" type="success">
                    默认
                  </el-tag>
                </p>
                <p class="phone">{{ address.phone }}</p>
                <p class="full-address">
                  <el-icon><Location /></el-icon>
                  {{ address.province }} {{ address.city }} {{ address.district }} {{ address.detail }}
                </p>
              </div>
            </div>
            <div class="address-actions">
              <el-button link type="primary" @click="handleEdit(address)">
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button
                v-if="address.isDefault !== 1"
                link
                type="success"
                @click="handleSetDefault(address)"
              >
                设置默认
              </el-button>
              <el-button link type="danger" @click="handleDelete(address)">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="empty-state">
        <p>暂无收货地址</p>
        <el-button type="primary" @click="handleAdd">添加地址</el-button>
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="收货人">
          <el-input v-model="formData.consignee" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="省份">
          <el-input v-model="formData.province" placeholder="请输入省份" />
        </el-form-item>
        <el-form-item label="城市">
          <el-input v-model="formData.city" placeholder="请输入城市" />
        </el-form-item>
        <el-form-item label="区县">
          <el-input v-model="formData.district" placeholder="请输入区县" />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input
            v-model="formData.detail"
            type="textarea"
            :rows="2"
            placeholder="请输入详细地址"
          />
        </el-form-item>
        <el-form-item label="设为默认">
          <el-radio-group v-model="formData.isDefault">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
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
.address-list-page {
  padding: 20px 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.address-card {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 15px;
  transition: box-shadow 0.3s;
}

.address-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.address-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.address-main {
  flex: 1;
}

.consignee {
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 8px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.phone {
  color: #666;
  margin: 0 0 8px;
}

.full-address {
  color: #999;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 5px;
}

.address-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
  color: #999;
}
</style>
