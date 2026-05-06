<template>
  <div class="app-card" :class="{ 'app-card--featured': featured }">
    <div class="card-glow"></div>
    <div class="app-preview">
      <img v-if="app.cover" :src="formatCoverUrl(app.cover)" :alt="app.appName" />
      <div v-else class="app-placeholder">
        <div class="placeholder-icon">
          <span>🤖</span>
        </div>
      </div>
      <div class="app-overlay">
        <a-space>
          <a-button type="primary" @click="handleViewChat" class="cyber-btn">
            <template #icon><CommentOutlined /></template>
            查看对话
          </a-button>
          <a-button v-if="app.deployKey" @click="handleViewWork" class="cyber-btn-secondary">
            <template #icon><EyeOutlined /></template>
            查看作品
          </a-button>
        </a-space>
      </div>
      <div class="preview-decoration"></div>
    </div>
    <div class="app-info">
      <div class="app-info-left">
        <a-avatar :src="app.user?.userAvatar" :size="40" class="cyber-avatar">
          {{ app.user?.userName?.charAt(0) || 'U' }}
        </a-avatar>
      </div>
      <div class="app-info-right">
        <h3 class="app-title">{{ app.appName || '未命名应用' }}</h3>
        <p class="app-author">
          <span class="author-icon">@</span>
          {{ app.user?.userName || (featured ? '官方' : '未知用户') }}
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { formatCoverUrl } from '@/utils/format'
import { CommentOutlined, EyeOutlined } from '@ant-design/icons-vue'

interface Props {
  app: API.AppVO
  featured?: boolean
}

interface Emits {
  (e: 'view-chat', appId: string | number | undefined): void
  (e: 'view-work', app: API.AppVO): void
}

const props = withDefaults(defineProps<Props>(), {
  featured: false,
})

const emit = defineEmits<Emits>()

const handleViewChat = () => {
  emit('view-chat', props.app.id)
}

const handleViewWork = () => {
  emit('view-work', props.app)
}
</script>

<style scoped>
.app-card {
  position: relative;
  background: rgba(15, 15, 25, 0.85);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(0, 255, 157, 0.1);
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  cursor: pointer;
}

.app-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 157, 0.5), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.app-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5), 0 0 30px rgba(0, 255, 157, 0.15);
  border-color: rgba(0, 255, 157, 0.3);
}

.app-card:hover::before {
  opacity: 1;
}

.card-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle at center, rgba(0, 255, 157, 0.1) 0%, transparent 50%);
  opacity: 0;
  transition: opacity 0.5s ease;
  pointer-events: none;
}

.app-card:hover .card-glow {
  opacity: 1;
}

.app-preview {
  height: 180px;
  background: linear-gradient(135deg, rgba(0, 255, 157, 0.05) 0%, rgba(0, 200, 255, 0.05) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  position: relative;
}

.app-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.app-card:hover .app-preview img {
  transform: scale(1.05);
}

.app-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder-icon {
  width: 80px;
  height: 80px;
  background: rgba(0, 255, 157, 0.1);
  border: 1px solid rgba(0, 255, 157, 0.2);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  transition: all 0.3s ease;
}

.app-card:hover .placeholder-icon {
  background: rgba(0, 255, 157, 0.15);
  box-shadow: 0 0 30px rgba(0, 255, 157, 0.2);
  transform: scale(1.05);
}

.app-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(10, 10, 15, 0.85);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.app-card:hover .app-overlay {
  opacity: 1;
}

.preview-decoration {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: linear-gradient(to top, rgba(10, 10, 15, 0.8), transparent);
  pointer-events: none;
}

.cyber-btn {
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%) !important;
  border: none !important;
  color: #0a0a0f !important;
  font-weight: 600 !important;
  box-shadow: 0 0 20px rgba(0, 255, 157, 0.3) !important;
}

.cyber-btn:hover {
  box-shadow: 0 0 30px rgba(0, 255, 157, 0.5) !important;
}

.cyber-btn-secondary {
  background: rgba(255, 255, 255, 0.1) !important;
  border: 1px solid rgba(0, 255, 157, 0.3) !important;
  color: #00ff9d !important;
}

.cyber-btn-secondary:hover {
  background: rgba(0, 255, 157, 0.1) !important;
  border-color: #00ff9d !important;
}

.app-info {
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.app-info-left {
  flex-shrink: 0;
}

.app-info-right {
  flex: 1;
  min-width: 0;
}

.app-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 4px;
  color: white;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-family: 'Noto Sans SC', sans-serif;
}

.app-author {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.5);
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: flex;
  align-items: center;
  gap: 4px;
}

.author-icon {
  color: #00ff9d;
  font-weight: 600;
}

.cyber-avatar {
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%) !important;
  color: #0a0a0f !important;
  font-weight: 700 !important;
  border: 2px solid rgba(0, 255, 157, 0.3);
}

.app-card--featured {
  border-color: rgba(0, 255, 157, 0.3);
}

.app-card--featured::after {
  content: 'FEATURED';
  position: absolute;
  top: 12px;
  right: 12px;
  font-size: 10px;
  font-weight: 700;
  color: #0a0a0f;
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%);
  padding: 4px 8px;
  border-radius: 4px;
  letter-spacing: 1px;
  font-family: 'Orbitron', sans-serif;
  z-index: 1;
}
</style>
