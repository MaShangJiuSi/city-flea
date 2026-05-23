<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElCard, ElRow, ElCol, ElDatePicker, ElButton, ElTable, ElTableColumn } from 'element-plus'
import { Download } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import request from '@/utils/request'

const dateRange = ref<[Date, Date] | null>(null)
const turnoverChartRef = ref<HTMLDivElement>()
const orderChartRef = ref<HTMLDivElement>()
const userChartRef = ref<HTMLDivElement>()
const salesTableData = ref([])

onMounted(() => {
  initCharts()
  loadSalesData()
})

const initCharts = () => {
  if (turnoverChartRef.value) {
    const chart = echarts.init(turnoverChartRef.value)
    chart.setOption({
      title: { text: '营业额统计', left: 'center' },
      tooltip: { trigger: 'axis' },
      xAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      },
      yAxis: { type: 'value' },
      series: [{
        data: [1200, 1800, 1500, 2100, 1900, 2500, 2800],
        type: 'line',
        smooth: true,
        areaStyle: {}
      }]
    })
  }

  if (orderChartRef.value) {
    const chart = echarts.init(orderChartRef.value)
    chart.setOption({
      title: { text: '订单统计', left: 'center' },
      tooltip: { trigger: 'axis' },
      xAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      },
      yAxis: { type: 'value' },
      series: [{
        data: [45, 67, 52, 78, 65, 89, 95],
        type: 'bar'
      }]
    })
  }

  if (userChartRef.value) {
    const chart = echarts.init(userChartRef.value)
    chart.setOption({
      title: { text: '用户增长', left: 'center' },
      tooltip: { trigger: 'axis' },
      xAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      },
      yAxis: { type: 'value' },
      series: [{
        data: [120, 145, 132, 168, 189, 210, 235],
        type: 'line',
        smooth: true
      }]
    })
  }
}

const loadSalesData = async () => {
  try {
    const res = await request.get('/admin/report/top10')
    salesTableData.value = res.data || []
  } catch (error) {
    console.error('加载销售数据失败:', error)
  }
}

const handleExport = () => {
  console.log('导出报表')
}
</script>

<template>
  <div class="report-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据统计</span>
          <div>
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="margin-right: 10px"
            />
            <el-button type="primary" :icon="Download" @click="handleExport">
              导出报表
            </el-button>
          </div>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="12">
          <div ref="turnoverChartRef" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :span="12">
          <div ref="orderChartRef" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="12">
          <div ref="userChartRef" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :span="12">
          <el-card title="商品销量排行">
            <el-table :data="salesTableData" stripe max-height="300">
              <el-table-column prop="name" label="商品名称" />
              <el-table-column prop="sales" label="销量" width="100" />
              <el-table-column prop="amount" label="销售额" width="120">
                <template #default="{ row }">
                  ¥{{ row.amount }}
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<style scoped>
.report-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
