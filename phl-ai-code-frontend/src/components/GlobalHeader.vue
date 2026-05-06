<template>
  <a-layout-header class="header">
    <div class="header-bg"></div>
    <div class="header-content">
      <a-row :wrap="false">
        <!-- 左侧：Logo和标题 -->
        <a-col flex="200px">
          <RouterLink to="/">
            <div class="header-left">
              <div class="logo-container">
                <div class="logo-glow"></div>
                <div class="logo-icon">
                  <span class="logo-text">PHL</span>
                </div>
              </div>
              <h1 class="site-title">
                <span class="title-main">裴恒林</span>
                <span class="title-accent">AI 应用平台</span>
              </h1>
            </div>
          </RouterLink>
        </a-col>

        <!-- 中间：导航菜单 -->
        <a-col flex="auto" class="nav-col">
          <div class="nav-links">
            <RouterLink to="/" class="nav-link" :class="{ active: selectedKeys[0] === '/' }">
              <HomeOutlined class="nav-icon" />
              <span>主页</span>
            </RouterLink>
            <RouterLink
              v-if="loginUserStore.loginUser.userRole === 'admin'"
              to="/admin/userManage"
              class="nav-link"
              :class="{ active: selectedKeys[0] === '/admin/userManage' }"
            >
              <UserOutlined class="nav-icon" />
              <span>用户管理</span>
            </RouterLink>
            <RouterLink
              v-if="loginUserStore.loginUser.userRole === 'admin'"
              to="/admin/appManage"
              class="nav-link"
              :class="{ active: selectedKeys[0] === '/admin/appManage' }"
            >
              <AppstoreOutlined class="nav-icon" />
              <span>应用管理</span>
            </RouterLink>
          </div>
        </a-col>

        <!-- 右侧：用户操作区域 -->
        <a-col flex="200px">
          <div class="user-login-status">
            <div v-if="loginUserStore.loginUser.id" class="user-info">
              <a-dropdown>
                <a-space class="user-avatar-wrapper">
                  <a-avatar :src="loginUserStore.loginUser.userAvatar" class="user-avatar">
                    {{ loginUserStore.loginUser.userName?.charAt(0) || 'U' }}
                  </a-avatar>
                  <span class="user-name">{{ loginUserStore.loginUser.userName ?? '无名' }}</span>
                  <DownOutlined class="dropdown-icon" />
                </a-space>
                <template #overlay>
                  <a-menu class="cyber-dropdown">
                    <a-menu-item @click="doLogout" class="logout-item">
                      <LogoutOutlined />
                      <span>退出登录</span>
                    </a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown>
            </div>
            <div v-else>
              <RouterLink to="/user/login" class="login-btn">
                <span>登录</span>
                <LoginOutlined />
              </RouterLink>
            </div>
          </div>
        </a-col>
      </a-row>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import { userLogout } from '@/api/userController.ts'
import { LogoutOutlined, HomeOutlined, LoginOutlined, DownOutlined, UserOutlined, AppstoreOutlined } from '@ant-design/icons-vue'

const loginUserStore = useLoginUserStore()
const router = useRouter()

const selectedKeys = ref<string[]>(['/'])
router.afterEach((to) => {
  selectedKeys.value = [to.path]
})

const doLogout = async () => {
  const res = await userLogout()
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({ userName: '未登录' })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败：' + res.data.message)
  }
}
</script>

<style scoped>
.header {
  position: relative;
  background: rgba(10, 10, 15, 0.95) !important;
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(0, 255, 157, 0.1);
  padding: 0 24px !important;
  height: 72px;
}

.header-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(90deg, rgba(0, 255, 157, 0.03) 0%, transparent 50%, rgba(0, 200, 255, 0.03) 100%);
  pointer-events: none;
}

.header-content {
  position: relative;
  z-index: 1;
  height: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
  height: 100%;
}

.logo-container {
  position: relative;
  width: 48px;
  height: 48px;
}

.logo-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 80px;
  height: 80px;
  background: radial-gradient(circle, rgba(0, 255, 157, 0.3) 0%, transparent 70%);
  transform: translate(-50%, -50%);
  animation: glowPulse 3s ease-in-out infinite;
}

