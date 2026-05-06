<template>
  <div id="userManagePage">
    <!-- 动态代码雨背景 -->
    <div class="code-rain-bg">
      <div v-for="i in 15" :key="i" class="code-column" :style="{ '--delay': i * 0.7 + 's', '--x': (i * 6) % 100 + '%' }">
        <span v-for="j in 25" :key="j" :style="{ '--char-delay': j * 0.08 + 's' }">{{ codeChars[(i * j) % codeChars.length] }}</span>
      </div>
    </div>

    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">用户管理</h1>
      <p class="page-desc">管理平台上的所有用户</p>
    </div>

    <!-- 搜索表单 -->
    <div class="search-section">
      <a-form layout="inline" :model="searchParams" @finish="doSearch" class="search-form">
        <a-form-item label="账号">
          <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" class="cyber-input" />
        </a-form-item>
        <a-form-item label="用户名">
          <a-input v-model:value="searchParams.userName" placeholder="输入用户名" class="cyber-input" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" class="cyber-btn-primary">
            <template #icon><SearchOutlined /></template>
            搜索
          </a-button>
        </a-form-item>
      </a-form>
    </div>

    <a-divider class="cyber-divider" />

    <!-- 表格 -->
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="doTableChange"
      :scroll="{ x: 1200 }"
      class="cyber-table"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <a-avatar :src="record.userAvatar" :size="48" class="user-avatar">
            {{ record.userName?.charAt(0) || 'U' }}
          </a-avatar>
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <a-tag :color="record.userRole === 'admin' ? 'green' : 'blue'" class="role-tag">
            {{ record.userRole === 'admin' ? '管理员' : '普通用户' }}
          </a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'userProfile'">
          <span class="profile-text">{{ record.userProfile || '暂无' }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          <span class="time-value">{{ formatTime(record.createTime) }}</span>
        </template>
        <template v-else-if="column.key === 'action'">
          <a-popconfirm title="确定要删除这个用户吗？" @confirm="doDelete(record.id)">
            <a-button danger size="small" class="danger-btn">
              <template #icon><DeleteOutlined /></template>
              删除
            </a-button>
          </a-popconfirm>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { deleteUser, listUserVoByPage } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { SearchOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import { formatTime } from '@/utils/time'

const codeChars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789{}[]<>/\\|;:=+-*&^%$#@!'

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 80,
    fixed: 'left',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    width: 120,
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    width: 120,
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
    width: 80,
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
    width: 200,
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
    width: 100,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '操作',
    key: 'action',
    width: 100,
    fixed: 'right',
  },
]

// 展示的数据
const data = ref<API.UserVO[]>([])
const total = ref(0)

// 搜索条件
const searchParams = reactive<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

