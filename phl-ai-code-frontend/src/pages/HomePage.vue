<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser'
import { addApp, listMyAppVoByPage, listGoodAppVoByPage } from '@/api/appController'
import { getDeployUrl } from '@/config/env'
import AppCard from '@/components/AppCard.vue'

const router = useRouter()
const loginUserStore = useLoginUserStore()

const userPrompt = ref('')
const modelType = ref('qwen')
const creating = ref(false)

const myApps = ref<API.AppVO[]>([])
const myAppsPage = reactive({
  current: 1,
  pageSize: 6,
  total: 0,
})

const featuredApps = ref<API.AppVO[]>([])
const featuredAppsPage = reactive({
  current: 1,
  pageSize: 6,
  total: 0,
})

const setPrompt = (prompt: string) => {
  userPrompt.value = prompt
}

const createApp = async () => {
  if (!userPrompt.value.trim()) {
    message.warning('请输入应用描述')
    return
  }

  if (!loginUserStore.loginUser.id) {
    message.warning('请先登录')
    await router.push('/user/login')
    return
  }

  creating.value = true
  try {
    const res = await addApp({
      initPrompt: userPrompt.value.trim(),
      modelType: modelType.value,
    })

    if (res.data.code === 0 && res.data.data) {
      message.success('应用创建成功')
      const appId = res.data.data
      await router.push(`/app/chat/${appId}`)
    } else {
      message.error('创建失败：' + (res.data.message || '未知错误'))
    }
  } catch (error) {
    console.error('创建应用失败：', error)
    message.error('创建失败，请重试')
  } finally {
    creating.value = false
  }
}

const loadMyApps = async () => {
  if (!loginUserStore.loginUser.id) {
    return
  }

  try {
    const res = await listMyAppVoByPage({
      pageNum: myAppsPage.current,
      pageSize: myAppsPage.pageSize,
      sortField: 'createTime',
      sortOrder: 'desc',
    })

    if (res.data.code === 0 && res.data.data) {
      myApps.value = res.data.data.records || []
      myAppsPage.total = res.data.data.totalRow || 0
    }
  } catch (error) {
    console.error('加载我的应用失败：', error)
  }
}

const loadFeaturedApps = async () => {
  try {
    const res = await listGoodAppVoByPage({
      pageNum: featuredAppsPage.current,
      pageSize: featuredAppsPage.pageSize,
      sortField: 'createTime',
      sortOrder: 'desc',
    })

    if (res.data.code === 0 && res.data.data) {
      featuredApps.value = res.data.data.records || []
      featuredAppsPage.total = res.data.data.totalRow || 0
    }
  } catch (error) {
    console.error('加载精选应用失败：', error)
  }
}

const viewChat = (appId: string | number | undefined) => {
  if (appId) {
    router.push(`/app/chat/${appId}?view=1`)
  }
}

const viewWork = (app: API.AppVO) => {
  if (app.deployKey) {
    const url = getDeployUrl(app.deployKey)
    window.open(url, '_blank')
  }
}

onMounted(() => {
  loadMyApps()
  loadFeaturedApps()

  const handleMouseMove = (e: MouseEvent) => {
    const { clientX, clientY } = e
    const { innerWidth, innerHeight } = window
    const x = (clientX / innerWidth) * 100
    const y = (clientY / innerHeight) * 100

    document.documentElement.style.setProperty('--mouse-x', `${x}%`)
    document.documentElement.style.setProperty('--mouse-y', `${y}%`)
  }

  document.addEventListener('mousemove', handleMouseMove)

  return () => {
    document.removeEventListener('mousemove', handleMouseMove)
  }
})
</script>