.logo-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translate(-50%, -50%);
  box-shadow: 0 0 20px rgba(0, 255, 157, 0.4);
}

.logo-text {
  font-size: 14px;
  font-weight: 700;
  color: #0a0a0f;
  font-family: 'Orbitron', sans-serif;
  letter-spacing: 1px;
}

@keyframes glowPulse {
  0%, 100% { opacity: 0.5; transform: translate(-50%, -50%) scale(1); }
  50% { opacity: 0.8; transform: translate(-50%, -50%) scale(1.2); }
}

.site-title {
  margin: 0;
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.title-main {
  font-size: 12px;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.5);
  font-family: 'Noto Sans SC', sans-serif;
  letter-spacing: 2px;
}

.title-accent {
  font-size: 16px;
  font-weight: 700;
  background: linear-gradient(90deg, #00ff9d 0%, #00c8ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-family: 'Noto Sans SC', sans-serif;
}

.nav-col {
  display: flex;
  align-items: center;
  overflow: visible !important;
  flex: auto !important;
  justify-content: center !important;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 8px;
  height: 70px;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 8px;
  height: 70px;
  padding: 0 20px;
  color: rgba(255, 255, 255, 0.6) !important;
  font-size: 14px;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.3s ease;
  border-bottom: 2px solid transparent;
}

.nav-link:hover {
  color: #00ff9d !important;
  border-bottom-color: rgba(0, 255, 157, 0.3);
}

.nav-link.active {
  color: #00ff9d !important;
  border-bottom-color: #00ff9d;
}

.nav-icon {
  font-size: 16px;
}

.user-login-status {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  height: 100%;
}

.user-info {
  cursor: pointer;
}

.user-avatar-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.user-avatar-wrapper:hover {
  background: rgba(0, 255, 157, 0.1);
}

.user-avatar {
  width: 36px !important;
  height: 36px !important;
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%) !important;
  color: #0a0a0f !important;
  font-weight: 700 !important;
  font-size: 14px;
}

.user-name {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}

.dropdown-icon {
  color: rgba(255, 255, 255, 0.4);
  font-size: 10px;
}

.cyber-dropdown {
  background: rgba(15, 15, 25, 0.95) !important;
  border: 1px solid rgba(0, 255, 157, 0.15) !important;
  backdrop-filter: blur(20px) !important;
  border-radius: 8px !important;
  padding: 4px !important;
}

.logout-item {
  color: rgba(255, 255, 255, 0.7) !important;
  border-radius: 6px !important;
  display: flex !important;
  align-items: center !important;
  gap: 8px !important;
}

.logout-item:hover {
  background: rgba(255, 0, 110, 0.1) !important;
  color: #ff006e !important;
}

.login-btn {
  position: relative;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 28px;
  background: linear-gradient(135deg, rgba(0, 255, 157, 0.1) 0%, rgba(0, 200, 255, 0.05) 100%) !important;
  border: 2px solid rgba(0, 255, 157, 0.4) !important;
  border-radius: 50px !important;
  color: #00ff9d !important;
  font-weight: 600;
  font-size: 14px;
  letter-spacing: 2px;
  text-decoration: none;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  overflow: hidden;
  font-family: 'Orbitron', sans-serif;
  text-transform: uppercase;
  box-shadow: 0 0 20px rgba(0, 255, 157, 0.2), inset 0 0 20px rgba(0, 255, 157, 0.05);
}

.login-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 157, 0.3), transparent);
  transition: left 0.5s ease;
}

.login-btn::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  background: radial-gradient(circle, rgba(0, 255, 157, 0.4), transparent);
  border-radius: 50%;
  transform: translate(-50%, -50%);
  transition: all 0.4s ease;
}

.login-btn:hover {
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%) !important;
  border-color: #00ff9d !important;
  color: #0a0a0f !important;
  box-shadow: 0 0 30px rgba(0, 255, 157, 0.5), 0 0 60px rgba(0, 255, 157, 0.3);
  transform: translateY(-2px);
}

.login-btn:hover::before {
  left: 100%;
}

.login-btn:hover::after {
  width: 200%;
  height: 200%;
}

.login-btn span {
  position: relative;
  z-index: 1;
}
</style>
