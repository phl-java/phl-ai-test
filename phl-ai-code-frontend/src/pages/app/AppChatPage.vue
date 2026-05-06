<template>
  <div id="appChatPage">
    <!-- 顶部栏 -->
    <div class="header-bar">
      <div class="header-left">
        <h1 class="app-name">{{ appInfo?.appName || '网站生成器' }}</h1>
        <a-tag v-if="appInfo?.codeGenType" class="code-gen-type-tag">
          {{ formatCodeGenType(appInfo.codeGenType) }}
        </a-tag>
        <a-tag v-if="appInfo?.modelType" :color="appInfo.modelType === 'deepseek' ? 'blue' : 'green'" class="model-type-tag">
          <template #icon><RobotOutlined v-if="appInfo.modelType === 'deepseek'" /></template>
          {{ appInfo.modelType === 'deepseek' ? 'DeepSeek' : '通义千问' }}
        </a-tag>
      </div>
      <div class="header-right">
        <a-button @click="showAppDetail" class="cyber-btn-outline">
          <template #icon><InfoCircleOutlined /></template>
          应用详情
        </a-button>
        <a-button
            @click="downloadCode"
            :loading="downloading"
            :disabled="!isOwner"
            class="cyber-btn-outline"
        >
          <template #icon><DownloadOutlined /></template>
          下载代码
        </a-button>
        <a-button @click="deployApp" :loading="deploying" class="cyber-btn-primary">
          <template #icon><CloudUploadOutlined /></template>
          部署
        </a-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧对话区域 -->
      <div class="chat-section">
        <!-- 消息区域 -->
        <div class="messages-container" ref="messagesContainer">
          <!-- 加载更多按钮 -->
          <div v-if="hasMoreHistory" class="load-more-container">
            <a-button type="link" @click="loadMoreHistory" :loading="loadingHistory" size="small" class="load-more-btn">
              加载更多历史消息
            </a-button>
          </div>
          <div v-for="(message, index) in messages" :key="index" class="message-item">
            <div v-if="message.type === 'user'" class="user-message">
              <div class="message-content">{{ message.content }}</div>
              <div class="message-avatar">
                <a-avatar :src="loginUserStore.loginUser.userAvatar" class="cyber-avatar" />
              </div>
            </div>
            <div v-else class="ai-message">
              <div class="message-avatar">
                <a-avatar :src="aiAvatar" class="cyber-avatar" />
              </div>
              <div class="message-content">
                <MarkdownRenderer v-if="message.content" :content="message.content" />
                <div v-if="message.loading" class="loading-indicator">
                  <a-spin size="small" />
                  <span>AI 正在思考...</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 选中元素信息展示 -->
        <a-alert
            v-if="selectedElementInfo"
            class="selected-element-alert"
            closable
            @close="clearSelectedElement"
        >
          <template #message>
            <div class="selected-element-info">
              <div class="element-header">
                <span class="element-tag">
                  选中元素：{{ selectedElementInfo.tagName.toLowerCase() }}
                </span>
                <span v-if="selectedElementInfo.id" class="element-id">
                  #{{ selectedElementInfo.id }}
                </span>
                <span v-if="selectedElementInfo.className" class="element-class">
                  .{{ selectedElementInfo.className.split(' ').join('.') }}
                </span>
              </div>
              <div class="element-details">
                <div v-if="selectedElementInfo.textContent" class="element-item">
                  内容: {{ selectedElementInfo.textContent.substring(0, 50) }}
                  {{ selectedElementInfo.textContent.length > 50 ? '...' : '' }}
                </div>
                <div v-if="selectedElementInfo.pagePath" class="element-item">
                  页面路径: {{ selectedElementInfo.pagePath }}
                </div>
                <div class="element-item">
                  选择器:
                  <code class="element-selector-code">{{ selectedElementInfo.selector }}</code>
                </div>
              </div>
            </div>
          </template>
        </a-alert>

        <!-- Build status indicator -->
        <div v-if="buildStatus.visible" class="build-status-bar" :class="buildStatus.stage.toLowerCase()">
          <a-spin v-if="buildStatus.stage === 'BUILDING' || buildStatus.stage === 'FIXING'" size="small" />
          <CheckCircleOutlined v-else-if="buildStatus.stage === 'DONE'" />
          <CloseCircleOutlined v-else-if="buildStatus.stage === 'FAILED'" />
          <span class="build-status-message">
            <template v-if="buildStatus.stage === 'BUILDING'">正在构建项目...</template>
            <template v-else-if="buildStatus.stage === 'FIXING'">AI 正在自动修复 ({{ buildStatus.retryCount }}/{{ buildStatus.maxRetries }})...</template>
            <template v-else-if="buildStatus.stage === 'DONE'">构建成功</template>
            <template v-else-if="buildStatus.stage === 'FAILED'">构建失败</template>
          </span>
        </div>

        <!-- 用户消息输入框 -->
        <div class="input-container">
          <div class="input-wrapper">
            <a-tooltip v-if="!isOwner" title="无法在别人的作品下对话哦~" placement="top">
              <a-textarea
                  v-model:value="userInput"
                  :placeholder="getInputPlaceholder()"
                  :rows="4"
                  :maxlength="1000"
                  @keydown.enter.prevent="sendMessage"
                  :disabled="isGenerating || !isOwner"
                  class="cyber-input"
              />
            </a-tooltip>
            <a-textarea
                v-else
                v-model:value="userInput"
                :placeholder="getInputPlaceholder()"
                :rows="4"
                :maxlength="1000"
                @keydown.enter.prevent="sendMessage"
                :disabled="isGenerating"
                class="cyber-input"
            />
            <div class="input-actions">
              <a-button
                  type="primary"
                  @click="sendMessage"
                  :loading="isGenerating"
                  :disabled="!isOwner"
                  class="cyber-btn-primary"
              >
                <template #icon>
                  <SendOutlined />
                </template>
              </a-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间网页展示区域 -->
      <div class="preview-section">
        <div class="preview-header">
          <h3>生成后的网页展示</h3>
          <div class="preview-actions">
            <a-button
                v-if="isOwner && previewUrl"
                type="link"
                :danger="isEditMode"
                @click="toggleEditMode"
                :class="{ 'edit-mode-active': isEditMode }"
                style="padding: 0; height: auto; margin-right: 12px"
            >
              <template #icon>
                <EditOutlined />
              </template>
              {{ isEditMode ? '退出编辑' : '编辑模式' }}
            </a-button>
            <a-button v-if="previewUrl" type="link" @click="openInNewTab">
              <template #icon>
                <ExportOutlined />
              </template>
              新窗口打开
            </a-button>
          </div>
        </div>
        <div class="preview-content">
          <div v-if="!previewUrl && !isGenerating" class="preview-placeholder">
            <div class="placeholder-icon">🌐</div>
            <p>网站文件生成完成后将在这里展示</p>
          </div>
          <div v-else-if="isGenerating" class="preview-loading">
            <a-spin size="large" />
            <p>正在生成网站...</p>
          </div>
          <iframe
              v-else
              :src="previewUrl"
              class="preview-iframe"
              frameborder="0"
              @load="onIframeLoad"
          ></iframe>
        </div>
      </div>

      <!-- 右侧属性编辑面板 -->
      <div class="style-editor-section" v-if="selectedElementInfo">
        <StyleEditorPanel
          :element-info="selectedElementInfo"
          :visible="!!selectedElementInfo"
          @update:style="handleStyleChange"
          @update:style:batch="handleBatchStyleApply"
          @upload:image="handleImageUpload"
          @close="clearSelectedElement"
        />
      </div>
    </div>

    <!-- 应用详情弹窗 -->
    <AppDetailModal
        v-model:open="appDetailVisible"
        :app="appInfo"
        :show-actions="isOwner || isAdmin"
        @edit="editApp"
        @delete="deleteApp"
    />

    <!-- 部署成功弹窗 -->
    <DeploySuccessModal
        v-model:open="deployModalVisible"
        :deploy-url="deployUrl"
        @open-site="openDeployedSite"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, onUnmounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser'
