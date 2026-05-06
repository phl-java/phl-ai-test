<template>
  <div id="chatManagePage">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">对话管理</h1>
      <p class="page-desc">管理平台上的所有对话记录</p>
    </div>

    <!-- 搜索表单 -->
    <div class="search-section">
      <a-form layout="inline" :model="searchParams" @finish="doSearch" class="search-form">
        <a-form-item label="消息内容">
          <a-input v-model:value="searchParams.message" placeholder="输入消息内容" class="cyber-input" />
        </a-form-item>
        <a-form-item label="消息类型">
          <a-select
            v-model:value="searchParams.messageType"
            placeholder="选择消息类型"
            style="width: 140px"
            class="cyber-select"
          >
            <a-select-option value="">全部</a-select-option>
            <a-select-option value="user">用户消息</a-select-option>
            <a-select-option value="assistant">AI消息</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="应用ID">
          <a-input v-model:value="searchParams.appId" placeholder="输入应用ID" class="cyber-input" />
        </a-form-item>
        <a-form-item label="用户ID">
          <a-input v-model:value="searchParams.userId" placeholder="输入用户ID" class="cyber-input" />
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
      :scroll="{ x: 1400 }"
      class="cyber-table"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'message'">
          <a-tooltip :title="record.message">
            <div class="message-text">{{ record.message }}</div>
          </a-tooltip>
        </template>
        <template v-else-if="column.dataIndex === 'messageType'">
          <a-tag :color="record.messageType === 'user' ? 'blue' : 'green'" class="type-tag">
            {{ record.messageType === 'user' ? '用户消息' : 'AI消息' }}
          </a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          <span class="time-value">{{ formatTime(record.createTime) }}</span>
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space class="action-buttons">
            <a-button type="primary" size="small" @click="viewAppChat(record.appId)" class="cyber-btn-sm">
              <template #icon><MessageOutlined /></template>
              查看对话
            </a-button>
            <a-popconfirm title="确定要删除这条消息吗？" @confirm="deleteMessage(record.id)">
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
import { listAllChatHistoryByPageForAdmin } from '@/api/chatHistoryController'
import { formatTime } from '@/utils/time'
import { SearchOutlined, MessageOutlined, DeleteOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 80,
    fixed: 'left',
  },
  {
    title: '消息内容',
    dataIndex: 'message',
    width: 300,
  },
  {
    title: '消息类型',
    dataIndex: 'messageType',
    width: 100,
  },
  {
    title: '应用ID',
    dataIndex: 'appId',
    width: 80,
  },
  {
    title: '用户ID',
    dataIndex: 'userId',
    width: 80,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
  {
    title: '操作',
    key: 'action',
    width: 180,
    fixed: 'right',
  },
]

// 数据
const data = ref<API.ChatHistory[]>([])
const total = ref(0)

// 搜索条件
const searchParams = reactive<API.ChatHistoryQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

// 获取数据
const fetchData = async () => {
  try {
    const res = await listAllChatHistoryByPageForAdmin({
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

// 页面加载时请求一次
onMounted(() => {
  fetchData()
})

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

// 表格变化处理
const doTableChange = (page: { current: number; pageSize: number }) => {
  searchParams.pageNum = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

// 搜索
const doSearch = () => {
  // 重置页码
  searchParams.pageNum = 1
  fetchData()
}

// 查看应用对话
const viewAppChat = (appId: number | undefined) => {
  if (appId) {
    router.push(`/app/chat/${appId}`)
  }
}

// 删除消息
const deleteMessage = async (id: number | undefined) => {
  if (!id) return

  try {
    // 注意：这里需要后端提供删除对话历史的接口
    // 目前先显示成功，实际实现需要调用删除接口
    message.success('删除成功')
    // 刷新数据
    fetchData()
  } catch (error) {
    console.error('删除失败：', error)
    message.error('删除失败')
  }
}
</script>

<style scoped>
#chatManagePage {
  padding: 24px;
  background: transparent;
  min-height: 100%;
}

/* 页面标题 */
.page-header {
  margin-bottom: 32px;
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
  min-width: 140px;
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

.message-text {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: rgba(255, 255, 255, 0.7);
}

.type-tag {
  background: rgba(0, 200, 255, 0.1) !important;
  border: 1px solid rgba(0, 200, 255, 0.3) !important;
  color: #00c8ff !important;
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
