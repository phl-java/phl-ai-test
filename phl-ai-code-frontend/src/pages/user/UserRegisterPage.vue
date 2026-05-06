<template>
  <div id="userRegisterPage">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="deco-glow deco-glow-1"></div>
      <div class="deco-glow deco-glow-2"></div>
      <div class="deco-grid"></div>
    </div>

    <div class="register-container">
      <!-- 左侧品牌区 -->
      <div class="brand-section">
        <div class="brand-content">
          <div class="logo-container">
            <div class="logo-glow"></div>
            <div class="logo-ring"></div>
            <div class="logo-icon">
              <span class="logo-text">PHL</span>
            </div>
          </div>

          <h1 class="brand-title">
            <span class="title-line">加入</span>
            <span class="title-line title-accent">裴恒林 AI 平台</span>
          </h1>

          <p class="brand-subtitle">释放创造力，零代码构建未来<br />与 AI 一起创造无限可能</p>

          <div class="brand-features">
            <div class="feature-item">
              <span class="feature-check">✓</span>
              <span>免费注册，即刻开始</span>
            </div>
            <div class="feature-item">
              <span class="feature-check">✓</span>
              <span>一句话生成完整应用</span>
            </div>
            <div class="feature-item">
              <span class="feature-check">✓</span>
              <span>一键部署，随时分享</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧表单区 -->
      <div class="form-section">
        <div class="form-card">
          <div class="form-glow"></div>

          <div class="form-header">
            <h2>创建账户</h2>
            <p>开始你的 AI 创作之旅</p>
          </div>

          <a-form
            :model="formState"
            name="register"
            autocomplete="off"
            @finish="handleSubmit"
            class="register-form"
          >
            <div class="cyber-input-wrapper">
              <a-form-item
                name="userAccount"
                :rules="[{ required: true, message: '请输入账号' }]"
                style="margin-bottom: 0"
              >
                <a-input
                  v-model:value="formState.userAccount"
                  placeholder="请输入账号"
                  size="large"
                  class="cyber-input"
                >
                  <template #prefix>
                    <UserOutlined class="cyber-input-icon" />
                  </template>
                </a-input>
              </a-form-item>
            </div>

            <div class="cyber-input-wrapper">
              <a-form-item
                name="userPassword"
                :rules="[
                  { required: true, message: '请输入密码' },
                  { min: 8, message: '密码长度不能小于 8 位' },
                ]"
                style="margin-bottom: 0"
              >
                <a-input-password
                  v-model:value="formState.userPassword"
                  placeholder="请输入密码"
                  size="large"
                  class="cyber-input"
                >
                  <template #prefix>
                    <LockOutlined class="cyber-input-icon" />
                  </template>
                </a-input-password>
              </a-form-item>
            </div>

            <div class="cyber-input-wrapper">
              <a-form-item
                name="checkPassword"
                :rules="[
                  { required: true, message: '请确认密码' },
                  { min: 8, message: '密码长度不能小于 8 位' },
                  { validator: validateCheckPassword },
                ]"
                style="margin-bottom: 0"
              >
                <a-input-password
                  v-model:value="formState.checkPassword"
                  placeholder="请确认密码"
                  size="large"
                  class="cyber-input"
                >
                  <template #prefix>
                    <LockOutlined class="cyber-input-icon" />
                  </template>
                </a-input-password>
              </a-form-item>
            </div>

            <div class="form-options">
              <span class="login-text">已有账号？</span>
              <RouterLink to="/user/login" class="login-link">立即登录</RouterLink>
            </div>

            <div class="cyber-btn-wrapper">
              <a-form-item style="margin-bottom: 0">
                <a-button
                  type="primary"
                  html-type="submit"
                  size="large"
                  class="cyber-submit-btn"
                  :loading="loading"
                >
                  <span class="btn-content">
                    <span>注册</span>
                    <ArrowRightOutlined class="btn-icon" />
                  </span>
                </a-button>
              </a-form-item>
            </div>
          </a-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { userRegister } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import { UserOutlined, LockOutlined, ArrowRightOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

const loading = ref(false)

const validateCheckPassword = (rule: unknown, value: string, callback: (error?: Error) => void) => {
  if (value && value !== formState.userPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const handleSubmit = async (values: API.UserRegisterRequest) => {
  loading.value = true
  try {
    const res = await userRegister(values)
    if (res.data.code === 0) {
      message.success('注册成功')
      router.push({ path: '/user/login', replace: true })
    } else {
      message.error('注册失败：' + res.data.message)
    }
  } catch {
    message.error('注册失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
#userRegisterPage {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #0a0a0f;
  font-family: 'Noto Sans SC', -apple-system, BlinkMacSystemFont, sans-serif;
  padding: 40px 20px;
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.deco-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
}

.deco-glow-1 {
  width: 500px;
  height: 500px;
  background: rgba(0, 255, 157, 0.1);
  top: -20%;
  left: -10%;
}

.deco-glow-2 {
  width: 400px;
  height: 400px;
  background: rgba(0, 200, 255, 0.08);
  bottom: -15%;
  right: -5%;
}

.deco-grid {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image:
    linear-gradient(rgba(0, 255, 157, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 255, 157, 0.03) 1px, transparent 1px);
  background-size: 60px 60px;
}

/* 主容器 */
.register-container {
  display: flex;
  width: 100%;
  max-width: 1000px;
  min-height: 620px;
  background: rgba(15, 15, 25, 0.8);
  border-radius: 24px;
  border: 1px solid rgba(0, 255, 157, 0.1);
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
  overflow: hidden;
  position: relative;
  z-index: 1;
  backdrop-filter: blur(20px);
}

/* 左侧品牌区 */
.brand-section {
  flex: 0 0 40%;
  background: linear-gradient(135deg, rgba(0, 255, 157, 0.08) 0%, rgba(0, 200, 255, 0.05) 100%);
  padding: 60px 48px;
  display: flex;
  align-items: center;
  border-right: 1px solid rgba(0, 255, 157, 0.1);
}

.brand-content {
  width: 100%;
}

.logo-container {
  position: relative;
  width: 90px;
  height: 90px;
  margin-bottom: 36px;
}

.logo-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 140px;
  height: 140px;
  background: radial-gradient(circle, rgba(0, 255, 157, 0.3) 0%, transparent 70%);
  transform: translate(-50%, -50%);
  animation: glowPulse 3s ease-in-out infinite;
}

.logo-ring {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border: 2px solid rgba(0, 255, 157, 0.3);
  border-radius: 50%;
  animation: ringRotate 20s linear infinite;
}

.logo-ring::before {
  content: '';
  position: absolute;
  top: -4px;
  left: 50%;
  width: 8px;
  height: 8px;
  background: #00ff9d;
  border-radius: 50%;
  box-shadow: 0 0 15px #00ff9d;
  transform: translateX(-50%);
}

@keyframes glowPulse {
  0%, 100% { opacity: 0.5; transform: translate(-50%, -50%) scale(1); }
  50% { opacity: 0.8; transform: translate(-50%, -50%) scale(1.1); }
}

@keyframes ringRotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.logo-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translate(-50%, -50%);
  box-shadow: 0 0 30px rgba(0, 255, 157, 0.4);
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  color: #0a0a0f;
  font-family: 'Orbitron', sans-serif;
  letter-spacing: 2px;
}

.brand-title {
  margin: 0 0 20px;
  line-height: 1.3;
}

.title-line {
  display: block;
  font-size: 22px;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.6);
}

.title-accent {
  font-size: 28px;
  font-weight: 700;
  background: linear-gradient(90deg, #00ff9d 0%, #00c8ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.brand-subtitle {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
  line-height: 1.8;
  margin: 0 0 40px;
}

.brand-features {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.7);
}

.feature-check {
  width: 24px;
  height: 24px;
  background: rgba(0, 255, 157, 0.15);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #00ff9d;
  font-size: 12px;
  font-weight: 700;
}

/* 右侧表单区 */
.form-section {
  flex: 1;
  padding: 60px 56px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.form-card {
  position: relative;
  width: 100%;
  max-width: 380px;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 20px;
  padding: 48px 40px;
  backdrop-filter: blur(10px);
}

.form-glow {
  position: absolute;
  top: -1px;
  left: 50%;
  width: 60%;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 157, 0.5), transparent);
  transform: translateX(-50%);
}

.form-header {
  text-align: center;
  margin-bottom: 36px;
}

.form-header h2 {
  font-size: 26px;
  font-weight: 700;
  color: white;
  margin: 0 0 10px;
  letter-spacing: 2px;
}

.form-header p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.4);
  margin: 0;
}

