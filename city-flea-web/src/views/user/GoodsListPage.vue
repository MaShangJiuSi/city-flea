<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElRow, ElCol, ElCard, ElPagination, ElSelect, ElOption, ElInput, ElButton, ElSkeleton } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()

const goodsList = ref([])
const categories = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)
const selectedCategory = ref('')
const searchKeyword = ref('')

onMounted(async () => {
  await loadCategories()
  
  if (route.query.keyword) {
    searchKeyword.value = route.query.keyword as string
  }
  if (route.query.categoryId) {
    selectedCategory.value = route.query.categoryId as string
  }
  
  await loadGoodsList()
})

const loadCategories = async () => {
  try {
    const res = await request.get('/user/category/list')
    categories.value = res.data || []
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const loadGoodsList = async () => {
  loading.value = true
  try {
    const res = await request.get('/user/goods/list', {
      params: {
        page: currentPage.value,
        pageSize: pageSize.value,
        categoryId: selectedCategory.value,
        keyword: searchKeyword.value
      }
    })
    goodsList.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载商品列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadGoodsList()
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  loadGoodsList()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const goToGoodsDetail = (id: number) => {
  router.push(`/user/goods/${id}`)
}

watch(
  () => route.query,
  () => {
    if (route.query.keyword) {
      searchKeyword.value = route.query.keyword as string
    }
    if (route.query.categoryId) {
      selectedCategory.value = route.query.categoryId as string
    }
    loadGoodsList()
  }
)
</script>

<template>
  <div class="goods-list-page">
    <div class="filter-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索商品"
        style="width: 200px"
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button :icon="Search" @click="handleSearch" />
        </template>
      </el-input>
      
      <el-select
        v-model="selectedCategory"
        placeholder="选择分类"
        style="width: 150px"
        clearable
        @change="handleSearch"
      >
        <el-option
          v-for="category in categories"
          :key="category.id"
          :label="category.name"
          :value="category.id"
        />
      </el-select>
    </div>

    <el-row :gutter="20" v-loading="loading">
      <el-col
        v-for="goods in goodsList"
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
            <div class="goods-footer">
              <span class="price">¥{{ goods.price }}</span>
              <span class="sales">销量: {{ goods.sales || 0 }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div v-if="goodsList.length === 0 && !loading" class="empty-state">
      <p>暂无商品</p>
    </div>

    <div class="pagination" v-if="total > 0">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<style scoped>
.goods-list-page {
  padding: 20px 0;
}

.filter-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
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

.goods-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 20px;
  color: #f56c6c;
  font-weight: bold;
}

.sales {
  font-size: 12px;
  color: #999;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
  color: #999;
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>
