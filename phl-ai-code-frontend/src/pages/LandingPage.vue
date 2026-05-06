<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser'
import {
  UserOutlined,
  ThunderboltOutlined,
  GlobalOutlined,
  ToolOutlined,
  RocketOutlined,
  TeamOutlined,
  CheckCircleOutlined,
  ArrowRightOutlined
} from '@ant-design/icons-vue'

const router = useRouter()
const loginUserStore = useLoginUserStore()

const userPrompt = ref('')
const creating = ref(false)

const features = [
  {
    icon: ThunderboltOutlined,
    title: 'AI 智能生成',
    desc: '基于大模型，一句话创建完整应用'
  },
  {
    icon: GlobalOutlined,
    title: '多端适配',
    desc: '自动适配 PC、H5、小程序等多端展示'
  },
  {
    icon: ToolOutlined,
    title: '灵活定制',
    desc: '支持自定义样式、功能模块自由组合'
  },
  {
    icon: RocketOutlined,
    title: '一键部署',
    desc: '代码自动构建，实时预览效果'
  }
]

const products = [
  {
    title: '企业官网',
    desc: '打造专业的企业品牌形象',
    color: '#1890ff'
  },
  {
    title: '个人博客',
    desc: '展示您的独特视角与创意',
    color: '#722ed1'
  },
  {
    title: '电商平台',
    desc: '快速搭建在线商城系统',
    color: '#eb2f96'
  },
  {
    title: '管理后台',
    desc: '高效的数据管理与分析系统',
    color: '#13c2c2'
  },
  {
    title: '落地页',
    desc: '高转化的营销展示页面',
    color: '#fa8c16'
  },
  {
    title: '作品集',
    desc: '展示创意与设计作品',
    color: '#52c41a'
  }
]

const stats = [
  { value: '10W+', label: '生成应用' },
  { value: '99.9%', label: '服务可用性' },
  { value: '5s', label: '平均生成速度' },
  { value: '50+', label: '模板类型' }
]

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
    const res = await fetch('/api/app/add', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ initPrompt: userPrompt.value.trim() })
    }).then(r => r.json())

    if (res.code === 0 && res.data) {
      message.success('应用创建成功')
      await router.push(`/app/chat/${res.data}`)
    } else {
      message.error('创建失败：' + (res.message || '未知错误'))
    }
  } catch (error) {
    console.error('创建应用失败：', error)
    message.error('创建失败，请重试')
  } finally {
    creating.value = false
  }
}

const goToLogin = () => {
  router.push('/user/login')
}

const scrollToSection = (id: string) => {
  document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' })
}
</script>

