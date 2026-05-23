<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElImage, ElButton, ElInputNumber, ElMessage, ElCard, ElSkeleton } from 'element-plus'
import { ShoppingCart, Star } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { useCartStore } from '@/stores'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

const goodsId = parseInt(route.params.id as string)
const goods = ref<any>(null)
const loading = ref(false)
const quantity = ref(1)

onMounted(async () => {
  await loadGoodsDetail()
})

const loadGoodsDetail = async () => {
  loading.value = true
  try {
    const res = await request.get(`/user/goods/${goodsId}`)
    goods.value = res.data
  } catch (error) {
    console.error('加载商品详情失败:', error)
  } finally {
    loading.value = false
  }
}

const handleAddToCart = () => {
  if (!goods.value) return
  
  cartStore.addItem({
    goodsId: goods.value.id,
    goodsName: goods.value.name,
    image: goods.value.image,
    price: goods.value.price,
    quantity: quantity.value
  })
  
  ElMessage.success('已添加到购物车')
}

const handleBuyNow = () => {
  if (!goods.value) return
  
  cartStore.clearCart()
  cartStore.addItem({
    goodsId: goods.value.id,
    goodsName: goods.value.name,
    image: goods.value.image,
    price: goods.value.price,
    quantity: quantity.value
  })
  
  router.push('/user/cart')
}
</script>

<template>
  <div class="goods-detail-page">
    <el-card v-loading="loading">
      <el-skeleton :rows="6" animated v-if="loading" />
      
      <div v-else-if="goods" class="goods-detail">
        <el-row :gutter="40">
          <el-col :span="10">
            <el-image
              :src="goods.image || '/placeholder.png'"
              :zoom-rate="1.2"
              :preview-src-list="[goods.image]"
              fit="cover"
              class="goods-image"
            />
          </el-col>
          
          <el-col :span="14">
            <div class="goods-info">
              <h1 class="goods-title">{{ goods.name }}</h1>
              
              <div class="goods-price">
                <span class="current-price">¥{{ goods.price }}</span>
                <span class="original-price" v-if="goods.originalPrice">
                  ¥{{ goods.originalPrice }}
                </span>
              </div>
              
              <div class="goods-stats">
                <span><el-icon><Star /></el-icon> 销量: {{ goods.sales || 0 }}</span>
                <span>库存: {{ goods.stock || 100 }}</span>
              </div>
              
              <div class="goods-description">
                <h3>商品描述</h3>
                <p>{{ goods.description || '暂无描述' }}</p>
              </div>
              
              <div class="goods-meta">
                <p><strong>分类:</strong> {{ goods.categoryName }}</p>
                <p><strong>新旧程度:</strong> {{ goods.condition || '全新' }}</p>
              </div>
              
              <div class="goods-actions">
                <div class="quantity-selector">
                  <span>数量:</span>
                  <el-input-number v-model="quantity" :min="1" :max="99" />
                </div>
                
                <div class="action-buttons">
                  <el-button type="primary" size="large" @click="handleBuyNow">
                    立即购买
                  </el-button>
                  <el-button size="large" @click="handleAddToCart">
                    <el-icon><ShoppingCart /></el-icon>
                    加入购物车
                  </el-button>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <div v-else class="empty-state">
        <p>商品不存在</p>
        <el-button @click="router.push('/user/goods')">返回商品列表</el-button>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.goods-detail-page {
  padding: 20px 0;
}

.goods-detail {
  padding: 20px;
}

.goods-image {
  width: 100%;
  height: 500px;
  border-radius: 8px;
}

.goods-info {
  padding: 20px 0;
}

.goods-title {
  font-size: 28px;
  font-weight: bold;
  margin: 0 0 20px;
  color: #333;
}

.goods-price {
  margin-bottom: 20px;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 4px;
}

.current-price {
  font-size: 36px;
  color: #f56c6c;
  font-weight: bold;
  margin-right: 20px;
}

.original-price {
  font-size: 20px;
  color: #999;
  text-decoration: line-through;
}

.goods-stats {
  display: flex;
  gap: 30px;
  margin-bottom: 30px;
  color: #666;
}

.goods-stats span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.goods-description {
  margin-bottom: 20px;
}

.goods-description h3 {
  font-size: 18px;
  margin: 0 0 10px;
}

.goods-description p {
  color: #666;
  line-height: 1.6;
}

.goods-meta {
  margin-bottom: 30px;
  color: #666;
}

.goods-meta p {
  margin: 10px 0;
}

.goods-actions {
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.quantity-selector {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}

.quantity-selector span {
  font-size: 16px;
  color: #666;
}

.action-buttons {
  display: flex;
  gap: 15px;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
  color: #999;
}
</style>
