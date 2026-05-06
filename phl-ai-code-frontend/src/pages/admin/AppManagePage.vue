<template>
  <div id="appManagePage">
    <!-- 动态代码雨背景 -->
    <div class="code-rain-bg">
      <div v-for="i in 15" :key="i" class="code-column" :style="{ '--delay': i * 0.7 + 's', '--x': (i * 6) % 100 + '%' }">
        <span v-for="j in 25" :key="j" :style="{ '--char-delay': j * 0.08 + 's' }">{{ codeChars[(i * j) % codeChars.length] }}</span>
      </div>
    </div>

    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">应用管理</h1>
      <p class="page-desc">管理平台上的所有 AI 生成应用</p>
    </div>

    <!-- 搜索表单 -->
    <div class="search-section">
      <a-form layout="inline" :model="searchParams" @finish="doSearch" class="search-form">
        <a-form-item label="应用名称">
          <a-input v-model:value="searchParams.appName" placeholder="输入应用名称" class="cyber-input" />
        </a-form-item>
        <a-form-item label="创建者">
          <a-input v-model:value="searchParams.userId" placeholder="输入用户ID" class="cyber-input" />
        </a-form-item>
        <a-form-item label="生成类型">
          <a-select
            v-model:value="searchParams.codeGenType"
            placeholder="选择生成类型"
            style="width: 150px"
            class="cyber-select"
          >
            <a-select-option value="">全部</a-select-option>
            <a-select-option
              v-for="option in CODE_GEN_TYPE_OPTIONS"
              :key="option.value"
              :value="option.value"
            >
              {{ option.label }}
            </a-select-option>
          </a-select>
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
        <template v-if="column.dataIndex === 'cover'">
          <a-image v-if="record.cover" :src="formatCoverUrl(record.cover)" :width="80" :height="60" class="cover-image" />
          <div v-else class="no-cover">
            <span class="no-cover-icon">📦</span>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'initPrompt'">
          <a-tooltip :title="record.initPrompt">
            <div class="prompt-text">{{ record.initPrompt }}</div>
          </a-tooltip>
        </template>
        <template v-else-if="column.dataIndex === 'codeGenType'">
          <a-tag class="type-tag">{{ formatCodeGenType(record.codeGenType) }}</a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'priority'">
          <a-tag v-if="record.priority === 99" color="gold" class="featured-tag">精选</a-tag>
          <span v-else class="priority-value">{{ record.priority || 0 }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'deployedTime'">
          <span v-if="record.deployedTime" class="time-value">
            {{ formatTime(record.deployedTime) }}
          </span>
          <span v-else class="text-muted">未部署</span>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          <span class="time-value">{{ formatTime(record.createTime) }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'user'">
          <UserInfo :user="record.user" size="small" />
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space class="action-buttons">
            <a-button type="primary" size="small" @click="editApp(record)" class="cyber-btn-sm">
              <template #icon><EditOutlined /></template>
              编辑
            </a-button>
            <a-button
              size="small"
              @click="toggleFeatured(record)"
              :class="['cyber-btn-sm', { 'featured-btn': record.priority === 99 }]"
            >
              <template #icon><StarOutlined /></template>
              {{ record.priority === 99 ? '取消精选' : '精选' }}
            </a-button>
            <a-popconfirm title="确定要删除这个应用吗？" @confirm="deleteApp(record.id)">
              <a-button danger size="small" class="danger-btn">
                <template #icon><DeleteOutlined /></template>
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { listAppVoByPageByAdmin, deleteAppByAdmin, updateAppByAdmin } from '@/api/appController'
import { CODE_GEN_TYPE_OPTIONS, formatCodeGenType } from '@/utils/codeGenTypes'
import { formatTime } from '@/utils/time'
import { formatCoverUrl } from '@/utils/format'
import UserInfo from '@/components/UserInfo.vue'
import { SearchOutlined, EditOutlined, StarOutlined, DeleteOutlined } from '@ant-design/icons-vue'

const codeChars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789{}[]<>/\\|;:=+-*&^%$#@!'

const router = useRouter()

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 80,
    fixed: 'left',
  },
  {
    title: '应用名称',
    dataIndex: 'appName',
    width: 150,
  },
  {
    title: '封面',
    dataIndex: 'cover',
    width: 100,
  },
  {
    title: '初始提示词',
    dataIndex: 'initPrompt',
    width: 200,
  },
  {
    title: '生成类型',
    dataIndex: 'codeGenType',
    width: 100,
  },
  {
    title: '优先级',
    dataIndex: 'priority',
    width: 80,
  },
  {
    title: '部署时间',
    dataIndex: 'deployedTime',
    width: 160,
  },
  {
    title: '创建者',
    dataIndex: 'user',
    width: 120,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
  {
    title: '操作',
    key: 'action',
    width: 220,
    fixed: 'right',
  },
]

const data = ref<API.AppVO[]>([])
const total = ref(0)

const searchParams = reactive<API.AppQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

const fetchData = async () => {
  try {
    const res = await listAppVoByPageByAdmin({
      ...searchParams,
    })
    if (res.data.data) {
      data.value = res.data.data.records ?? []
      total.value = res.data.data.totalRow ?? 0
    } else {
      message.error('获取数据失败，' + res.data.message)
    }
  } catch (error) {
    console.error('获取数据失败：', error)
    message.error('获取数据失败')
  }
}

onMounted(() => {
  fetchData()
})

const pagination = computed(() => {
  return {
    current: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条`,
  }
})

const doTableChange = (page: { current: number; pageSize: number }) => {
  searchParams.pageNum = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

const doSearch = () => {
  searchParams.pageNum = 1
  fetchData()
}

const editApp = (app: API.AppVO) => {
  router.push(`/app/edit/${app.id}`)
}

const toggleFeatured = async (app: API.AppVO) => {
  if (!app.id) return

  const newPriority = app.priority === 99 ? 0 : 99

  try {
    const res = await updateAppByAdmin({
      id: app.id,
      priority: newPriority,
    })

    if (res.data.code === 0) {
      message.success(newPriority === 99 ? '已设为精选' : '已取消精选')
      fetchData()
    } else {
      message.error('操作失败：' + res.data.message)
    }
  } catch (error) {
    console.error('操作失败：', error)
    message.error('操作失败')
  }
}

const deleteApp = async (id: number | undefined) => {
  if (!id) return

  try {
    const res = await deleteAppByAdmin({ id })
    if (res.data.code === 0) {
      message.success('删除成功')
      fetchData()
    } else {
      message.error('删除失败：' + res.data.message)
    }
  } catch (error) {
    console.error('删除失败：', error)
    message.error('删除失败')
  }
}
</script>

<style scoped>
#appManagePage {
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
  min-width: 180px;
}

.cyber-input::placeholder {
  color: rgba(255, 255, 255, 0.3) !important;
}

.cyber-input:focus {
  border-color: rgba(0, 255, 157, 0.4) !important;
  box-shadow: 0 0 0 3px rgba(0, 255, 157, 0.1) !important;
}

.cyber-select :deep(.ant-select-selector) {
  background: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(0, 255, 157, 0.15) !important;
  color: white !important;
  border-radius: 8px !important;
}

.cyber-select :deep(.ant-select-arrow) {
  color: rgba(255, 255, 255, 0.5) !important;
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

.cyber-table :deep(.ant-table-column-sorter) {
  color: rgba(255, 255, 255, 0.4) !important;
}

.cyber-table :deep(.ant-table-filter-trigger) {
  color: rgba(255, 255, 255, 0.4) !important;
}

/* 封面图 */
.cover-image {
  border-radius: 6px;
  border: 1px solid rgba(0, 255, 157, 0.2);
}

.no-cover {
  width: 80px;
  height: 60px;
  background: rgba(0, 255, 157, 0.05);
  border: 1px dashed rgba(0, 255, 157, 0.2);
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.no-cover-icon {
  font-size: 24px;
  opacity: 0.5;
}

.prompt-text {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: rgba(255, 255, 255, 0.7);
}

.type-tag {
  background: rgba(0, 200, 255, 0.1) !important;
  border: 1px solid rgba(0, 200, 255, 0.3) !important;
  color: #00c8ff !important;
  font-size: 12px;
}

.featured-tag {
  background: linear-gradient(135deg, #ffd700 0%, #ffa500 100%) !important;
  border: none !important;
  color: #0a0a0f !important;
  font-weight: 600;
}

.priority-value {
  color: rgba(255, 255, 255, 0.5);
}

.time-value {
  color: rgba(255, 255, 255, 0.6);
  font-size: 13px;
}

.text-muted {
  color: rgba(255, 255, 255, 0.3);
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

.cyber-btn-sm {
  background: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(0, 255, 157, 0.2) !important;
  color: #00ff9d !important;
  font-size: 12px;
}

.cyber-btn-sm:hover {
  background: rgba(0, 255, 157, 0.1) !important;
  border-color: rgba(0, 255, 157, 0.4) !important;
}

.featured-btn {
  background: rgba(255, 215, 0, 0.1) !important;
  border-color: rgba(255, 215, 0, 0.3) !important;
  color: #ffd700 !important;
}

.featured-btn:hover {
  background: rgba(255, 215, 0, 0.2) !important;
  border-color: #ffd700 !important;
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

.action-buttons {
  display: flex;
  gap: 8px;
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