<template>
  <div class="landing-page">
    <!-- 导航栏 -->
    <header class="header">
      <div class="nav-container">
        <div class="logo">
          <img src="@/assets/logo.png" alt="Logo" />
          <span class="logo-text">PHL AI CODE</span>
        </div>
        <nav class="nav-links">
          <a @click="scrollToSection('home')">首页</a>
          <a @click="scrollToSection('features')">核心能力</a>
          <a @click="scrollToSection('products')">产品服务</a>
          <a @click="scrollToSection('about')">关于我们</a>
        </nav>
        <div class="header-actions">
          <template v-if="loginUserStore.loginUser.id">
            <a-button type="primary" @click="router.push('/')">进入控制台</a-button>
          </template>
          <template v-else>
            <a-button @click="goToLogin">登录</a-button>
            <a-button type="primary" @click="goToLogin">立即体验</a-button>
          </template>
        </div>
      </div>
    </header>

    <!-- Hero 区域 -->
    <section id="home" class="hero-section">
      <div class="hero-bg">
        <div class="hero-gradient"></div>
        <div class="hero-particles"></div>
      </div>
      <div class="hero-content">
        <h1 class="hero-title">
          <span class="gradient-text">AI 驱动</span>的应用生成平台
        </h1>
        <p class="hero-subtitle">
          只需描述您的需求，AI 即可为您生成完整的应用程序<br />
          支持网页、Vue、React 等多种项目类型，一键部署上线
        </p>
        <div class="hero-input-wrapper">
          <a-input-search
            v-model:value="userPrompt"
            placeholder="帮我创建一个企业管理官网，包含产品展示、新闻动态、联系我们..."
            size="large"
            class="hero-input"
            @search="createApp"
            :loading="creating"
          >
            <template #enterButton>
              <a-button type="primary" size="large" :loading="creating">
                立即生成
              </a-button>
            </template>
          </a-input-search>
        </div>
        <div class="hero-tags">
          <span><CheckCircleOutlined /> 无需编码</span>
          <span><CheckCircleOutlined /> 实时预览</span>
          <span><CheckCircleOutlined /> 一键部署</span>
        </div>
      </div>
    </section>

    <!-- 数据统计 -->
    <section class="stats-section">
      <div class="stats-container">
        <div v-for="stat in stats" :key="stat.label" class="stat-item">
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
      </div>
    </section>

    <!-- 核心能力 -->
    <section id="features" class="features-section">
      <div class="section-container">
        <div class="section-header">
          <h2 class="section-title">核心能力</h2>
          <p class="section-desc">强大的 AI 能力，简化应用开发全流程</p>
        </div>
        <div class="features-grid">
          <div v-for="feature in features" :key="feature.title" class="feature-card">
            <div class="feature-icon">
              <component :is="feature.icon" />
            </div>
            <h3 class="feature-title">{{ feature.title }}</h3>
            <p class="feature-desc">{{ feature.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 产品服务 -->
    <section id="products" class="products-section">
      <div class="section-container">
        <div class="section-header">
          <h2 class="section-title">支持生成的应用类型</h2>
          <p class="section-desc">多种应用类型覆盖您的业务场景</p>
        </div>
        <div class="products-grid">
          <div
            v-for="product in products"
            :key="product.title"
            class="product-card"
            @click="userPrompt = '创建一个' + product.title + '，' + product.desc"
          >
            <div class="product-accent" :style="{ backgroundColor: product.color }"></div>
            <h3 class="product-title">{{ product.title }}</h3>
            <p class="product-desc">{{ product.desc }}</p>
            <div class="product-action">
              <span>立即创建</span>
              <ArrowRightOutlined />
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 关于我们 -->
    <section id="about" class="about-section">
      <div class="section-container">
        <div class="about-content">
          <div class="about-text">
            <h2 class="section-title">关于 PHL AI CODE</h2>
            <p>
              PHL AI CODE 是一个基于大模型的智能应用生成平台，致力于让每个人都能轻松创建自己的应用程序。
              无需掌握编程技能，只需描述您的想法，AI 就能为您生成完整的、可直接使用的应用程序代码。
            </p>
            <p>
              我们支持多种应用类型的生成，包括企业官网、个人博客、电商平台、管理后台等。
              生成的应用代码结构清晰、样式美观、功能完善，可直接用于生产环境。
            </p>
            <div class="about-features">
              <div class="about-feature-item">
                <TeamOutlined class="about-icon" />
                <div>
                  <h4>技术团队</h4>
                  <p>专业的 AI 与前端技术团队</p>
                </div>
              </div>
              <div class="about-feature-item">
                <RocketOutlined class="about-icon" />
                <div>
                  <h4>快速迭代</h4>
                  <p>持续优化用户体验与生成效果</p>
                </div>
              </div>
            </div>
          </div>
          <div class="about-image">
            <div class="about-image-placeholder">
              <UserOutlined />
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA 区域 -->
    <section class="cta-section">
      <div class="cta-container">
        <h2>准备好开始了吗？</h2>
        <p>立即体验 AI 应用生成，让您的想法变成现实</p>
        <a-button type="primary" size="large" @click="goToLogin">
          立即开始
          <ArrowRightOutlined />
        </a-button>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-container">
        <div class="footer-top">
          <div class="footer-brand">
            <div class="logo">
              <img src="@/assets/logo.png" alt="Logo" />
              <span class="logo-text">PHL AI CODE</span>
            </div>
            <p>让 AI 成为您的开发伙伴</p>
          </div>
          <div class="footer-links">
            <div class="footer-column">
              <h4>产品</h4>
              <a href="javascript:void(0)">核心能力</a>
              <a href="javascript:void(0)">产品服务</a>
              <a href="javascript:void(0)">定价</a>
            </div>
            <div class="footer-column">
              <h4>帮助</h4>
              <a href="javascript:void(0)">使用文档</a>
              <a href="javascript:void(0)">常见问题</a>
              <a href="javascript:void(0)">联系支持</a>
            </div>
            <div class="footer-column">
              <h4>关于</h4>
              <a href="javascript:void(0)">关于我们</a>
              <a href="javascript:void(0)">加入团队</a>
              <a href="javascript:void(0)">更新日志</a>
            </div>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2024 PHL AI CODE. All rights reserved.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.landing-page {
  width: 100%;
  min-height: 100vh;
  background: #0a0a0a;
  color: #fff;
}

/* 导航栏 */
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: rgba(10, 10, 10, 0.9);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo img {
  height: 40px;
  width: 40px;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(135deg, #1890ff, #722ed1);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nav-links {
  display: flex;
  gap: 40px;
}

.nav-links a {
  color: rgba(255, 255, 255, 0.8);
  font-size: 15px;
  cursor: pointer;
  transition: color 0.3s;
}

.nav-links a:hover {
  color: #1890ff;
}

.header-actions {
  display: flex;
  gap: 12px;
}

/* Hero 区域 */
.hero-section {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 120px 24px 80px;
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  inset: 0;
}

.hero-gradient {
  position: absolute;
  inset: 0;
  background: 
    radial-gradient(ellipse 80% 50% at 50% -20%, rgba(24, 144, 255, 0.3), transparent),
    radial-gradient(ellipse 60% 40% at 80% 60%, rgba(114, 46, 209, 0.2), transparent),
    radial-gradient(ellipse 50% 30% at 20% 80%, rgba(19, 194, 194, 0.15), transparent);
}

.hero-particles {
  position: absolute;
  inset: 0;
  background-image: 
    radial-gradient(2px 2px at 20% 30%, rgba(255, 255, 255, 0.3), transparent),
    radial-gradient(2px 2px at 40% 70%, rgba(255, 255, 255, 0.2), transparent),
    radial-gradient(1px 1px at 60% 20%, rgba(255, 255, 255, 0.3), transparent),
    radial-gradient(2px 2px at 80% 50%, rgba(255, 255, 255, 0.2), transparent);
  background-size: 200px 200px;
  animation: particleFloat 20s linear infinite;
}

@keyframes particleFloat {
  0% { transform: translateY(0); }
  100% { transform: translateY(-200px); }
}

.hero-content {
  position: relative;
  z-index: 1;
  max-width: 900px;
  text-align: center;
}

.hero-title {
  font-size: 64px;
  font-weight: 800;
  line-height: 1.1;
  margin-bottom: 24px;
  letter-spacing: -2px;
}

.gradient-text {
  background: linear-gradient(135deg, #1890ff, #722ed1, #13c2c2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 20px;
  color: rgba(255, 255, 255, 0.7);
  line-height: 1.8;
  margin-bottom: 48px;
}

.hero-input-wrapper {
  max-width: 700px;
  margin: 0 auto 32px;
}

.hero-input {
  background: rgba(255, 255, 255, 0.1) !important;
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
  border-radius: 12px !important;
  padding: 8px !important;
}

.hero-input :deep(.ant-input) {
  background: transparent !important;
  color: #fff !important;
  font-size: 16px;
}

.hero-input :deep(.ant-input::placeholder) {
  color: rgba(255, 255, 255, 0.5) !important;
}

.hero-input :deep(.ant-btn-primary) {
  height: 48px !important;
  padding: 0 32px !important;
  border-radius: 8px !important;
  background: linear-gradient(135deg, #1890ff, #722ed1) !important;
  border: none !important;
}

.hero-tags {
  display: flex;
  justify-content: center;
  gap: 32px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
}

.hero-tags span {
  display: flex;
  align-items: center;
  gap: 8px;
}

.hero-tags :deep(.anticon) {
  color: #52c41a;
}

/* 数据统计 */
.stats-section {
  background: rgba(255, 255, 255, 0.02);
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  padding: 48px 0;
}

.stats-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 24px;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 48px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 48px;
  font-weight: 800;
  background: linear-gradient(135deg, #1890ff, #722ed1);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
}

/* 通用区域样式 */
.section-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 100px 24px;
}

.section-header {
  text-align: center;
  margin-bottom: 64px;
}

.section-title {
  font-size: 42px;
  font-weight: 700;
  margin-bottom: 16px;
  background: linear-gradient(135deg, #fff, rgba(255, 255, 255, 0.8));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.section-desc {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.6);
}

/* 核心能力 */
.features-section {
  background: #0a0a0a;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 32px;
}

.feature-card {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  padding: 40px 28px;
  transition: all 0.4s ease;
}

.feature-card:hover {
  background: rgba(255, 255, 255, 0.06);
  border-color: rgba(24, 144, 255, 0.5);
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(24, 144, 255, 0.15);
}

.feature-icon {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, rgba(24, 144, 255, 0.2), rgba(114, 46, 209, 0.2));
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24px;
  font-size: 28px;
  color: #1890ff;
}

.feature-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 12px;
}

.feature-desc {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
  line-height: 1.6;
}

/* 产品服务 */
.products-section {
  background: rgba(255, 255, 255, 0.01);
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.product-card {
  position: relative;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  padding: 36px 28px;
  cursor: pointer;
  transition: all 0.4s ease;
  overflow: hidden;
}

.product-card:hover {
  background: rgba(255, 255, 255, 0.06);
  border-color: rgba(24, 144, 255, 0.4);
  transform: translateY(-4px);
}

.product-accent {
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  opacity: 0.8;
}

.product-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 12px;
  padding-left: 12px;
}

.product-desc {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
  margin-bottom: 24px;
  padding-left: 12px;
}

.product-action {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #1890ff;
  font-size: 14px;
  padding-left: 12px;
  opacity: 0;
  transform: translateX(-10px);
  transition: all 0.3s ease;
}

.product-card:hover .product-action {
  opacity: 1;
  transform: translateX(0);
}

/* 关于我们 */
.about-section {
  background: #0a0a0a;
}

.about-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
}

.about-text p {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.7);
  line-height: 1.8;
  margin-bottom: 24px;
}

.about-features {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  margin-top: 40px;
}

.about-feature-item {
  display: flex;
  gap: 16px;
}

.about-icon {
  font-size: 32px;
  color: #1890ff;
  flex-shrink: 0;
}

.about-feature-item h4 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 4px;
}

