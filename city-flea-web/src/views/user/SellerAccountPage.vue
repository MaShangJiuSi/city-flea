<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElCard, ElRow, ElCol, ElStatistic, ElTable, ElTableColumn } from 'element-plus'
import { Wallet } from '@element-plus/icons-vue'
import request from '@/utils/request'

const accountInfo = ref({
  balance: 0,
  totalIncome: 0,
  totalExpense: 0
})

const flowList = ref<any[]>([])
const loading = ref(false)

onMounted(async () => {
  await loadAccountInfo()
  await loadFlowList()
})

const loadAccountInfo = async () => {
  try {
    const res = await request.get('/user/seller/account/info')
    accountInfo.value = {
      balance: res.data.balance || 0,
      totalIncome: res.data.totalIncome || 0,
      totalExpense: res.data.totalExpense || 0
    }
  } catch (error) {
    console.error('加载账户信息失败:', error)
  }
}

const loadFlowList = async () => {
  loading.value = true
  try {
    const res = await request.get('/user/seller/account/flow')
    flowList.value = res.data.records || []
  } catch (error) {
    console.error('加载资金流水失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="seller-account-page">
    <el-card>
      <template #header>
        <div class="header">
          <span>资金账户</span>
        </div>
      </template>

      <el-row :gutter="20" class="stats-row">
        <el-col :span="8">
          <el-statistic title="账户余额" :value="accountInfo.balance">
            <template #prefix>
              <el-icon size="24" color="#67c23a"><Wallet /></el-icon>
            </template>
            <template #suffix>元</template>
          </el-statistic>
        </el-col>
        <el-col :span="8">
          <el-statistic title="累计收入" :value="accountInfo.totalIncome">
            <template #prefix>
              <el-icon size="24" color="#409eff">¥</el-icon>
            </template>
            <template #suffix>元</template>
          </el-statistic>
        </el-col>
        <el-col :span="8">
          <el-statistic title="累计支出" :value="accountInfo.totalExpense">
            <template #prefix>
              <el-icon size="24" color="#f56c6c">¥</el-icon>
            </template>
            <template #suffix>元</template>
          </el-statistic>
        </el-col>
      </el-row>

      <h3 class="section-title">资金流水</h3>

      <el-table :data="flowList" v-loading="loading" stripe>
        <el-table-column prop="createTime" label="时间" width="180" />
        <el-table-column prop="type" label="类型" width="120">
          <template #default="{ row }">
            <el-tag :type="row.type === 1 ? 'success' : 'danger'">
              {{ row.type === 1 ? '收入' : '支出' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            <span :class="row.type === 1 ? 'income' : 'expense'">
              {{ row.type === 1 ? '+' : '-' }}¥{{ row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" />
        <el-table-column prop="balance" label="余额" width="120">
          <template #default="{ row }">
            ¥{{ row.balance }}
          </template>
        </el-table-column>
      </el-table>

      <div v-if="flowList.length === 0 && !loading" class="empty-state">
        <p>暂无资金流水记录</p>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.seller-account-page {
  padding: 20px 0;
}

.header {
  font-weight: bold;
}

.stats-row {
  margin-bottom: 30px;
  padding: 20px 0;
}

.section-title {
  margin: 30px 0 20px;
  font-size: 18px;
}

.income {
  color: #67c23a;
  font-weight: bold;
}

.expense {
  color: #f56c6c;
  font-weight: bold;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
  color: #999;
}
</style>