import {
  getAppVoById,
  deployApp as deployAppApi,
  deleteApp as deleteAppApi,
} from '@/api/appController'
import { listAppChatHistory } from '@/api/chatHistoryController'
import { CodeGenTypeEnum, formatCodeGenType } from '@/utils/codeGenTypes'
import request from '@/request'

import MarkdownRenderer from '@/components/MarkdownRenderer.vue'
import AppDetailModal from '@/components/AppDetailModal.vue'
import DeploySuccessModal from '@/components/DeploySuccessModal.vue'
import StyleEditorPanel from '@/components/StyleEditorPanel.vue'
import aiAvatar from '@/assets/aiAvatar.png'
import { API_BASE_URL, getStaticPreviewUrl } from '@/config/env'
import { VisualEditor, type ElementInfo } from '@/utils/visualEditor'

import {
  CloudUploadOutlined,
  SendOutlined,
  ExportOutlined,
  InfoCircleOutlined,
  DownloadOutlined,
  EditOutlined,
  RobotOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
} from '@ant-design/icons-vue'

const route = useRoute()
const router = useRouter()
const loginUserStore = useLoginUserStore()

const appInfo = ref<API.AppVO>()
const appId = ref<any>()

interface Message {
  type: 'user' | 'ai'
  content: string
  loading?: boolean
  createTime?: string
}