// 获取数据
const fetchData = async () => {
  const res = await listUserVoByPage({
    ...searchParams,
  })
  if (res.data.data) {
    data.value = res.data.data.records ?? []
    total.value = res.data.data.totalRow ?? 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
}

// 分页参数
const pagination = computed(() => {
  return {
    current: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条`,
  }
})

// 表格分页变化时的操作
const doTableChange = (page: { current: number; pageSize: number }) => {
  searchParams.pageNum = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

// 搜索数据
const doSearch = () => {
  // 重置页码
  searchParams.pageNum = 1
  fetchData()
}

// 删除数据
const doDelete = async (id: string) => {
  if (!id) {
    return
  }
  const res = await deleteUser({ id })
  if (res.data.code === 0) {
    message.success('删除成功')
    // 刷新数据
    fetchData()
  } else {
    message.error('删除失败')
  }
}

// 页面加载时请求一次
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
#userManagePage {
  padding: 24px;
  background: #0a0a0f;
  min-height: 100%;
  position: relative;
}

/* 动态代码雨背景 */
.code-rain-bg {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  opacity: 0.15;
  pointer-events: none;
  z-index: 0;
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

/* 页面标题 */
.page-header {
  margin-bottom: 32px;
  position: relative;
  z-index: 1;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: white;
  margin: 0 0 8px;
  font-family: 'Noto Sans SC', sans-serif;
}

.page-desc {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
  margin: 0;
}

/* 搜索区域 */
.search-section {
  margin-bottom: 24px;
  position: relative;
  z-index: 1;
}

.search-form :deep(.ant-form-item-label > label) {
  color: rgba(255, 255, 255, 0.7) !important;
  font-size: 14px;
}

.cyber-input {
  background: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(0, 255, 157, 0.15) !important;
  color: white !important;
  border-radius: 8px !important;
  min-width: 160px;
}

.cyber-input::placeholder {
  color: rgba(255, 255, 255, 0.3) !important;
}

.cyber-input:focus {
  border-color: rgba(0, 255, 157, 0.4) !important;
  box-shadow: 0 0 0 3px rgba(0, 255, 157, 0.1) !important;
}

.cyber-divider {
  border-color: rgba(0, 255, 157, 0.1) !important;
  margin: 0 0 24px;
}

/* 表格样式 */
.cyber-table :deep(.ant-table) {
  background: rgba(15, 15, 25, 0.6) !important;
  border-radius: 12px;
  overflow: hidden;
}

.cyber-table :deep(.ant-table-thead > tr > th) {
  background: rgba(0, 255, 157, 0.08) !important;
  border-bottom: 1px solid rgba(0, 255, 157, 0.15) !important;
  color: #00ff9d !important;
  font-weight: 600;
  font-size: 13px;
  letter-spacing: 1px;
}

.cyber-table :deep(.ant-table-tbody > tr > td) {
  border-bottom: 1px solid rgba(0, 255, 157, 0.08) !important;
  color: rgba(255, 255, 255, 0.8);
}

.cyber-table :deep(.ant-table-tbody > tr:hover > td) {
  background: rgba(0, 255, 157, 0.05) !important;
}

/* 固定列样式（第一列和最后一列） */
.cyber-table :deep(.ant-table-cell-fix-left),
.cyber-table :deep(.ant-table-cell-fix-right) {
  background: rgba(15, 15, 25, 0.8) !important;
}

.cyber-table :deep(.ant-table-tbody > tr:hover .ant-table-cell-fix-left),
.cyber-table :deep(.ant-table-tbody > tr:hover .ant-table-cell-fix-right) {
  background: rgba(0, 255, 157, 0.08) !important;
}

.user-avatar {
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%) !important;
  color: #0a0a0f !important;
  font-weight: 700 !important;
}

.role-tag {
  background: rgba(0, 200, 255, 0.1) !important;
  border: 1px solid rgba(0, 200, 255, 0.3) !important;
  color: #00c8ff !important;
}

.profile-text {
  color: rgba(255, 255, 255, 0.6);
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
}

.time-value {
  color: rgba(255, 255, 255, 0.6);
  font-size: 13px;
}

/* 按钮样式 */
.cyber-btn-primary {
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%) !important;
  border: none !important;
  color: #0a0a0f !important;
  font-weight: 600 !important;
  box-shadow: 0 0 20px rgba(0, 255, 157, 0.3) !important;
}

.cyber-btn-primary:hover {
  box-shadow: 0 0 30px rgba(0, 255, 157, 0.5) !important;
}

.danger-btn {
  background: rgba(255, 0, 110, 0.1) !important;
  border: 1px solid rgba(255, 0, 110, 0.3) !important;
  color: #ff006e !important;
}

.danger-btn:hover {
  background: rgba(255, 0, 110, 0.2) !important;
  border-color: #ff006e !important;
}

/* 分页 */
.cyber-table :deep(.ant-pagination-item) {
  background: rgba(15, 15, 25, 0.8) !important;
  border-color: rgba(0, 255, 157, 0.15) !important;
}

.cyber-table :deep(.ant-pagination-item a) {
  color: rgba(255, 255, 255, 0.7) !important;
}

.cyber-table :deep(.ant-pagination-item-active) {
  border-color: #00ff9d !important;
}

.cyber-table :deep(.ant-pagination-item-active a) {
  color: #00ff9d !important;
}

.cyber-table :deep(.ant-pagination-prev .ant-pagination-item-link),
.cyber-table :deep(.ant-pagination-next .ant-pagination-item-link) {
  background: rgba(15, 15, 25, 0.8) !important;
  border-color: rgba(0, 255, 157, 0.15) !important;
  color: rgba(255, 255, 255, 0.7) !important;
}

.cyber-table :deep(.ant-pagination-options .ant-select-selector) {
  background: rgba(15, 15, 25, 0.8) !important;
  border-color: rgba(0, 255, 157, 0.15) !important;
  color: white !important;
}
</style>