<template>
  <div id="homePage">
    <!-- 动态代码雨背景 -->
    <div class="code-rain-bg">
      <div v-for="i in 15" :key="i" class="code-column" :style="{ '--delay': i * 0.7 + 's', '--x': (i * 6) % 100 + '%' }">
        <span v-for="j in 25" :key="j" :style="{ '--char-delay': j * 0.08 + 's' }">{{ codeChars[(i * j) % codeChars.length] }}</span>
      </div>
    </div>

    <div class="container">
      <!-- 网站标题和描述 -->
      <div class="hero-section">
        <div class="hero-glow"></div>
        <div class="hero-content">
          <h1 class="hero-title">
            <span class="title-line">裴恒林</span>
            <span class="title-line title-accent">AI 应用生成平台</span>
          </h1>
          <p class="hero-description">释放创造力，零代码构建未来<br />一句话轻松创建网站应用</p>
        </div>
      </div>

      <!-- 用户提示词输入框 -->
      <div class="input-section">
        <div class="input-wrapper">
          <div class="input-glow"></div>
          <a-textarea
            v-model:value="userPrompt"
            placeholder="描述你想要的应用，比如：帮我创建一个个人博客网站..."
            :rows="4"
            :maxlength="1000"
            class="prompt-input"
          />
          <div class="input-actions">
            <a-button type="primary" size="large" @click="createApp" :loading="creating" class="submit-btn">
              <template #icon>
                <span class="btn-icon">↑</span>
              </template>
              <span>创建应用</span>
            </a-button>
          </div>
        </div>
      </div>

      <!-- AI模型选择 -->
      <div class="model-select-section">
        <span class="model-select-label">AI模型</span>
        <a-select v-model:value="modelType" style="width: 200px" class="model-select">
          <a-select-option value="qwen">通义千问 Qwen</a-select-option>
          <a-select-option value="deepseek">DeepSeek</a-select-option>
        </a-select>
      </div>

      <!-- 快捷按钮 -->
      <div class="quick-actions">
        <a-button
          v-for="(action, index) in quickActions"
          :key="index"
          @click="setPrompt(action.prompt)"
          class="quick-btn"
        >
          <span class="quick-icon">{{ action.icon }}</span>
          <span class="quick-text">{{ action.label }}</span>
        </a-button>
      </div>

      <!-- 我的作品 -->
      <div class="section" v-if="myApps.length > 0">
        <div class="section-header">
          <div class="section-line"></div>
          <h2 class="section-title">我的作品</h2>
          <div class="section-line"></div>
        </div>
        <div class="app-grid">
          <AppCard
            v-for="app in myApps"
            :key="app.id"
            :app="app"
            @view-chat="viewChat"
            @view-work="viewWork"
          />
        </div>
        <div class="pagination-wrapper">
          <a-pagination
            v-model:current="myAppsPage.current"
            v-model:page-size="myAppsPage.pageSize"
            :total="myAppsPage.total"
            :show-size-changer="false"
            :show-total="(total: number) => `共 ${total} 个应用`"
            @change="loadMyApps"
          />
        </div>
      </div>

      <!-- 精选案例 -->
      <div class="section">
        <div class="section-header">
          <div class="section-line"></div>
          <h2 class="section-title">精选案例</h2>
          <div class="section-line"></div>
        </div>
        <div class="featured-grid">
          <AppCard
            v-for="app in featuredApps"
            :key="app.id"
            :app="app"
            :featured="true"
            @view-chat="viewChat"
            @view-work="viewWork"
          />
        </div>
        <div class="pagination-wrapper">
          <a-pagination
            v-model:current="featuredAppsPage.current"
            v-model:page-size="featuredAppsPage.pageSize"
            :total="featuredAppsPage.total"
            :show-size-changer="false"
            :show-total="(total: number) => `共 ${total} 个案例`"
            @change="loadFeaturedApps"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
const codeChars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ{}[]<>/\\|;:=+-*&^%$#@!'

const quickActions = [
  { icon: '📝', label: '个人博客', prompt: '创建一个现代化的个人博客网站，包含文章列表、详情页、分类标签、搜索功能、评论系统和个人简介页面。' },
  { icon: '🏢', label: '企业官网', prompt: '设计一个专业的企业官网，包含公司介绍、产品服务展示、新闻资讯、联系我们等页面。' },
  { icon: '🛒', label: '在线商城', prompt: '构建一个功能完整的在线商城，包含商品展示、购物车、用户注册登录、订单管理等功能。' },
  { icon: '🎨', label: '作品展示', prompt: '制作一个精美的作品展示网站，适合设计师、摄影师、艺术家等创作者。' },
]

export default {
  data() {
    return {
      codeChars,
      quickActions,
    }
  },
}
</script>

<style scoped>
#homePage {
  width: 100%;
  margin: 0;
  padding: 0;
  min-height: 100vh;
  background: #0a0a0f;
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
  opacity: 0.08;
  pointer-events: none;
}

.code-column {
  position: absolute;
  top: -100%;
  left: var(--x);
  display: flex;
  flex-direction: column;
  gap: 16px;
  animation: codeFall 20s linear infinite;
  animation-delay: var(--delay);
}

.code-column span {
  font-family: 'Orbitron', monospace;
  font-size: 12px;
  color: #00ff9d;
  text-shadow: 0 0 8px #00ff9d;
  opacity: 0;
  animation: charFade 0.5s ease forwards;
  animation-delay: var(--char-delay);
}

@keyframes codeFall {
  0% { transform: translateY(0); }
  100% { transform: translateY(250vh); }
}

@keyframes charFade {
  0% { opacity: 0; }
  50% { opacity: 1; }
  100% { opacity: 0.3; }
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  position: relative;
  z-index: 2;
  width: 100%;
  box-sizing: border-box;
}

/* 英雄区域 */
.hero-section {
  text-align: center;
  padding: 80px 0 60px;
  margin-bottom: 40px;
  position: relative;
}

.hero-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 600px;
  height: 400px;
  background: radial-gradient(ellipse at center, rgba(0, 255, 157, 0.15) 0%, transparent 70%);
  transform: translate(-50%, -50%);
  animation: glowPulse 4s ease-in-out infinite;
  pointer-events: none;
}

