<template>
  <div id="userLoginPage">
    <!-- 动态代码雨背景 -->
    <div class="code-rain-bg">
      <div v-for="i in 20" :key="i" class="code-column" :style="{ '--delay': i * 0.5 + 's', '--x': (i * 5) % 100 + '%' }">
        <span v-for="j in 30" :key="j" :style="{ '--char-delay': j * 0.1 + 's' }">{{ codeChars[(i * j) % codeChars.length] }}</span>
      </div>
    </div>

    <div class="login-container">
      <!-- 左侧品牌区域 - 赛博朋克风格 -->
      <div class="login-brand">
        <div class="brand-content">
          <div class="logo-container">
            <div class="logo-glow"></div>
            <div class="logo-ring"></div>
            <div class="logo-icon">
              <span class="logo-text">PHL</span>
            </div>
          </div>

          <div class="title-container">
            <h1 class="brand-title">
              <span class="title-line title-line-1">裴恒林</span>
              <span class="title-line title-line-2">AI 应用平台</span>
            </h1>
          </div>

          <p class="brand-subtitle">释放创造力，零代码构建未来</p>

          <div class="brand-features">
            <div class="feature-item" v-for="(feature, index) in features" :key="index" :style="{ '--index': index }">
              <div class="feature-icon">
                <component :is="feature.icon" />
              </div>
              <div class="feature-text">
                <h4>{{ feature.title }}</h4>
                <p>{{ feature.desc }}</p>
              </div>
            </div>
          </div>

          <div class="brand-decoration">
            <div class="deco-line"></div>
            <div class="deco-text">AI CODE GENERATOR</div>
            <div class="deco-line"></div>
          </div>
        </div>
      </div>

      <!-- 右侧表单区域 - 毛玻璃效果 -->
      <div class="login-form-area">
        <div class="login-form-wrapper">
          <div class="form-card">
            <div class="form-glow"></div>

            <div class="form-header">
              <h2>欢迎回来</h2>
              <p>登录您的账户，开始创造之旅</p>
            </div>

            <a-form
              :model="formState"
              name="login"
              autocomplete="off"
              @finish="handleSubmit"
              class="login-form"
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

              <div class="form-options">
                <a-checkbox v-model:checked="rememberMe" class="cyber-checkbox">
                  <span class="checkbox-label">记住我</span>
                </a-checkbox>
                <RouterLink to="/user/register" class="register-link">
                  初次使用？<span class="link-accent">创建账户</span>
                </RouterLink>
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
                      <span>开始创造</span>
                      <ArrowRightOutlined class="btn-icon" />
                    </span>
                  </a-button>
                </a-form-item>
              </div>
            </a-form>
          </div>

          <div class="form-footer">
            <span class="footer-text">登录即表示同意</span>
            <a href="#" class="footer-link">服务条款</a>
            <span class="footer-divider">和</span>
            <a href="#" class="footer-link">隐私政策</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { userLogin } from '@/api/userController'
import { useLoginUserStore } from '@/stores/loginUser'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  UserOutlined,
  LockOutlined,
  ArrowRightOutlined,
  RocketOutlined,
  CodeOutlined,
  ThunderboltOutlined,
} from '@ant-design/icons-vue'

const router = useRouter()
const loginUserStore = useLoginUserStore()

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

const rememberMe = ref(false)
const loading = ref(false)

const codeChars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789{}[]<>/\\|;:=+-*&^%$#@!'

const features = [
  { icon: RocketOutlined, title: '极速生成', desc: '几秒内生成完整应用' },
  { icon: CodeOutlined, title: '零代码', desc: '无需编程经验' },
  { icon: ThunderboltOutlined, title: '智能驱动', desc: 'AI 助力创意实现' },
]

const handleSubmit = async (values: any) => {
  loading.value = true
  try {
    const res = await userLogin(values)
    if (res.data.code === 0 && res.data.data) {
      await loginUserStore.fetchLoginUser()
      message.success('登录成功')
      router.push({ path: '/', replace: true })
    } else {
      message.error('登录失败：' + res.data.message)
    }
  } catch {
    message.error('登录失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;600;700&family=Noto+Sans+SC:wght@300;400;500;700&display=swap');

#userLoginPage {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #0a0a0f;
  font-family: 'Noto Sans SC', 'Orbitron', sans-serif;
  padding: 20px;
  position: relative;
  overflow: hidden;
}

/* 动态代码雨背景 */
.code-rain-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  opacity: 0.15;
  pointer-events: none;
}

.code-column {
  position: absolute;
  top: -100%;
  left: var(--x);
  display: flex;
  flex-direction: column;
  gap: 20px;
  animation: codeFall 15s linear infinite;
  animation-delay: var(--delay);
}

.code-column span {
  font-family: 'Orbitron', monospace;
  font-size: 14px;
  color: #00ff9d;
  text-shadow: 0 0 10px #00ff9d;
  opacity: 0;
  animation: charFade 0.5s ease forwards;
  animation-delay: var(--char-delay);
}

