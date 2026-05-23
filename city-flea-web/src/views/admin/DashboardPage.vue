<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElCard, ElRow, ElCol, ElStatistic } from 'element-plus'
import { Money, ShoppingCart, User, Goods } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import request from '@/utils/request'

const dashboardData = ref({
  turnover: 0,
  orderCount: 0,
  userCount: 0,
  goodsCount: 0
})

const orderChartRef = ref<HTMLDivElement>()
const userChartRef = ref<HTMLDivElement>()

onMounted(async () => {
  await loadDashboardData()
  initCharts()
})

const loadDashboardData = async () => {
  try {
    const res = await request.get('/admin/workspace/businessData')
    dashboardData.value = {
      turnover: res.data.turnover || 0,
      orderCount: res.data.orderCount || 0,
      userCount: res.data.userCount || 0,
      goodsCount: res.data.goodsCount || 0
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const initCharts = () => {
  if (orderChartRef.value) {
    const orderChart = echarts.init(orderChartRef.value)
    orderChart.setOption({
      title: { text: '订单统计' },
      tooltip: {},
      xAxis: { type: 'category', data: ['待付款', '待发货', '已发货', '已完成', '已取消'] },
      yAxis: { type: 'value' },
      series: [{ type: 'bar', data: [10, 20, 30, 40, 15] }]
    })
  }

  if (userChartRef.value) {
    const userChart = echarts.init(userChartRef.value)
    userChart.setOption({
      title: { text: '用户增长' },
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'] },
      yAxis: { type: 'value' },
      series: [{ type: 'line', data: [120, 200, 150, 80, 70, 110, 130] }]
    })
  }
}
</script>

<template>
  <div class="dashboard">
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="今日营业额" :value="dashboardData.turnover">
            <template #prefix>
              <el-icon size="24" color="#409eff"><Money /></el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="今日订单" :value="dashboardData.orderCount">
            <template #prefix>
              <el-icon size="24" color="#67c23a"><ShoppingCart /></el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="用户总数" :value="dashboardData.userCount">
            <template #prefix>
              <el-icon size="24" color="#e6a23c"><User /></el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="商品总数" :value="dashboardData.goodsCount">
            <template #prefix>
              <el-icon size="24" color="#f56c6c"><Goods /></el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <div ref="orderChartRef" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div ref="userChartRef" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-cards {
  margin-bottom: 20px;
}
</style>
