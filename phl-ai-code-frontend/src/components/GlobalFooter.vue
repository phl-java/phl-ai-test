<template>
  <a-layout-footer class="footer">
    <div class="footer-decoration"></div>
    <div class="footer-content">
      <div class="footer-brand">
        <div class="footer-logo">
          <span class="logo-text">PHL</span>
        </div>
        <div class="footer-info">
          <p class="footer-title">裴恒林 AI 应用平台</p>
          <p class="footer-desc">释放创造力，零代码构建未来</p>
        </div>
      </div>
      <div class="footer-divider"></div>
      <div class="footer-bottom">
        <p class="copyright">
          <span class="copyright-symbol">©</span> 2026 PHL-AI-CODE. All rights reserved.
        </p>
        <p class="tech-stack">
          <span class="tech-label">POWERED BY</span>
          <span class="tech-value">Vue 3 + LangChain4j + AI</span>
        </p>
      </div>
    </div>
  </a-layout-footer>
</template>

<script setup lang="ts">
</script>

<style scoped>
.footer {
  position: relative;
  background: rgba(10, 10, 15, 0.95) !important;
  backdrop-filter: blur(20px);
  border-top: 1px solid rgba(0, 255, 157, 0.1);
  padding: 48px 24px 24px !important;
  margin-top: 80px;
  overflow: hidden;
}

.footer-decoration {
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60%;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 157, 0.5), transparent);
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}

.footer-brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-bottom: 32px;
}

.footer-logo {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 0 20px rgba(0, 255, 157, 0.3);
}

.logo-text {
  font-size: 12px;
  font-weight: 700;
  color: #0a0a0f;
  font-family: 'Orbitron', sans-serif;
  letter-spacing: 1px;
}

.footer-info {
  text-align: left;
}

.footer-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: white;
  font-family: 'Noto Sans SC', sans-serif;
}

.footer-desc {
  margin: 4px 0 0;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.4);
}

.footer-divider {
  width: 100%;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 157, 0.2), transparent);
  margin-bottom: 24px;
}

.footer-bottom {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.copyright {
  margin: 0;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.4);
  font-family: 'Noto Sans SC', sans-serif;
}

.copyright-symbol {
  margin-right: 4px;
}

.tech-stack {
  margin: 0;
  font-size: 11px;
  color: rgba(0, 255, 157, 0.5);
  letter-spacing: 2px;
  font-family: 'Orbitron', sans-serif;
  display: flex;
  align-items: center;
  gap: 8px;
}

.tech-label {
  color: rgba(255, 255, 255, 0.3);
}

.tech-value {
  color: #00ff9d;
}
</style>