/* ========== 最小化输入框 ========== */
.cyber-input-wrapper {
  position: relative;
  margin-bottom: 20px;
}

.cyber-input-wrapper :deep(.ant-input-affix-wrapper) {
  height: 48px !important;
  border-radius: 6px !important;
  background: rgba(255, 255, 255, 0.03) !important;
  border: none !important;
  box-shadow: none !important;
  padding: 0 12px !important;
  transition: all 0.3s ease !important;
}

.cyber-input-wrapper :deep(.ant-input-affix-wrapper:hover),
.cyber-input-wrapper :deep(.ant-input-affix-wrapper:focus),
.cyber-input-wrapper :deep(.ant-input-affix-wrapper-focused) {
  border: none !important;
  box-shadow: none !important;
  background: rgba(255, 255, 255, 0.05) !important;
}

.cyber-input-wrapper :deep(.ant-input-affix-wrapper .ant-input) {
  border: none !important;
  box-shadow: none !important;
  background: transparent !important;
}

.cyber-input-wrapper :deep(.ant-input-affix-wrapper .ant-input:focus) {
  border: none !important;
  box-shadow: none !important;
  outline: none !important;
}

.cyber-input-wrapper :deep(.ant-input) {
  font-size: 15px !important;
  color: #ffffff !important;
  background: transparent !important;
  border: none !important;
  box-shadow: none !important;
  outline: none !important;
}

