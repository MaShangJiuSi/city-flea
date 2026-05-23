<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElCarousel, ElCarouselItem, ElCard, ElRow, ElCol, ElInput, ElButton } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()
const categories = ref([])
const recommendGoods = ref([])
const searchKeyword = ref('')

onMounted(async () => {
  await loadCategories()
  await loadRecommendGoods()
})

const loadCategories = async () => {
  try {
    const res = await request.get('/user/category/list')
    categories.value = res.data || []
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const loadRecommendGoods = async () => {
  try {
    const res = await request.get('/user/goods/list')
    recommendGoods.value = res.data.records?.slice(0, 8) || []
  } catch (error) {
    console.error('加载推荐商品失败:', error)
  }
}

const handleSearch = () => {
  router.push({
    path: '/user/goods',
    query: { keyword: searchKeyword.value }
  })
}

const goToGoodsDetail = (id: number) => {
  router.push(`/user/goods/${id}`)
}

const goToCategory = (categoryId: number) => {
  router.push({
    path: '/user/goods',
    query: { categoryId: categoryId.toString() }
  })
}
</script>

<template>
  <div class="home-page">
    <div class="search-section">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索商品..."
        size="large"
        class="search-input"
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button :icon="Search" @click="handleSearch" />
        </template>
      </el-input>
    </div>

    <el-carousel height="400px" class="banner-carousel">
      <el-carousel-item>
        <div class="banner-item banner-1">
          <h2>同城跳蚤市场</h2>
          <p>发现身边的二手好物</p>
        </div>
      </el-carousel-item>
      <el-carousel-item>
        <div class="banner-item banner-2">
          <h2>轻松买卖</h2>
          <p>快递配送，安全便捷</p>
        </div>
      </el-carousel-item>
    </el-carousel>

    <div class="category-section">
      <h2 class="section-title">商品分类</h2>
      <el-row :gutter="20">
        <el-col
          v-for="category in categories"
          :key="category.id"
          :span="6"
          :xs="12"
        >
          <el-card
            class="category-card"
            shadow="hover"
            @click="goToCategory(category.id)"
          >
            <div class="category-content">
              <span class="category-name">{{ category.name }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="goods-section">
      <h2 class="section-title">推荐商品</h2>
      <el-row :gutter="20">
        <el-col
          v-for="goods in recommendGoods"
          :key="goods.id"
          :span="6"
          :xs="12"
          :sm="8"
          :md="6"
        >
          <el-card
            class="goods-card"
            shadow="hover"
            @click="goToGoodsDetail(goods.id)"
          >
            <div class="goods-image">
              <img :src="goods.image || '/placeholder.png'" :alt="goods.name" />
            </div>
            <div class="goods-info">
              <h3 class="goods-name">{{ goods.name }}</h3>
              <p class="goods-desc">{{ goods.description }}</p>
              <div class="goods-price">
                <span class="price">¥{{ goods.price }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <div v-if="recommendGoods.length === 0" class="empty-state">
        <p>暂无推荐商品</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-page {
  padding: 20px 0;
}

.search-section {
  max-width: 600px;
  margin: 0 auto 30px;
}

.search-input {
  width: 100%;
}

.banner-carousel {
  margin-bottom: 40px;
}

.banner-item {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #fff;
  text-align: center;
}

.banner-1 {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.banner-2 {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.banner-item h2 {
  font-size: 48px;
  margin: 0 0 20px;
}

.banner-item p {
  font-size: 24px;
  margin: 0;
}

.section-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.category-section {
  margin-bottom: 40px;
}

.category-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.category-card:hover {
  transform: translateY(-5px);
}

.category-content {
  text-align: center;
  padding: 20px;
}

.category-name {
  font-size: 16px;
  color: #333;
}

.goods-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.goods-card:hover {
  transform: translateY(-5px);
}

.goods-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: #f5f5f5;
}

.goods-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.goods-info {
  padding: 10px 0;
}

.goods-name {
  font-size: 16px;
  margin: 0 0 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.goods-desc {
  font-size: 14px;
  color: #999;
  margin: 0 0 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.goods-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 20px;
  color: #f56c6c;
  font-weight: bold;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
  color: #999;
}
</style>
