<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
</script>

<template>
  <BasicLayout />
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;600;700&family=Noto+Sans+SC:wght@300;400;500;700&display=swap');

/* ========== CSS 变量 - 赛博朋克主题 ========== */
:root {
  /* 主色调 */
  --color-primary: #00ff9d;
  --color-primary-dark: #00cc7d;
  --color-primary-light: #66ffb3;
  --color-secondary: #00c8ff;
  --color-accent: #ff006e;

  /* 背景色 */
  --bg-dark: #0a0a0f;
  --bg-dark-secondary: #0f0f1a;
  --bg-card: rgba(15, 15, 25, 0.85);
  --bg-card-hover: rgba(20, 20, 35, 0.9);

  /* 文字色 */
  --text-primary: #ffffff;
  --text-secondary: rgba(255, 255, 255, 0.7);
  --text-muted: rgba(255, 255, 255, 0.4);

  /* 边框色 */
  --border-color: rgba(0, 255, 157, 0.15);
  --border-color-hover: rgba(0, 255, 157, 0.3);

  /* 发光效果 */
  --glow-primary: 0 0 20px rgba(0, 255, 157, 0.4);
  --glow-secondary: 0 0 30px rgba(0, 200, 255, 0.3);

  /* 阴影 */
  --shadow-card: 0 8px 32px rgba(0, 0, 0, 0.4);
  --shadow-card-hover: 0 15px 50px rgba(0, 0, 0, 0.5), var(--glow-primary);

  /* 圆角 */
  --radius-sm: 8px;
  --radius-md: 12px;
  --radius-lg: 16px;
  --radius-xl: 24px;

  /* 字体 */
  --font-display: 'Orbitron', sans-serif;
  --font-body: 'Noto Sans SC', -apple-system, BlinkMacSystemFont, sans-serif;
}

/* ========== 全局重置 ========== */
* {
  box-sizing: border-box;
}

body {
  margin: 0;
  padding: 0;
  font-family: var(--font-body);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background: var(--bg-dark);
  color: var(--text-primary);
  overflow-x: hidden;
}

html {
  overflow-x: hidden;
}

/* ========== Ant Design Vue 覆盖 ========== */
.ant-layout {
  background: var(--bg-dark) !important;
}

.ant-layout-content {
  background: var(--bg-dark) !important;
}

/* 菜单 */
.ant-menu {
  background: transparent !important;
  border-bottom: 1px solid var(--border-color) !important;
  color: var(--text-secondary) !important;
}

.ant-menu-horizontal > .ant-menu-item {
  color: var(--text-secondary) !important;
  font-family: var(--font-body);
}

.ant-menu-horizontal > .ant-menu-item:hover,
.ant-menu-horizontal > .ant-menu-item-selected {
  color: var(--color-primary) !important;
  border-bottom-color: var(--color-primary) !important;
}

.ant-menu-horizontal > .ant-menu-item::after {
  border-bottom-color: var(--color-primary) !important;
}

/* 按钮 */
.ant-btn-primary {
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-secondary) 100%) !important;
  border: none !important;
  color: var(--bg-dark) !important;
  font-weight: 600 !important;
  box-shadow: var(--glow-primary) !important;
  transition: all 0.3s ease !important;
}

.ant-btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 0 30px rgba(0, 255, 157, 0.5) !important;
}

/* 输入框 */
.ant-input,
.ant-input-affix-wrapper {
  background: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid var(--border-color) !important;
  color: var(--text-primary) !important;
  border-radius: var(--radius-md) !important;
}

.ant-input::placeholder {
  color: var(--text-muted) !important;
}

.ant-input:hover,
.ant-input-affix-wrapper:hover,
.ant-input-affix-wrapper-focused {
  border-color: var(--color-primary) !important;
  box-shadow: 0 0 0 3px rgba(0, 255, 157, 0.1) !important;
}

/* 卡片 */
.ant-card {
  background: var(--bg-card) !important;
  border: 1px solid var(--border-color) !important;
  border-radius: var(--radius-lg) !important;
  backdrop-filter: blur(20px) !important;
}

/* 下拉菜单 */
.ant-dropdown-menu {
  background: var(--bg-card) !important;
  border: 1px solid var(--border-color) !important;
  backdrop-filter: blur(20px) !important;
}

.ant-dropdown-menu-item {
  color: var(--text-secondary) !important;
}

.ant-dropdown-menu-item:hover {
  background: rgba(0, 255, 157, 0.1) !important;
  color: var(--color-primary) !important;
}

/* 头像 */
.ant-avatar {
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-secondary) 100%) !important;
  color: var(--bg-dark) !important;
  font-weight: 600 !important;
}

/* 复选框 */
.ant-checkbox-checked .ant-checkbox-inner {
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-secondary) 100%) !important;
  border-color: var(--color-primary) !important;
}

/* 标签 */
.ant-tag {
  background: rgba(0, 255, 157, 0.1) !important;
  border: 1px solid var(--color-primary) !important;
  color: var(--color-primary) !important;
}

/* 表格 */
.ant-table {
  background: transparent !important;
  color: var(--text-secondary) !important;
}

.ant-table-thead > tr > th {
  background: rgba(0, 255, 157, 0.05) !important;
  border-bottom: 1px solid var(--border-color) !important;
  color: var(--color-primary) !important;
}

.ant-table-tbody > tr > td {
  border-bottom: 1px solid var(--border-color) !important;
}

.ant-table-tbody > tr:hover > td {
  background: rgba(0, 255, 157, 0.05) !important;
}

/* 分页 */
.ant-pagination-item {
  background: var(--bg-card) !important;
  border-color: var(--border-color) !important;
}

.ant-pagination-item a {
  color: var(--text-secondary) !important;
}

.ant-pagination-item-active {
  border-color: var(--color-primary) !important;
}

.ant-pagination-item-active a {
  color: var(--color-primary) !important;
}

/* 消息提示 */
.ant-message-notice-content {
  background: var(--bg-card) !important;
  border: 1px solid var(--border-color) !important;
  color: var(--text-primary) !important;
  backdrop-filter: blur(20px) !important;
}

/* 模态框 */
.ant-modal-content {
  background: var(--bg-card) !important;
  border: 1px solid var(--border-color) !important;
  backdrop-filter: blur(20px) !important;
}

.ant-modal-header {
  background: transparent !important;
  border-bottom: 1px solid var(--border-color) !important;
}

.ant-modal-title {
  color: var(--text-primary) !important;
}

.ant-modal-close-x {
  color: var(--text-muted) !important;
}

/* 滚动条 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: var(--bg-dark);
}

::-webkit-scrollbar-thumb {
  background: var(--border-color);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: var(--color-primary);
}
</style>