const messages = ref<Message[]>([])
const userInput = ref('')
const isGenerating = ref(false)
const messagesContainer = ref<HTMLElement>()

const loadingHistory = ref(false)
const hasMoreHistory = ref(false)
const lastCreateTime = ref<string>()
const historyLoaded = ref(false)

const previewUrl = ref('')
const previewReady = ref(false)

const deploying = ref(false)
const deployModalVisible = ref(false)
const deployUrl = ref('')

const downloading = ref(false)

const isEditMode = ref(false)
const selectedElementInfo = ref<ElementInfo | null>(null)
const pendingStyleChanges = ref<{ property: string; value: string }[]>([])
const visualEditor = new VisualEditor({
  onElementSelected: (elementInfo: ElementInfo) => {
    selectedElementInfo.value = elementInfo
  },
})

const buildStatus = ref<{
  visible: boolean
  stage: string
  message: string
  retryCount: number
  maxRetries: number
}>({
  visible: false,
  stage: '',
  message: '',
  retryCount: 0,
  maxRetries: 3,
})

const isOwner = computed(() => {
  return appInfo.value?.userId === loginUserStore.loginUser.id
})

const isAdmin = computed(() => {
  return loginUserStore.loginUser.userRole === 'admin'
})

const appDetailVisible = ref(false)

const showAppDetail = () => {
  appDetailVisible.value = true
}

const loadChatHistory = async (isLoadMore = false) => {
  if (!appId.value || loadingHistory.value) return
  loadingHistory.value = true
  try {
    const params: API.listAppChatHistoryParams = {
      appId: appId.value,
      pageSize: 10,
    }
    if (isLoadMore && lastCreateTime.value) {
      params.lastCreateTime = lastCreateTime.value
    }
    const res = await listAppChatHistory(params)
    if (res.data.code === 0 && res.data.data) {
      const chatHistories = res.data.data.records || []
      if (chatHistories.length > 0) {
        const historyMessages: Message[] = chatHistories
            .map((chat) => ({
              type: (chat.messageType === 'user' ? 'user' : 'ai') as 'user' | 'ai',
              content: chat.message || '',
              createTime: chat.createTime,
            }))
            .reverse()
        if (isLoadMore) {
          messages.value.unshift(...historyMessages)
        } else {
          messages.value = historyMessages
        }
        lastCreateTime.value = chatHistories[chatHistories.length - 1]?.createTime
        hasMoreHistory.value = chatHistories.length === 10
      } else {
        hasMoreHistory.value = false
      }
      historyLoaded.value = true
    }
  } catch (error) {
    console.error('加载对话历史失败：', error)
    message.error('加载对话历史失败')
  } finally {
    loadingHistory.value = false
  }
}

const loadMoreHistory = async () => {
  await loadChatHistory(true)
}

const fetchAppInfo = async (retryCount = 0) => {
  const id = route.params.id as string
  if (!id) {
    message.error('应用ID不存在')
    router.push('/')
    return
  }

  appId.value = id

  try {
    const res = await getAppVoById({ id: id })
    if (res.data.code === 0 && res.data.data) {
      appInfo.value = res.data.data

      await loadChatHistory()
      if (messages.value.length >= 2) {
        updatePreview()
      }
      if (
          appInfo.value.initPrompt &&
          isOwner.value &&
          messages.value.length === 0 &&
          historyLoaded.value
      ) {
        await sendInitialMessage(appInfo.value.initPrompt)
      }
    } else {
      if (retryCount < 3 && res.data.code !== 0) {
        console.log(`应用信息获取失败，第${retryCount + 1}次重试...`)
        setTimeout(() => {
          fetchAppInfo(retryCount + 1)
        }, 1000 * (retryCount + 1))
        return
      }

      message.error('获取应用信息失败: ' + res.data.message)
      router.push('/')
    }
  } catch (error) {
    console.error('获取应用信息失败：', error)

    if (retryCount < 3) {
      console.log(`应用信息获取异常，第${retryCount + 1}次重试...`)
      setTimeout(() => {
        fetchAppInfo(retryCount + 1)
      }, 1000 * (retryCount + 1))
      return
    }

    message.error('获取应用信息失败')
    router.push('/')
  }
}

