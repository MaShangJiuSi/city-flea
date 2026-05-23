<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElCard, ElButton, ElInputNumber, ElEmpty, ElMessage } from 'element-plus'
import { Delete, ShoppingCart } from '@element-plus/icons-vue'
import { useCartStore } from '@/stores'

const router = useRouter()
const cartStore = useCartStore()

const handleQuantityChange = (goodsId: number, quantity: number) => {
  cartStore.updateQuantity(goodsId, quantity)
}

const handleRemoveItem = (goodsId: number) => {
  cartStore.removeItem(goodsId)
  ElMessage.success('已移除')
}

const handleCheckout = () => {
  if (cartStore.items.length === 0) {
    ElMessage.warning('购物车是空的')
    return
  }
  router.push('/user/address')
}
</script>

<template>
  <div class="cart-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的购物车</span>
          <span class="item-count">共 {{ cartStore.totalCount }} 件商品</span>
        </div>
      </template>

      <div v-if="cartStore.items.length > 0">
        <div class="cart-items">
          <div
            v-for="item in cartStore.items"
            :key="item.goodsId"
            class="cart-item"
          >
            <div class="item-image">
              <img :src="item.image || '/placeholder.png'" :alt="item.goodsName" />
            </div>
            
            <div class="item-info">
              <h3 class="item-name">{{ item.goodsName }}</h3>
            </div>
            
            <div class="item-price">
              ¥{{ item.price }}
            </div>
            
            <div class="item-quantity">
              <el-input-number
                :model-value="item.quantity"
                :min="1"
                :max="99"
                size="small"
                @update:model-value="(val: number) => handleQuantityChange(item.goodsId, val)"
              />
            </div>
            
            <div class="item-subtotal">
              ¥{{ (item.price * item.quantity).toFixed(2) }}
            </div>
            
            <div class="item-actions">
              <el-button
                type="danger"
                link
                @click="handleRemoveItem(item.goodsId)"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>
        </div>

        <div class="cart-footer">
          <div class="cart-summary">
            <div class="total-info">
              <span>商品总价：</span>
              <span class="total-price">¥{{ cartStore.totalPrice.toFixed(2) }}</span>
            </div>
            <div class="total-count">
              共 {{ cartStore.totalCount }} 件
            </div>
          </div>
          
          <div class="cart-actions">
            <el-button size="large" @click="router.push('/user/goods')">
              继续购物
            </el-button>
            <el-button
              type="primary"
              size="large"
              @click="handleCheckout"
            >
              去结算
            </el-button>
          </div>
        </div>
      </div>

      <el-empty v-else>
        <template #image>
          <el-icon :size="80" color="#ddd"><ShoppingCart /></el-icon>
        </template>
        <template #description>
          <p>购物车是空的</p>
        </template>
        <el-button type="primary" @click="router.push('/user/goods')">
          去逛逛
        </el-button>
      </el-empty>
    </el-card>
  </div>
</template>

<style scoped>
.cart-page {
  padding: 20px 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-count {
  color: #999;
  font-size: 14px;
}

.cart-items {
  border-bottom: 1px solid #eee;
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid #f5f5f5;
}

.item-image {
  width: 100px;
  height: 100px;
  margin-right: 20px;
  border-radius: 4px;
  overflow: hidden;
  background: #f5f5f5;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
  margin-right: 20px;
}

.item-name {
  font-size: 16px;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.item-price {
  width: 100px;
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
  margin-right: 20px;
}

.item-quantity {
  width: 120px;
  margin-right: 20px;
}

.item-subtotal {
  width: 100px;
  color: #f56c6c;
  font-size: 20px;
  font-weight: bold;
  margin-right: 20px;
}

.item-actions {
  width: 50px;
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
  margin-top: 20px;
}

.cart-summary {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.total-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.total-price {
  font-size: 24px;
  color: #f56c6c;
  font-weight: bold;
}

.total-count {
  color: #999;
  font-size: 14px;
}

.cart-actions {
  display: flex;
  gap: 15px;
}
</style>