.about-feature-item p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
  margin: 0;
}

.about-image-placeholder {
  width: 100%;
  aspect-ratio: 4/3;
  background: linear-gradient(135deg, rgba(24, 144, 255, 0.1), rgba(114, 46, 209, 0.1));
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 80px;
  color: rgba(255, 255, 255, 0.2);
}

/* CTA */
.cta-section {
  background: linear-gradient(135deg, rgba(24, 144, 255, 0.15), rgba(114, 46, 209, 0.15));
  padding: 100px 24px;
  text-align: center;
}

.cta-container h2 {
  font-size: 42px;
  font-weight: 700;
  margin-bottom: 16px;
}

.cta-container p {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: 40px;
}

.cta-container :deep(.ant-btn-primary) {
  height: 52px !important;
  padding: 0 48px !important;
  font-size: 16px !important;
  background: linear-gradient(135deg, #1890ff, #722ed1) !important;
  border: none !important;
  border-radius: 26px !important;
}

/* 页脚 */
.footer {
  background: #050505;
  padding: 80px 0 40px;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.footer-top {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 80px;
  padding-bottom: 60px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.footer-brand p {
  color: rgba(255, 255, 255, 0.5);
  margin-top: 16px;
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 40px;
}

.footer-column h4 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #fff;
}

.footer-column a {
  display: block;
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
  margin-bottom: 12px;
  transition: color 0.3s;
}

.footer-column a:hover {
  color: #1890ff;
}

.footer-bottom {
  padding-top: 40px;
  text-align: center;
  color: rgba(255, 255, 255, 0.4);
  font-size: 14px;
}

/* 响应式 */
@media (max-width: 1024px) {
  .features-grid,
  .products-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .about-content {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 36px;
  }
  
  .hero-subtitle {
    font-size: 16px;
  }
  
  .nav-links {
    display: none;
  }
  
  .stats-container {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .features-grid,
  .products-grid {
    grid-template-columns: 1fr;
  }
  
  .about-features {
    grid-template-columns: 1fr;
  }
}
</style>