const sendInitialMessage = async (prompt: string) => {
  messages.value.push({
    type: 'user',
    content: prompt,
  })

  const aiMessageIndex = messages.value.length
  messages.value.push({
    type: 'ai',
    content: '',
    loading: true,
  })

  await nextTick()
  scrollToBottom()

  isGenerating.value = true
  await generateCode(prompt, aiMessageIndex)
}

const sendMessage = async () => {
  if (!userInput.value.trim() || isGenerating.value) {
    return
  }

  let message = userInput.value.trim()
  if (selectedElementInfo.value) {
    let elementContext = `\n\n选中元素信息：`
    if (selectedElementInfo.value.pagePath) {
      elementContext += `\n- 页面路径: ${selectedElementInfo.value.pagePath}`
    }
    elementContext += `\n- 标签: ${selectedElementInfo.value.tagName.toLowerCase()}\n- 选择器: ${selectedElementInfo.value.selector}`
    if (selectedElementInfo.value.textContent) {
      elementContext += `\n- 当前内容: ${selectedElementInfo.value.textContent.substring(0, 100)}`
    }
    message += elementContext
  }
  userInput.value = ''
  messages.value.push({
    type: 'user',
    content: message,
  })

  if (selectedElementInfo.value) {
    clearSelectedElement()
    if (isEditMode.value) {
      toggleEditMode()
    }
  }

  const aiMessageIndex = messages.value.length
  messages.value.push({
    type: 'ai',
    content: '',
    loading: true,
  })

  await nextTick()
  scrollToBottom()

  isGenerating.value = true
  await generateCode(message, aiMessageIndex)
}

const generateCode = async (userMessage: string, aiMessageIndex: number) => {
  let eventSource: EventSource | null = null
  let streamCompleted = false

  try {
    const baseURL = request.defaults.baseURL || API_BASE_URL

    const params = new URLSearchParams({
      appId: appId.value || '',
      message: userMessage,
    })

    const url = `${baseURL}/app/chat/gen/code?${params}`

    eventSource = new EventSource(url, {
      withCredentials: true,
    })

    let fullContent = ''

    eventSource.onmessage = function (event) {
      if (streamCompleted) return

      try {
        const parsed = JSON.parse(event.data)

        if (parsed.type === 'build_status') {
          handleBuildStatus(parsed)
          return
        }

        const content = parsed.d

        if (content !== undefined && content !== null) {
          fullContent += content
          messages.value[aiMessageIndex].content = fullContent
          messages.value[aiMessageIndex].loading = false
          scrollToBottom()
        }
      } catch (error) {
        console.error('解析消息失败:', error)
        handleError(error, aiMessageIndex)
      }
    }

    eventSource.addEventListener('done', function () {
      if (streamCompleted) return

      streamCompleted = true
      isGenerating.value = false
      eventSource?.close()

      setTimeout(async () => {
        await fetchAppInfo()
        updatePreview()
      }, 1000)
    })

    eventSource.addEventListener('business-error', function (event: MessageEvent) {
      if (streamCompleted) return

      try {
        const errorData = JSON.parse(event.data)
        console.error('SSE业务错误事件:', errorData)

        const errorMessage = errorData.message || '生成过程中出现错误'
        messages.value[aiMessageIndex].content = `❌ ${errorMessage}`
        messages.value[aiMessageIndex].loading = false
        message.error(errorMessage)

        streamCompleted = true
        isGenerating.value = false
        eventSource?.close()
      } catch (parseError) {
        console.error('解析错误事件失败:', parseError, '原始数据:', event.data)
        handleError(new Error('服务器返回错误'), aiMessageIndex)
      }
    })

    eventSource.onerror = function () {
      if (streamCompleted || !isGenerating.value) return
      if (eventSource?.readyState === EventSource.CONNECTING) {
        streamCompleted = true
        isGenerating.value = false
        eventSource?.close()

        setTimeout(async () => {
          await fetchAppInfo()
          updatePreview()
        }, 1000)
      } else {
        handleError(new Error('SSE连接错误'), aiMessageIndex)
      }
    }
  } catch (error) {
    console.error('创建 EventSource 失败：', error)
    handleError(error, aiMessageIndex)
  }
}