@keyframes codeFall {
  0% { transform: translateY(0); }
  100% { transform: translateY(200vh); }
}

@keyframes charFade {
  0% { opacity: 0; }
  50% { opacity: 1; }
  100% { opacity: 0.3; }
}

/* 主容器 */
.login-container {
  display: flex;
  width: 100%;
  max-width: 1100px;
  min-height: 640px;
  background: rgba(15, 15, 25, 0.8);
  border-radius: 24px;
  border: 1px solid rgba(0, 255, 157, 0.1);
  box-shadow:
    0 0 80px rgba(0, 255, 157, 0.1),
    0 25px 50px -12px rgba(0, 0, 0, 0.5);
  overflow: hidden;
  position: relative;
  z-index: 1;
  backdrop-filter: blur(20px);
}

/* 左侧品牌区域 */
.login-brand {
  flex: 0 0 42%;
  background: linear-gradient(135deg, rgba(0, 255, 157, 0.05) 0%, rgba(0, 200, 255, 0.05) 100%);
  padding: 60px 48px;
  display: flex;
  align-items: center;
  position: relative;
  border-right: 1px solid rgba(0, 255, 157, 0.1);
}

.login-brand::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:
    radial-gradient(ellipse at 20% 80%, rgba(0, 255, 157, 0.1) 0%, transparent 50%),
    radial-gradient(ellipse at 80% 20%, rgba(0, 200, 255, 0.08) 0%, transparent 50%);
  pointer-events: none;
}

.brand-content {
  position: relative;
  z-index: 1;
  width: 100%;
}

/* Logo 样式 */
.logo-container {
  position: relative;
  width: 100px;
  height: 100px;
  margin-bottom: 40px;
}

.logo-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 150px;
  height: 150px;
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
  box-shadow: 0 0 20px #00ff9d;
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
  width: 70px;
  height: 70px;
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%);
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translate(-50%, -50%);
  box-shadow:
    0 0 40px rgba(0, 255, 157, 0.4),
    0 8px 32px rgba(0, 0, 0, 0.3);
}

.logo-text {
  font-size: 22px;
  font-weight: 700;
  color: #0a0a0f;
  font-family: 'Orbitron', sans-serif;
  letter-spacing: 2px;
}

/* 标题样式 */
.title-container {
  margin-bottom: 20px;
}

.brand-title {
  margin: 0;
  line-height: 1.2;
}

.title-line {
  display: block;
}

.title-line-1 {
  font-size: 24px;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.6);
  letter-spacing: 4px;
  margin-bottom: 8px;
}

.title-line-2 {
  font-size: 32px;
  font-weight: 700;
  background: linear-gradient(90deg, #00ff9d 0%, #00c8ff 50%, #00ff9d 100%);
  background-size: 200% auto;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: textShine 3s linear infinite;
  letter-spacing: 2px;
}

@keyframes textShine {
  0% { background-position: 0% center; }
  100% { background-position: 200% center; }
}

.brand-subtitle {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.4);
  margin: 0 0 48px;
  letter-spacing: 1px;
}

/* 特色功能 */
.brand-features {
  display: flex;
  flex-direction: column;
  gap: 28px;
  margin-bottom: 48px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 20px;
  opacity: 0;
  transform: translateX(-20px);
  animation: slideIn 0.6s ease forwards;
  animation-delay: calc(0.3s + var(--index) * 0.15s);
}

@keyframes slideIn {
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.feature-icon {
  width: 48px;
  height: 48px;
  background: rgba(0, 255, 157, 0.1);
  border: 1px solid rgba(0, 255, 157, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #00ff9d;
  font-size: 22px;
  transition: all 0.3s ease;
}

.feature-item:hover .feature-icon {
  background: rgba(0, 255, 157, 0.2);
  box-shadow: 0 0 20px rgba(0, 255, 157, 0.3);
  transform: scale(1.05);
}

.feature-text h4 {
  font-size: 15px;
  font-weight: 600;
  color: white;
  margin: 0 0 4px;
}

.feature-text p {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.4);
  margin: 0;
}

/* 装饰元素 */
.brand-decoration {
  display: flex;
  align-items: center;
  gap: 16px;
}

.deco-line {
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 157, 0.3), transparent);
}

.deco-text {
  font-size: 10px;
  color: rgba(0, 255, 157, 0.4);
  letter-spacing: 3px;
  font-family: 'Orbitron', sans-serif;
}

