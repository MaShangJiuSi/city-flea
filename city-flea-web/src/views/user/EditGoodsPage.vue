<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElCard, ElForm, ElFormItem, ElInput, ElInputNumber, ElSelect, ElOption, ElButton, ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()

const goodsId = parseInt(route.params.id as string)
const formData = ref({
  id: goodsId,
  name: '',
  categoryId: '',
  price: 0,
  description: '',
  condition: '全新'
})

const categories = ref<any[]>([])
const loading = ref(false)
const submitting = ref(false)

onMounted(async () => {
  await loadCategories()
  await loadGoodsDetail()
})

const loadCategories = async () => {
  try {
    const res = await request.get('/user/category/list')
    categories.value = res.data || []
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const loadGoodsDetail = async () => {
  loading.value = true
  try {
    const res = await request.get(`/user/seller/goods/${goodsId}`)
    formData.value = res.data
  } catch (error) {
    console.error('加载商品详情失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSubmit = async () => {
  if (!formData.value.name) {
    ElMessage.warning('请输入商品名称')
    return
  }
  if (!formData.value.categoryId) {
    ElMessage.warning('请选择商品分类')
    return
  }
  if (formData.value.price <= 0) {
    ElMessage.warning('请输入商品价格')
    return
  }

  submitting.value = true
  try {
    await request.put('/user/seller/goods', formData.value)
    ElMessage.success('更新成功')
    router.push('/user/seller/goods')
  } catch (error) {
    console.error('更新失败:', error)
  } finally {
    submitting.value = false
  }
}

const handleCancel = () => {
  router.back()
}
</script>

<template>
  <div class="edit-goods-page">
    <el-card v-loading="loading">
      <template #header>
        <span>编辑商品</span>
      </template>

      <el-form :model="formData" label-width="100px">
        <el-form-item label="商品名称">
          <el-input v-model="formData.name" placeholder="请输入商品名称" />
        </el-form-item>

        <el-form-item label="商品分类">
          <el-select v-model="formData.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="商品价格">
          <el-input-number
            v-model="formData.price"
            :min="0.01"
            :precision="2"
            :step="1"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="新旧程度">
          <el-select v-model="formData.condition" placeholder="请选择新旧程度" style="width: 100%">
            <el-option label="全新" value="全新" />
            <el-option label="几乎全新" value="几乎全新" />
            <el-option label="轻微使用痕迹" value="轻微使用痕迹" />
            <el-option label="明显使用痕迹" value="明显使用痕迹" />
          </el-select>
        </el-form-item>

        <el-form-item label="商品描述">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="4"
            placeholder="请输入商品描述"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">
            保存修改
          </el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.edit-goods-page {
  padding: 20px 0;
}
</style>