const handleError = (error: unknown, aiMessageIndex: number) => {
  console.error('生成代码失败：', error)
  messages.value[aiMessageIndex].content = '抱歉，生成过程中出现了错误，请重试。'
  messages.value[aiMessageIndex].loading = false
  message.error('生成失败，请重试')
  isGenerating.value = false
}

const updatePreview = () => {
  if (appId.value) {
    const codeGenType = appInfo.value?.codeGenType || CodeGenTypeEnum.HTML
    const newPreviewUrl = getStaticPreviewUrl(codeGenType, appId.value)
    previewUrl.value = newPreviewUrl
    previewReady.value = true
  }
}

const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

const handleBuildStatus = (status: any) => {
  buildStatus.value = {
    visible: true,
    stage: status.stage,
    message: status.message,
    retryCount: status.retryCount || 0,
    maxRetries: status.maxRetries || 3,
  }

  if (status.stage === 'DONE' || status.stage === 'FAILED') {
    setTimeout(() => {
      buildStatus.value.visible = false
      if (status.stage === 'DONE') {
        updatePreview()
      }
    }, 3000)
  }
}

const downloadCode = async () => {
  if (!appId.value) {
    message.error('应用ID不存在')
    return
  }
  downloading.value = true
  try {
    const API_BASE_URL = request.defaults.baseURL || ''
    const url = `${API_BASE_URL}/app/download/${appId.value}`
    const response = await fetch(url, {
      method: 'GET',
      credentials: 'include',
    })
    if (!response.ok) {
      throw new Error(`下载失败: ${response.status}`)
    }
    const contentDisposition = response.headers.get('Content-Disposition')
    const fileName = contentDisposition?.match(/filename="(.+)"/)?.[1] || `app-${appId.value}.zip`
    const blob = await response.blob()
    const downloadUrl = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = downloadUrl
    link.download = fileName
    link.click()
    URL.revokeObjectURL(downloadUrl)
    message.success('代码下载成功')
  } catch (error) {
    console.error('下载失败：', error)
    message.error('下载失败，请重试')
  } finally {
    downloading.value = false
  }
}

const deployApp = async () => {
  if (!appId.value) {
    message.error('应用ID不存在')
    return
  }

  deploying.value = true
  try {
    const res = await deployAppApi({
      appId: appId.value,
    })

    if (res.data.code === 0 && res.data.data) {
      deployUrl.value = res.data.data
      deployModalVisible.value = true
      message.success('部署成功')
    } else {
      message.error('部署失败：' + res.data.message)
    }
  } catch (error) {
    console.error('部署失败：', error)
    message.error('部署失败，请重试')
  } finally {
    deploying.value = false
  }
}

const openInNewTab = () => {
  if (previewUrl.value) {
    window.open(previewUrl.value, '_blank')
  }
}

const openDeployedSite = () => {
  if (deployUrl.value) {
    window.open(deployUrl.value, '_blank')
  }
}

const onIframeLoad = () => {
  previewReady.value = true
  const iframe = document.querySelector('.preview-iframe') as HTMLIFrameElement
  if (iframe) {
    visualEditor.init(iframe)
    visualEditor.onIframeLoad()
  }
}

const editApp = () => {
  if (appInfo.value?.id) {
    router.push(`/app/edit/${appInfo.value.id}`)
  }
}

const deleteApp = async () => {
  if (!appInfo.value?.id) return

  try {
    const res = await deleteAppApi({ id: appInfo.value.id })
    if (res.data.code === 0) {
      message.success('删除成功')
      appDetailVisible.value = false
      router.push('/')
    } else {
      message.error('删除失败：' + res.data.message)
    }
  } catch (error) {
    console.error('删除失败：', error)
    message.error('删除失败')
  }
}

const handleStyleChange = (payload: { selector: string; property: string; value: string }) => {
  if (visualEditor) {
    visualEditor.applyStyle(payload.selector, payload.property, payload.value)
  }
}