.cyber-input-wrapper :deep(.ant-input:focus) {
  border: none !important;
  box-shadow: none !important;
  outline: none !important;
}

.cyber-input-wrapper :deep(.ant-input::placeholder) {
  color: rgba(255, 255, 255, 0.35) !important;
}

.cyber-input-wrapper :deep(.ant-input-prefix) {
  margin-right: 10px !important;
}

.cyber-input-wrapper :deep(.ant-input-suffix) {
  margin-left: 10px !important;
}

.cyber-input-wrapper :deep(.ant-input::placeholder) {
  color: rgba(255, 255, 255, 0.35) !important;
}

.cyber-input-wrapper :deep(.ant-input-prefix) {
  margin-right: 10px !important;
}

.cyber-input-wrapper :deep(.ant-input-suffix) {
  margin-left: 10px !important;
}

.cyber-input-icon {
  color: rgba(255, 255, 255, 0.4) !important;
  font-size: 16px !important;
}

.cyber-input-wrapper :deep(.ant-input-password-icon) {
  color: rgba(255, 255, 255, 0.4) !important;
  font-size: 16px !important;
}

.cyber-input-wrapper :deep(.ant-input-password-icon:hover) {
  color: rgba(0, 255, 157, 0.6) !important;
}

/* ========== 最小化按钮 ========== */
.cyber-btn-wrapper {
  margin-top: 8px;
}

.cyber-submit-btn {
  width: 100%;
  height: 48px !important;
  background: rgba(0, 255, 157, 0.1) !important;
  border: 1px solid rgba(0, 255, 157, 0.3) !important;
  border-radius: 6px !important;
  font-size: 15px !important;
  font-weight: 600 !important;
  color: rgba(0, 255, 157, 0.9) !important;
  letter-spacing: 2px;
  transition: all 0.3s ease !important;
}

.cyber-submit-btn:hover {
  background: rgba(0, 255, 157, 0.2) !important;
  border-color: rgba(0, 255, 157, 0.5) !important;
  color: #00ff9d !important;
}

.btn-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.btn-icon {
  font-size: 16px;
}

/* 表单选项 */
.form-options {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  margin-bottom: 24px;
}

.login-text {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.4);
}

.login-link {
  font-size: 14px;
  color: #00ff9d;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.2s ease;
}

.login-link:hover {
  color: #66ffb3;
  text-decoration: underline;
}

/* 响应式 */
@media (max-width: 860px) {
  .register-container {
    flex-direction: column;
    max-width: 480px;
  }

  .brand-section {
    flex: none;
    padding: 40px 32px;
    border-right: none;
    border-bottom: 1px solid rgba(0, 255, 157, 0.1);
  }

  .form-section {
    flex: none;
    padding: 40px 32px;
  }

  .brand-features {
    display: none;
  }
}
</style>