/* 右侧表单区域 */
.login-form-area {
  flex: 1;
  padding: 60px 56px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-form-wrapper {
  width: 100%;
  max-width: 380px;
}

.form-card {
  position: relative;
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
  margin-bottom: 40px;
}

.form-header h2 {
  font-size: 28px;
  font-weight: 700;
  color: white;
  margin: 0 0 12px;
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
  font-size: 14px !important;
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
  color: rgba(255, 255, 255, 0.3) !important;
}

.cyber-input-wrapper :deep(.ant-input-prefix) {
  margin-right: 10px !important;
}

.cyber-input-wrapper :deep(.ant-input-suffix) {
  margin-left: 10px !important;
}

.cyber-input-wrapper :deep(.ant-input::placeholder) {
  color: rgba(255, 255, 255, 0.3) !important;
}

.cyber-input-wrapper :deep(.ant-input-prefix) {
  margin-right: 10px !important;
}

.cyber-input-wrapper :deep(.ant-input-suffix) {
  margin-left: 10px !important;
}

.cyber-input-icon {
  color: rgba(0, 255, 157, 0.5) !important;
  font-size: 16px !important;
}

.cyber-input-wrapper :deep(.ant-input-password-icon) {
  color: rgba(255, 255, 255, 0.3) !important;
  font-size: 14px !important;
  transition: all 0.3s ease !important;
}

.cyber-input-wrapper :deep(.ant-input-password-icon:hover) {
  color: rgba(0, 255, 157, 0.6) !important;
}

/* ========== 赛博朋克霓虹按钮 ========== */
.cyber-btn-wrapper {
  margin-top: 8px;
}

.cyber-submit-btn {
  position: relative;
  width: 100%;
  height: 54px !important;
  background: linear-gradient(135deg, rgba(0, 255, 157, 0.15) 0%, rgba(0, 200, 255, 0.1) 100%) !important;
  border: 2px solid #00ff9d !important;
  border-radius: 12px !important;
  font-size: 16px !important;
  font-weight: 700 !important;
  color: #00ff9d !important;
  letter-spacing: 4px;
  font-family: 'Orbitron', sans-serif;
  text-transform: uppercase;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94) !important;
  box-shadow: 0 0 20px rgba(0, 255, 157, 0.2), inset 0 0 20px rgba(0, 255, 157, 0.05) !important;
}

.cyber-submit-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 157, 0.4), transparent);
  transition: left 0.6s ease;
}

.cyber-submit-btn::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  background: radial-gradient(circle, rgba(0, 255, 157, 0.6), transparent);
  border-radius: 50%;
  transform: translate(-50%, -50%);
  transition: all 0.5s ease;
}

.cyber-submit-btn:hover {
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%) !important;
  color: #0a0a0f !important;
  box-shadow: 0 0 40px rgba(0, 255, 157, 0.5), 0 0 80px rgba(0, 255, 157, 0.3), inset 0 0 30px rgba(255, 255, 255, 0.2) !important;
  transform: translateY(-3px);
}

.cyber-submit-btn:hover::before {
  left: 100%;
}

.cyber-submit-btn:hover::after {
  width: 300%;
  height: 300%;
}

.cyber-submit-btn:active {
  transform: translateY(-1px);
}

.btn-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  position: relative;
  z-index: 1;
}

.btn-icon {
  font-size: 18px;
  transition: transform 0.3s ease;
}

.cyber-submit-btn:hover .btn-icon {
  transform: translateX(5px);
}

/* 表单选项 */
.form-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 28px;
}

.cyber-checkbox :deep(.ant-checkbox-inner) {
  width: 20px;
  height: 20px;
  border-radius: 6px;
  border: 1px solid rgba(0, 255, 157, 0.3);
  background: rgba(10, 10, 15, 0.8);
}

.cyber-checkbox :deep(.ant-checkbox-checked .ant-checkbox-inner) {
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%);
  border-color: transparent;
  box-shadow: 0 0 10px rgba(0, 255, 157, 0.5);
}

.cyber-checkbox :deep(.ant-checkbox-checked::after) {
  border: none;
}

.checkbox-label {
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
  margin-left: 8px;
}

.register-link {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.4);
  text-decoration: none;
  transition: all 0.2s ease;
}

.register-link:hover {
  color: white;
}

.link-accent {
  color: #00ff9d;
  font-weight: 500;
}

/* 表单底部 */
.form-footer {
  text-align: center;
  margin-top: 24px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.3);
}

.footer-link {
  color: rgba(0, 255, 157, 0.6);
  text-decoration: none;
  transition: color 0.2s ease;
}

.footer-link:hover {
  color: #00ff9d;
}

.footer-divider {
  margin: 0 6px;
}

/* 响应式适配 */
@media (max-width: 960px) {
  .login-container {
    max-width: 800px;
  }

  .login-brand {
    padding: 40px 32px;
  }

  .login-form-area {
    padding: 40px 32px;
  }
}

@media (max-width: 768px) {
  #userLoginPage {
    padding: 16px;
  }

  .login-container {
    flex-direction: column;
    max-width: 440px;
    min-height: auto;
  }

  .login-brand {
    flex: none;
    padding: 48px 32px;
    border-right: none;
    border-bottom: 1px solid rgba(0, 255, 157, 0.1);
  }

  .brand-features {
    display: none;
  }

  .brand-decoration {
    display: none;
  }

  .login-form-area {
    flex: none;
    padding: 40px 24px;
  }

  .form-card {
    padding: 32px 24px;
  }

  .form-header h2 {
    font-size: 24px;
  }

  .title-line-2 {
    font-size: 26px;
  }
}
</style>