const handleBatchStyleApply = (payload: { selector: string; changes: { property: string; value: string }[] }) => {
  if (!payload.selector || payload.changes.length === 0) return

  const changesDesc = payload.changes
    .map(c => `${c.property}: ${c.value}`)
    .join(', ')

  userInput.value = `修改元素 ${payload.selector} 的样式：${changesDesc}`
  selectedElementInfo.value = null
  sendMessage()
}

const handleImageUpload = (payload: { file: File }) => {
  console.log('Image upload:', payload.file)
}

const toggleEditMode = () => {
  const iframe = document.querySelector('.preview-iframe') as HTMLIFrameElement
  if (!iframe) {
    message.warning('请等待页面加载完成')
    return
  }
  if (!previewReady.value) {
    message.warning('请等待页面加载完成')
    return
  }
  const newEditMode = visualEditor.toggleEditMode()
  isEditMode.value = newEditMode
}

const clearSelectedElement = () => {
  selectedElementInfo.value = null
  visualEditor.clearSelection()
}

const getInputPlaceholder = () => {
  if (selectedElementInfo.value) {
    return `正在编辑 ${selectedElementInfo.value.tagName.toLowerCase()} 元素，描述您想要的修改...`
  }
  return '请描述你想生成的网站，越详细效果越好哦'
}

onMounted(() => {
  fetchAppInfo()

  window.addEventListener('message', (event) => {
    visualEditor.handleIframeMessage(event)
  })
})

onUnmounted(() => {
})
</script>

<style scoped>
#appChatPage {
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 16px;
  background: #0a0a0f;
}

/* 顶部栏 */
.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background: rgba(15, 15, 25, 0.8);
  border-radius: 12px;
  border: 1px solid rgba(0, 255, 157, 0.1);
  margin-bottom: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.code-gen-type-tag {
  background: rgba(0, 255, 157, 0.1) !important;
  border: 1px solid rgba(0, 255, 157, 0.3) !important;
  color: #00ff9d !important;
  font-size: 12px;
}

.model-type-tag {
  margin-left: 0 !important;
}

.app-name {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: white;
  font-family: 'Noto Sans SC', sans-serif;
}

.header-right {
  display: flex;
  gap: 12px;
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

.cyber-btn-outline {
  background: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(0, 255, 157, 0.2) !important;
  color: #00ff9d !important;
}

.cyber-btn-outline:hover {
  background: rgba(0, 255, 157, 0.1) !important;
  border-color: rgba(0, 255, 157, 0.4) !important;
}

/* 主要内容区域 */
.main-content {
  flex: 1;
  display: flex;
  gap: 16px;
  padding: 0;
  overflow: hidden;
}

/* 左侧对话区域 */
.chat-section {
  flex: 2;
  display: flex;
  flex-direction: column;
  background: rgba(15, 15, 25, 0.8);
  border-radius: 12px;
  border: 1px solid rgba(0, 255, 157, 0.1);
  overflow: hidden;
}

.messages-container {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  scroll-behavior: smooth;
}

.message-item {
  margin-bottom: 16px;
}

.user-message {
  display: flex;
  justify-content: flex-end;
  align-items: flex-start;
  gap: 12px;
}

.ai-message {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 12px;
}

.message-content {
  max-width: 70%;
  padding: 14px 18px;
  border-radius: 16px;
  line-height: 1.6;
  word-wrap: break-word;
  font-size: 14px;
}

.user-message .message-content {
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%);
  color: #0a0a0f;
  border-bottom-right-radius: 4px;
}

.ai-message .message-content {
  background: rgba(255, 255, 255, 0.08);
  color: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-bottom-left-radius: 4px;
}

.message-avatar {
  flex-shrink: 0;
}

.cyber-avatar {
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%) !important;
  color: #0a0a0f !important;
  font-weight: 700 !important;
  border: 2px solid rgba(0, 255, 157, 0.3);
}

.loading-indicator {
  display: flex;
  align-items: center;
  gap: 10px;
  color: rgba(255, 255, 255, 0.5);
  padding: 8px 0;
}

.load-more-container {
  text-align: center;
  padding: 12px 0;
  margin-bottom: 16px;
}

.load-more-btn {
  color: #00ff9d !important;
}

/* 输入区域 */
.input-container {
  padding: 16px 20px;
  background: rgba(10, 10, 15, 0.6);
  border-top: 1px solid rgba(0, 255, 157, 0.1);
}

.input-wrapper {
  position: relative;
}

