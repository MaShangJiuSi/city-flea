<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { useUserStore } from '@/stores'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const code = ref('')
const loading = ref(false)

const handleLogin = async () => {
  if (!code.value.trim()) {
    ElMessage.warning('请输入登录码')
    return
  }

  loading.value = true
  try {
    const res = await request.post('/user/user/login', { code: code.value })
    userStore.setToken(res.data.token)
    userStore.setUserInfo(res.data)
    ElMessage.success('登录成功')
    
    const redirect = route.query.redirect as string
    router.push(redirect || '/user')
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-box">
      <h1>用户登录</h1>
      <el-form @submit.prevent="handleLogin">
        <el-form-item>
          <el-input
            v-model="code"
            placeholder="请输入登录码"
            size="large"
            prefix-icon="Key"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            native-type="submit"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="tips">
        <p>提示：登录码由系统生成，用于模拟微信授权登录</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.login-box h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
}

.login-button {
  width: 100%;
}

.tips {
  margin-top: 20px;
  text-align: center;
  color: #999;
  font-size: 12px;
}

.tips p {
  margin: 0;
}
</style>