@keyframes glowPulse {
  0%, 100% { opacity: 0.5; transform: translate(-50%, -50%) scale(1); }
  50% { opacity: 0.8; transform: translate(-50%, -50%) scale(1.1); }
}

.hero-content {
  position: relative;
  z-index: 1;
}

.hero-title {
  margin: 0 0 24px;
  line-height: 1.3;
}

.title-line {
  display: block;
  font-family: 'Noto Sans SC', sans-serif;
}

.title-line:first-child {
  font-size: 28px;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.6);
  letter-spacing: 8px;
}

.title-accent {
  font-size: 52px;
  font-weight: 700;
  background: linear-gradient(90deg, #00ff9d 0%, #00c8ff 50%, #00ff9d 100%);
  background-size: 200% auto;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: textShine 4s linear infinite;
  letter-spacing: 4px;
}

@keyframes textShine {
  0% { background-position: 0% center; }
  100% { background-position: 200% center; }
}

.hero-description {
  font-size: 18px;
  margin: 0;
  color: rgba(255, 255, 255, 0.5);
  line-height: 1.8;
  letter-spacing: 1px;
}

/* 输入区域 */
.input-section {
  margin: 0 auto 32px;
  max-width: 800px;
}

.input-wrapper {
  position: relative;
  background: rgba(15, 15, 25, 0.8);
  border: 1px solid rgba(0, 255, 157, 0.15);
  border-radius: 20px;
  padding: 8px;
  backdrop-filter: blur(20px);
  transition: all 0.3s ease;
}

.input-wrapper:focus-within {
  border-color: rgba(0, 255, 157, 0.4);
  box-shadow: 0 0 40px rgba(0, 255, 157, 0.1);
}

.input-glow {
  position: absolute;
  top: -1px;
  left: 50%;
  width: 60%;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 157, 0.5), transparent);
  transform: translateX(-50%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.input-wrapper:focus-within .input-glow {
  opacity: 1;
}

.prompt-input {
  background: transparent !important;
  border: none !important;
  font-size: 16px !important;
  color: white !important;
  padding: 16px 20px !important;
  border-radius: 16px !important;
}

.prompt-input::placeholder {
  color: rgba(255, 255, 255, 0.3) !important;
}

.prompt-input:focus {
  box-shadow: none !important;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
  padding: 8px 12px;
}

.submit-btn {
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%) !important;
  border: none !important;
  color: #0a0a0f !important;
  font-weight: 600 !important;
  height: 44px !important;
  padding: 0 32px !important;
  border-radius: 12px !important;
  font-size: 15px !important;
  box-shadow: 0 0 25px rgba(0, 255, 157, 0.3) !important;
  transition: all 0.3s ease !important;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 0 40px rgba(0, 255, 157, 0.5) !important;
}

.btn-icon {
  font-size: 18px;
  margin-right: 4px;
}

/* 模型选择 */
.model-select-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 32px;
}

.model-select-label {
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  font-weight: 500;
}

.model-select {
  background: rgba(15, 15, 25, 0.8) !important;
  border: 1px solid rgba(0, 255, 157, 0.2) !important;
  border-radius: 10px !important;
}

/* 快捷按钮 */
.quick-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-bottom: 80px;
  flex-wrap: wrap;
}

.quick-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 24px !important;
  height: auto !important;
  background: rgba(15, 15, 25, 0.6) !important;
  border: 1px solid rgba(0, 255, 157, 0.15) !important;
  border-radius: 50px !important;
  color: rgba(255, 255, 255, 0.7) !important;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease !important;
}

.quick-btn:hover {
  background: rgba(0, 255, 157, 0.1) !important;
  border-color: rgba(0, 255, 157, 0.4) !important;
  color: #00ff9d !important;
  transform: translateY(-3px);
  box-shadow: 0 0 30px rgba(0, 255, 157, 0.15);
}

.quick-icon {
  font-size: 18px;
}

.quick-text {
  font-size: 14px;
  font-weight: 500;
}

/* 区域标题 */
.section {
  margin-bottom: 80px;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 40px;
}

.section-line {
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 157, 0.3), transparent);
}

.section-title {
  font-size: 24px;
  font-weight: 600;
  color: white;
  margin: 0;
  letter-spacing: 4px;
  font-family: 'Noto Sans SC', sans-serif;
  white-space: nowrap;
}

/* 应用网格 */
.app-grid,
.featured-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

/* 分页 */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-title {
    padding: 0 20px;
  }

  .title-line:first-child {
    font-size: 20px;
    letter-spacing: 4px;
  }

  .title-accent {
    font-size: 32px;
  }

  .hero-description {
    font-size: 14px;
  }

  .quick-actions {
    gap: 12px;
  }

  .quick-btn {
    padding: 10px 16px !important;
    font-size: 13px;
  }

  .app-grid,
  .featured-grid {
    grid-template-columns: 1fr;
  }

  .section-title {
    font-size: 20px;
  }
}
</style>