.cyber-input {
  background: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(0, 255, 157, 0.15) !important;
  color: white !important;
  border-radius: 12px !important;
  padding: 14px 60px 14px 16px !important;
  font-size: 14px !important;
}

.cyber-input::placeholder {
  color: rgba(255, 255, 255, 0.3) !important;
}

.cyber-input:focus {
  border-color: rgba(0, 255, 157, 0.4) !important;
  box-shadow: 0 0 0 3px rgba(0, 255, 157, 0.1) !important;
}

.input-wrapper .ant-input {
  padding-right: 50px;
}

.input-actions {
  position: absolute;
  bottom: 12px;
  right: 12px;
}

/* 右侧预览区域 */
.preview-section {
  flex: 3;
  display: flex;
  flex-direction: column;
  background: rgba(15, 15, 25, 0.8);
  border-radius: 12px;
  border: 1px solid rgba(0, 255, 157, 0.1);
  overflow: hidden;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid rgba(0, 255, 157, 0.1);
  background: rgba(10, 10, 15, 0.4);
}

.preview-header h3 {
  margin: 0;
  font-size: 15px;
  font-weight: 600;
  color: white;
}

.preview-actions {
  display: flex;
  gap: 8px;
}

.preview-actions :deep(.ant-btn-link) {
  color: #00ff9d !important;
}

.preview-content {
  flex: 1;
  position: relative;
  overflow: hidden;
}

.preview-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: rgba(255, 255, 255, 0.4);
}

.placeholder-icon {
  font-size: 56px;
  margin-bottom: 20px;
}

.preview-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: rgba(255, 255, 255, 0.5);
}

.preview-loading p {
  margin-top: 20px;
}

.preview-iframe {
  width: 100%;
  height: 100%;
  border: none;
  background: white;
}

/* 选中元素提示 */
.selected-element-alert {
  margin: 0 20px 12px !important;
  background: rgba(0, 255, 157, 0.08) !important;
  border: 1px solid rgba(0, 255, 157, 0.2) !important;
  border-radius: 10px !important;
}

.selected-element-alert :deep(.ant-alert-message) {
  color: rgba(255, 255, 255, 0.9) !important;
}

.element-tag {
  color: #00ff9d;
  font-weight: 600;
}

.element-id {
  color: #00c8ff;
  margin-left: 8px;
}

.element-class {
  color: #ff006e;
  margin-left: 8px;
}

.element-item {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
  margin-top: 6px;
}

.element-selector-code {
  font-family: 'JetBrains Mono', monospace;
  background: rgba(0, 0, 0, 0.3);
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 11px;
  color: #00ff9d;
}

.edit-mode-active {
  background-color: rgba(255, 0, 110, 0.2) !important;
  border-color: #ff006e !important;
  color: #ff006e !important;
}

/* 右侧样式编辑面板 */
.style-editor-section {
  flex: 1.5;
  min-width: 320px;
  max-width: 400px;
  overflow-y: auto;
  animation: slideInRight 0.3s ease;
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .main-content {
    flex-direction: column;
  }

  .chat-section,
  .preview-section {
    flex: none;
    height: 50vh;
  }

  .style-editor-section {
    display: none;
  }
}

@media (max-width: 768px) {
  .header-bar {
    flex-direction: column;
    gap: 12px;
    padding: 12px 16px;
  }

  .header-right {
    flex-wrap: wrap;
    justify-content: center;
  }

  .app-name {
    font-size: 16px;
  }

  .message-content {
    max-width: 85%;
  }
}

.build-status-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  margin: 0 20px 12px;
  border-radius: 8px;
  font-size: 13px;
  animation: fadeIn 0.3s ease;
}

.build-status-bar.building {
  background: rgba(0, 255, 157, 0.08);
  border: 1px solid rgba(0, 255, 157, 0.2);
  color: #00ff9d;
}

.build-status-bar.fixing {
  background: rgba(255, 193, 7, 0.08);
  border: 1px solid rgba(255, 193, 7, 0.2);
  color: #ffc107;
}

.build-status-bar.done {
  background: rgba(82, 196, 26, 0.08);
  border: 1px solid rgba(82, 196, 26, 0.2);
  color: #52c41a;
}

.build-status-bar.failed {
  background: rgba(255, 0, 0, 0.08);
  border: 1px solid rgba(255, 0, 0, 0.2);
  color: #ff4d4f;
}

.build-status-message {
  flex: 1;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-5px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
