<template>
  <div v-show="visible" class="style-editor-panel">
    <div class="panel-header">
      <span class="panel-title">属性编辑</span>
      <a-button type="text" size="small" @click="$emit('close')" class="close-btn">
        <template #icon><CloseOutlined /></template>
      </a-button>
    </div>

    <div v-if="!elementInfo" class="panel-empty">
      <div class="empty-icon">🎯</div>
      <p class="empty-text">请先在预览页面中点击选中一个元素</p>
    </div>

    <template v-else>
      <div class="element-badge">
        <span class="badge-tag">{{ elementInfo.tagName.toLowerCase() }}</span>
        <span v-if="elementInfo.id" class="badge-id">#{{ elementInfo.id }}</span>
        <span v-if="elementInfo.className" class="badge-class">.{{ elementInfo.className.split(' ').join('.') }}</span>
      </div>

      <div class="panel-body">
        <a-collapse v-model:activeKey="activeKeys" :bordered="false" class="cyber-collapse" expand-icon-position="end">
          <a-collapse-panel key="layout" header="📐 布局">
            <div class="section-content">
              <div class="field-row">
                <div class="field-label">Width</div>
                <div class="field-control dim-control">
                  <a-input-number
                    :value="getDimNum('width')"
                    :min="0"
                    :max="9999"
                    :precision="0"
                    placeholder="auto"
                    class="dim-input"
                    @change="(val) => setDim('width', val, getDimUnit('width'))"
                  />
                  <a-select
                    :value="getDimUnit('width')"
                    size="small"
                    class="unit-select"
                    @change="(val) => setDim('width', getDimNum('width'), val)"
                  >
                    <a-select-option value="px">px</a-select-option>
                    <a-select-option value="%">%</a-select-option>
                    <a-select-option value="em">em</a-select-option>
                    <a-select-option value="rem">rem</a-select-option>
                    <a-select-option value="auto">auto</a-select-option>
                  </a-select>
                </div>
              </div>
              <div class="field-row">
                <div class="field-label">Height</div>
                <div class="field-control dim-control">
                  <a-input-number
                    :value="getDimNum('height')"
                    :min="0"
                    :max="9999"
                    :precision="0"
                    placeholder="auto"
                    class="dim-input"
                    @change="(val) => setDim('height', val, getDimUnit('height'))"
                  />
                  <a-select
                    :value="getDimUnit('height')"
                    size="small"
                    class="unit-select"
                    @change="(val) => setDim('height', getDimNum('height'), val)"
                  >
                    <a-select-option value="px">px</a-select-option>
                    <a-select-option value="%">%</a-select-option>
                    <a-select-option value="em">em</a-select-option>
                    <a-select-option value="rem">rem</a-select-option>
                    <a-select-option value="auto">auto</a-select-option>
                  </a-select>
                </div>
              </div>
              <div class="field-row">
                <div class="field-label">Margin</div>
                <div class="field-control four-dim">
                  <a-tooltip title="上">
                    <a-input-number
                      :value="getDimNum('margin-top')"
                      :min="0"
                      size="small"
                      class="dim-sm"
                      placeholder="T"
                      @change="(val) => setDim('margin-top', val, 'px')"
                    />
                  </a-tooltip>
                  <a-tooltip title="右">
                    <a-input-number
                      :value="getDimNum('margin-right')"
                      :min="0"
                      size="small"
                      class="dim-sm"
                      placeholder="R"
                      @change="(val) => setDim('margin-right', val, 'px')"
                    />
                  </a-tooltip>
                  <a-tooltip title="下">
                    <a-input-number
                      :value="getDimNum('margin-bottom')"
                      :min="0"
                      size="small"
                      class="dim-sm"
                      placeholder="B"
                      @change="(val) => setDim('margin-bottom', val, 'px')"
                    />
                  </a-tooltip>
                  <a-tooltip title="左">
                    <a-input-number
                      :value="getDimNum('margin-left')"
                      :min="0"
                      size="small"
                      class="dim-sm"
                      placeholder="L"
                      @change="(val) => setDim('margin-left', val, 'px')"
                    />
                  </a-tooltip>
                </div>
              </div>
              <div class="field-row">
                <div class="field-label">Padding</div>
                <div class="field-control four-dim">
                  <a-tooltip title="上">
                    <a-input-number
                      :value="getDimNum('padding-top')"
                      :min="0"
                      size="small"
                      class="dim-sm"
                      placeholder="T"
                      @change="(val) => setDim('padding-top', val, 'px')"
                    />
                  </a-tooltip>
                  <a-tooltip title="右">
                    <a-input-number
                      :value="getDimNum('padding-right')"
                      :min="0"
                      size="small"
                      class="dim-sm"
                      placeholder="R"
                      @change="(val) => setDim('padding-right', val, 'px')"
                    />
                  </a-tooltip>
                  <a-tooltip title="下">
                    <a-input-number
                      :value="getDimNum('padding-bottom')"
                      :min="0"
                      size="small"
                      class="dim-sm"
                      placeholder="B"
                      @change="(val) => setDim('padding-bottom', val, 'px')"
                    />
                  </a-tooltip>
                  <a-tooltip title="左">
                    <a-input-number
                      :value="getDimNum('padding-left')"
                      :min="0"
                      size="small"
                      class="dim-sm"
                      placeholder="L"
                      @change="(val) => setDim('padding-left', val, 'px')"
                    />
                  </a-tooltip>
                </div>
              </div>
            </div>
          </a-collapse-panel>

          <a-collapse-panel key="typography" header="✏️ 排版">
            <div class="section-content">
              <div class="field-row">
                <div class="field-label">Font Size</div>
                <div class="field-control slider-control">
                  <a-slider
                    :value="sliderVal('font-size', 16)"
                    :min="8"
                    :max="72"
                    :step="1"
                    class="cyber-slider"
                    @afterChange="(val) => setChange('font-size', val + 'px')"
                  />
                  <span class="slider-value">{{ getChange('font-size', '16') }}</span>
                </div>
              </div>
              <div class="field-row">
                <div class="field-label">Font Weight</div>
                <div class="field-control">
                  <a-select
                    :value="getChange('font-weight', 'normal')"
                    class="cyber-select"
                    @change="(val) => setChange('font-weight', val)"
                  >
                    <a-select-option value="normal">Normal</a-select-option>
                    <a-select-option value="bold">Bold</a-select-option>
                    <a-select-option value="100">100</a-select-option>
                    <a-select-option value="200">200</a-select-option>
                    <a-select-option value="300">300</a-select-option>
                    <a-select-option value="400">400</a-select-option>
                    <a-select-option value="500">500</a-select-option>
                    <a-select-option value="600">600</a-select-option>
                    <a-select-option value="700">700</a-select-option>
                    <a-select-option value="800">800</a-select-option>
                    <a-select-option value="900">900</a-select-option>
                  </a-select>
                </div>
              </div>
              <div class="field-row">
                <div class="field-label">Text Align</div>
                <div class="field-control">
                  <div class="align-btn-group">
                    <a-tooltip title="左对齐">
                      <a-button
                        :type="getChange('text-align', 'left') === 'left' ? 'primary' : 'default'"
                        size="small"
                        class="align-btn"
                        @click="setChange('text-align', 'left')"
                      >
                        <template #icon><AlignLeftOutlined /></template>
                      </a-button>
                    </a-tooltip>
                    <a-tooltip title="居中">
                      <a-button
                        :type="getChange('text-align', 'left') === 'center' ? 'primary' : 'default'"
                        size="small"
                        class="align-btn"
                        @click="setChange('text-align', 'center')"
                      >
                        <template #icon><AlignCenterOutlined /></template>
                      </a-button>
                    </a-tooltip>
                    <a-tooltip title="右对齐">
                      <a-button
                        :type="getChange('text-align', 'left') === 'right' ? 'primary' : 'default'"
                        size="small"
                        class="align-btn"
                        @click="setChange('text-align', 'right')"
                      >
                        <template #icon><AlignRightOutlined /></template>
                      </a-button>
                    </a-tooltip>
                    <a-tooltip title="两端对齐">
                      <a-button
                        :type="getChange('text-align', 'left') === 'justify' ? 'primary' : 'default'"
                        size="small"
                        class="align-btn"
                        @click="setChange('text-align', 'justify')"
                      >
                        <template #icon>
                          <span class="justify-icon">⇔</span>
                        </template>
                      </a-button>
                    </a-tooltip>
                  </div>
                </div>
              </div>
              <div class="field-row">
                <div class="field-label">Line Height</div>
                <div class="field-control">
                  <a-input-number
                    :value="getLineHeight()"
                    :min="0.5"
                    :max="5"
                    :step="0.1"
                    :precision="1"
                    placeholder="1.5"
                    class="cyber-input-number"
                    @change="(val) => {
                      if (val !== null) setChange('line-height', val.toString())
                    }"
                  />
                </div>
              </div>
            </div>
          </a-collapse-panel>

          <a-collapse-panel key="color" header="🎨 颜色">
            <div class="section-content">
              <div class="field-row">
                <div class="field-label">背景色</div>
                <div class="field-control color-field" @click="activeColorField = 'bg'">
                  <div class="color-input-wrap">
                    <input
                      type="color"
                      :value="getColorForDisplay('background-color')"
                      class="color-picker-native"
                      @input="(e) => setChange('background-color', (e.target as HTMLInputElement).value)"
                      @focus="activeColorField = 'bg'"
                    />
                    <a-input
                      :value="getChange('background-color', '')"
                      placeholder="选择颜色"
                      size="small"
                      class="color-hex-input"
                      @change="(e) => setChange('background-color', (e.target as HTMLInputElement).value)"
                      @focus="activeColorField = 'bg'"
                    />
                  </div>
                </div>
              </div>
              <div class="field-row">
                <div class="field-label">文字色</div>
                <div class="field-control color-field" @click="activeColorField = 'text'">
                  <div class="color-input-wrap">
                    <input
                      type="color"
                      :value="getColorForDisplay('color')"
                      class="color-picker-native"
                      @input="(e) => setChange('color', (e.target as HTMLInputElement).value)"
                      @focus="activeColorField = 'text'"
                    />
                    <a-input
                      :value="getChange('color', '')"
                      placeholder="选择颜色"
                      size="small"
                      class="color-hex-input"
                      @change="(e) => setChange('color', (e.target as HTMLInputElement).value)"
                      @focus="activeColorField = 'text'"
                    />
                  </div>
                </div>
              </div>
              <div class="field-row">
                <div class="field-label">边框色</div>
                <div class="field-control color-field" @click="activeColorField = 'border'">
                  <div class="color-input-wrap">
                    <input
                      type="color"
                      :value="getColorForDisplay('border-color')"
                      class="color-picker-native"
                      @input="(e) => setChange('border-color', (e.target as HTMLInputElement).value)"
                      @focus="activeColorField = 'border'"
                    />
                    <a-input
                      :value="getChange('border-color', '')"
                      placeholder="选择颜色"
                      size="small"
                      class="color-hex-input"
                      @change="(e) => setChange('border-color', (e.target as HTMLInputElement).value)"
                      @focus="activeColorField = 'border'"
                    />
                  </div>
                </div>
              </div>
              <div class="color-presets">
                <span class="presets-label">预设颜色</span>
                <div class="presets-grid">
                  <div
                    v-for="color in colorPresets"
                    :key="color"
                    class="preset-swatch"
                    :class="{ active: isSwatchActive(color) }"
                    :style="{ backgroundColor: color }"
                    @click="applyPresetColor(color)"
                  >
                    <CheckOutlined v-if="isSwatchActive(color)" class="swatch-check" />
                  </div>
                </div>
              </div>
            </div>
          </a-collapse-panel>

          <a-collapse-panel key="border" header="🔲 边框">
            <div class="section-content">
              <div class="field-row">
                <div class="field-label">Border Width</div>
                <div class="field-control">
                  <a-input-number
                    :value="getDimNum('border-width')"
                    :min="0"
                    :max="100"
                    :precision="0"
                    placeholder="0"
                    class="cyber-input-number"
                    @change="(val) => setDim('border-width', val, 'px')"
                  />
                </div>
              </div>
              <div class="field-row">
                <div class="field-label">Border Style</div>
                <div class="field-control">
                  <a-select
                    :value="getChange('border-style', 'none')"
                    class="cyber-select"
                    @change="(val) => setChange('border-style', val)"
                  >
                    <a-select-option value="none">None</a-select-option>
                    <a-select-option value="solid">Solid</a-select-option>
                    <a-select-option value="dashed">Dashed</a-select-option>
                    <a-select-option value="dotted">Dotted</a-select-option>
                    <a-select-option value="double">Double</a-select-option>
                  </a-select>
                </div>
              </div>
              <div class="field-row">
                <div class="field-label">Border Radius</div>
                <div class="field-control">
                  <a-input-number
                    :value="getDimNum('border-radius')"
                    :min="0"
                    :max="999"
                    :precision="0"
                    placeholder="0"
                    class="cyber-input-number"
                    @change="(val) => setDim('border-radius', val, 'px')"
                  />
                </div>
              </div>
            </div>
          </a-collapse-panel>

          <a-collapse-panel key="background" header="🖼️ 背景图">
            <div class="section-content">
              <div class="upload-area">
                <a-upload
                  :before-upload="handleBeforeUpload"
                  :show-upload-list="false"
                  :accept="imageMimeTypes"
                  class="cyber-upload"
                >
                  <a-button class="upload-btn">
                    <template #icon><UploadOutlined /></template>
                    上传图片
                  </a-button>
                </a-upload>
                <div v-if="getChange('background-image', '')" class="bg-preview-wrap">
                  <div
                    class="bg-preview"
                    :style="{ backgroundImage: getChange('background-image', '') }"
                  >
                    <a-button
                      size="small"
                      class="bg-remove-btn"
                      @click="removeBackgroundImage"
                    >
                      <template #icon><DeleteOutlined /></template>
                    </a-button>
                  </div>
                </div>
                <div v-else class="bg-preview-empty">
                  <PictureOutlined class="preview-icon" />
                  <span>暂无背景图</span>
                </div>
              </div>
            </div>
          </a-collapse-panel>

          <a-collapse-panel key="effect" header="✨ 效果">
            <div class="section-content">
              <div class="field-row">
                <div class="field-label">Opacity</div>
                <div class="field-control slider-control">
                  <a-slider
                    :value="sliderVal('opacity', 100)"
                    :min="0"
                    :max="100"
                    :step="1"
                    class="cyber-slider"
                    @afterChange="(val) => setChange('opacity', (val / 100).toFixed(2))"
                  />
                  <span class="slider-value">{{ getChange('opacity', '1') }}</span>
                </div>
              </div>
              <div class="field-row">
                <div class="field-label">Box Shadow</div>
                <div class="field-control">
                  <a-select
                    :value="getShadowLabel()"
                    class="cyber-select"
                    @change="(val) => setChange('box-shadow', shadowMap[val] || 'none')"
                  >
                    <a-select-option value="none">无</a-select-option>
                    <a-select-option value="sm">小</a-select-option>
                    <a-select-option value="md">中</a-select-option>
                    <a-select-option value="lg">大</a-select-option>
                    <a-select-option value="glow">发光</a-select-option>
                  </a-select>
                </div>
              </div>
            </div>
          </a-collapse-panel>
        </a-collapse>

        <div class="panel-actions">
          <a-button block class="reset-btn" @click="handleReset">
            <template #icon><ReloadOutlined /></template>
            重置
          </a-button>
          <a-button block type="primary" class="apply-btn" @click="handleApply">
            <template #icon><CheckOutlined /></template>
            应用修改
          </a-button>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import type { ElementInfo } from '@/utils/visualEditor'
import { COLOR_PRESETS } from '@/utils/colorPalette'
import {
  CloseOutlined,
  ReloadOutlined,
  CheckOutlined,
  AlignLeftOutlined,
  AlignCenterOutlined,
  AlignRightOutlined,
  UploadOutlined,
  DeleteOutlined,
  PictureOutlined,
} from '@ant-design/icons-vue'

interface Props {
  elementInfo: ElementInfo | null
  visible: boolean
}

interface StyleChange {
  selector: string
  property: string
  value: string
}

interface BatchStyleChange {
  selector: string
  changes: { property: string; value: string }[]
}

interface Emits {
  (e: 'update:style', payload: StyleChange): void
  (e: 'update:style:batch', payload: BatchStyleChange): void
  (e: 'upload:image', file: File): void
  (e: 'close'): void
}

const props = withDefaults(defineProps<Props>(), {
  elementInfo: null,
  visible: false,
})

const emit = defineEmits<Emits>()

const changes = reactive<Record<string, string>>({})
const activeKeys = ref<string[]>(['layout', 'typography', 'color', 'border', 'background', 'effect'])
const activeColorField = ref<'bg' | 'text' | 'border'>('bg')
const colorPresets = COLOR_PRESETS
const imageMimeTypes = 'image/png,image/jpeg,image/gif,image/webp,image/svg+xml'

const shadowMap: Record<string, string> = {
  none: 'none',
  sm: '0 2px 4px rgba(0,0,0,0.3)',
  md: '0 4px 12px rgba(0,0,0,0.4)',
  lg: '0 8px 32px rgba(0,0,0,0.5)',
  glow: '0 0 20px rgba(0,255,157,0.4)',
}

const reverseShadowMap: Record<string, string> = {
  'none': 'none',
  '0 2px 4px rgba(0,0,0,0.3)': 'sm',
  '0 4px 12px rgba(0,0,0,0.4)': 'md',
  '0 8px 32px rgba(0,0,0,0.5)': 'lg',
  '0 0 20px rgba(0,255,157,0.4)': 'glow',
}

function getChange(prop: string, defaultVal: string = ''): string {
  return prop in changes ? changes[prop] : defaultVal
}

function setChange(prop: string, value: string) {
  if (value === '' || value === null || value === undefined) {
    delete changes[prop]
  } else {
    changes[prop] = value
  }
  if (props.elementInfo) {
    emit('update:style', {
      selector: props.elementInfo.selector,
      property: prop,
      value: value || '',
    })
  }
}

function getDimNum(prop: string): number | null {
  const stored = changes[prop]
  if (!stored) return null
  const match = stored.match(/^(-?\d+\.?\d*)/)
  if (match) {
    return match[1] ? parseFloat(match[1]) : null
  }
  return null
}

function getDimUnit(prop: string, defaultUnit: string = 'px'): string {
  const stored = changes[prop]
  if (!stored) return defaultUnit
  const match = stored.match(/^(?:-?\d+\.?\d*)(px|%|em|rem|auto|vh|vw)?$/)
  if (match && match[1]) return match[1]
  if (stored === 'auto') return 'auto'
  return defaultUnit
}

function setDim(prop: string, num: number | null, unit: string) {
  if (num === null || num === undefined) {
    if (unit === 'auto') {
      setChange(prop, 'auto')
    } else {
      delete changes[prop]
    }
    return
  }
  setChange(prop, `${num}${unit}`)
}

const sliderVal = (prop: string, defaultVal: number): number => {
  const stored = changes[prop]
  if (!stored) return defaultVal
  if (prop === 'opacity') {
    const num = parseFloat(stored) * 100
    return isNaN(num) ? defaultVal : Math.round(num)
  }
  const num = parseInt(stored)
  return isNaN(num) ? defaultVal : num
}

function getLineHeight(): number | null {
  const val = changes['line-height']
  if (!val) return null
  const num = parseFloat(val)
  return isNaN(num) ? null : num
}

function getColorForDisplay(prop: string): string {
  const val = changes[prop]
  if (!val) return '#000000'
  return val
}

function getShadowLabel(): string {
  const val = changes['box-shadow']
  if (!val) return 'none'
  return reverseShadowMap[val] || 'custom'
}

function isSwatchActive(color: string): boolean {
  const targetKey = activeColorField.value === 'bg' ? 'background-color'
    : activeColorField.value === 'text' ? 'color'
    : 'border-color'
  return changes[targetKey]?.toLowerCase() === color.toLowerCase()
}

function applyPresetColor(color: string) {
  const targetKey = activeColorField.value === 'bg' ? 'background-color'
    : activeColorField.value === 'text' ? 'color'
    : 'border-color'
  setChange(targetKey, color)
}

function handleReset() {
  const keys = Object.keys(changes)
  for (const key of keys) {
    delete changes[key]
  }
  if (props.elementInfo) {
    for (const key of keys) {
      emit('update:style', {
        selector: props.elementInfo.selector,
        property: key,
        value: '',
      })
    }
  }
}

function handleApply() {
  if (!props.elementInfo) return

  const changeList = Object.entries(changes).map(([property, value]) => ({
    property,
    value,
  }))

  if (changeList.length === 0) return

  emit('update:style:batch', {
    selector: props.elementInfo.selector,
    changes: changeList,
  })
}

function handleBeforeUpload(file: File): boolean {
  emit('upload:image', file)
  const reader = new FileReader()
  reader.onload = (e) => {
    if (e.target?.result) {
      setChange('background-image', `url(${e.target.result})`)
    }
  }
  reader.readAsDataURL(file)
  return false
}

function removeBackgroundImage() {
  delete changes['background-image']
  if (props.elementInfo) {
    emit('update:style', {
      selector: props.elementInfo.selector,
      property: 'background-image',
      value: '',
    })
  }
}

watch(
  () => props.elementInfo,
  () => {
    const keys = Object.keys(changes)
    for (const key of keys) {
      delete changes[key]
    }
    activeColorField.value = 'bg'
  }
)

watch(
  () => props.visible,
  (val) => {
    if (!val) {
      const keys = Object.keys(changes)
      for (const key of keys) {
        delete changes[key]
      }
    }
  }
)
</script>

<style scoped>
.style-editor-panel {
  width: 100%;
  height: 100%;
  background: rgba(15, 15, 25, 0.95);
  border: 1px solid rgba(0, 255, 157, 0.1);
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid rgba(0, 255, 157, 0.1);
  flex-shrink: 0;
}

.panel-title {
  font-size: 14px;
  font-weight: 600;
  color: #ffffff;
  font-family: 'Noto Sans SC', sans-serif;
}

.close-btn {
  color: rgba(255, 255, 255, 0.4) !important;
}

.close-btn:hover {
  color: #ffffff !important;
}

.panel-empty {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  gap: 12px;
}

.empty-icon {
  font-size: 40px;
  opacity: 0.6;
}

.empty-text {
  color: rgba(255, 255, 255, 0.4);
  font-size: 13px;
  text-align: center;
  line-height: 1.5;
  margin: 0;
}

.element-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: rgba(0, 255, 157, 0.05);
  border-bottom: 1px solid rgba(0, 255, 157, 0.1);
  flex-shrink: 0;
  flex-wrap: wrap;
}

.badge-tag {
  font-size: 12px;
  font-weight: 700;
  color: #00ff9d;
  font-family: 'Orbitron', sans-serif;
  text-transform: uppercase;
}

.badge-id {
  font-size: 11px;
  color: #00c8ff;
  font-family: 'JetBrains Mono', monospace;
}

.badge-class {
  font-size: 11px;
  color: #ff006e;
  font-family: 'JetBrains Mono', monospace;
  word-break: break-all;
}

.panel-body {
  flex: 1;
  overflow-y: auto;
  padding: 0;
}

.cyber-collapse {
  background: transparent !important;
}

.cyber-collapse :deep(.ant-collapse-item) {
  border-bottom: 1px solid rgba(0, 255, 157, 0.08) !important;
}

.cyber-collapse :deep(.ant-collapse-header) {
  padding: 10px 16px !important;
  color: rgba(255, 255, 255, 0.9) !important;
  font-size: 13px !important;
  font-weight: 600 !important;
  font-family: 'Noto Sans SC', sans-serif !important;
  background: rgba(0, 255, 157, 0.02) !important;
  transition: background 0.2s ease !important;
}

.cyber-collapse :deep(.ant-collapse-header:hover) {
  background: rgba(0, 255, 157, 0.05) !important;
}

.cyber-collapse :deep(.ant-collapse-content-box) {
  padding: 0 !important;
}

.section-content {
  padding: 8px 16px 12px;
}

.field-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.field-row:last-child {
  margin-bottom: 0;
}

.field-label {
  width: 72px;
  flex-shrink: 0;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
  font-family: 'Noto Sans SC', sans-serif;
}

.field-control {
  flex: 1;
  min-width: 0;
}

.dim-control {
  display: flex;
  align-items: center;
  gap: 4px;
}

.dim-input {
  flex: 1;
}

.dim-input :deep(.ant-input-number-input) {
  font-size: 12px !important;
  color: rgba(255, 255, 255, 0.9) !important;
}

.unit-select {
  width: 68px;
  flex-shrink: 0;
}

.unit-select :deep(.ant-select-selector) {
  background: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(0, 255, 157, 0.1) !important;
  color: rgba(255, 255, 255, 0.7) !important;
  font-size: 12px !important;
}

.four-dim {
  display: flex;
  gap: 4px;
}

.dim-sm {
  flex: 1;
  min-width: 0;
}

.dim-sm :deep(.ant-input-number-input) {
  font-size: 11px !important;
  padding: 0 4px !important;
  text-align: center !important;
  color: rgba(255, 255, 255, 0.9) !important;
}

.slider-control {
  display: flex;
  align-items: center;
  gap: 8px;
}

.cyber-slider {
  flex: 1;
}

.cyber-slider :deep(.ant-slider-rail) {
  background: rgba(255, 255, 255, 0.1) !important;
}

.cyber-slider :deep(.ant-slider-track) {
  background: linear-gradient(90deg, #00ff9d, #00c8ff) !important;
}

.cyber-slider :deep(.ant-slider-handle) {
  border-color: #00ff9d !important;
  box-shadow: 0 0 6px rgba(0, 255, 157, 0.4) !important;
}

.slider-value {
  width: 32px;
  font-size: 11px;
  color: rgba(255, 255, 255, 0.6);
  text-align: right;
  font-family: 'JetBrains Mono', monospace;
  flex-shrink: 0;
}

.cyber-select {
  width: 100%;
}

.cyber-select :deep(.ant-select-selector) {
  background: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(0, 255, 157, 0.1) !important;
  color: rgba(255, 255, 255, 0.9) !important;
  font-size: 12px !important;
}

.cyber-select :deep(.ant-select-selection-item) {
  color: rgba(255, 255, 255, 0.9) !important;
}

.cyber-input-number {
  width: 100%;
}

.cyber-input-number :deep(.ant-input-number-input) {
  font-size: 12px !important;
  color: rgba(255, 255, 255, 0.9) !important;
}

.cyber-input-number :deep(.ant-input-number) {
  background: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(0, 255, 157, 0.1) !important;
}

.align-btn-group {
  display: flex;
  gap: 2px;
}

.align-btn {
  flex: 1;
  padding: 0 !important;
  width: 32px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.align-btn:not(.ant-btn-primary) {
  background: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(0, 255, 157, 0.1) !important;
  color: rgba(255, 255, 255, 0.6) !important;
}

.align-btn.ant-btn-primary {
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%) !important;
  border: none !important;
  color: #0a0a0f !important;
}

.justify-icon {
  font-size: 12px;
}

.color-field {
  cursor: pointer;
}

.color-input-wrap {
  display: flex;
  align-items: center;
  gap: 6px;
}

.color-picker-native {
  width: 28px;
  height: 28px;
  padding: 0;
  border: 1px solid rgba(0, 255, 157, 0.2);
  border-radius: 6px;
  cursor: pointer;
  background: transparent;
  flex-shrink: 0;
}

.color-picker-native::-webkit-color-swatch-wrapper {
  padding: 2px;
}

.color-picker-native::-webkit-color-swatch {
  border: none;
  border-radius: 4px;
}

.color-hex-input {
  flex: 1;
}

.color-hex-input :deep(.ant-input) {
  font-size: 12px !important;
  font-family: 'JetBrains Mono', monospace !important;
}

.color-presets {
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid rgba(0, 255, 157, 0.08);
}

.presets-label {
  display: block;
  font-size: 11px;
  color: rgba(255, 255, 255, 0.4);
  margin-bottom: 6px;
}

.presets-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.preset-swatch {
  width: 22px;
  height: 22px;
  border-radius: 50%;
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.2s ease;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preset-swatch:hover {
  transform: scale(1.2);
  border-color: rgba(255, 255, 255, 0.5);
}

.preset-swatch.active {
  border-color: #00ff9d;
  box-shadow: 0 0 8px rgba(0, 255, 157, 0.4);
}

.swatch-check {
  font-size: 10px;
  color: white;
  text-shadow: 0 0 3px rgba(0, 0, 0, 0.8);
}

.upload-area {
  display: flex;
  gap: 12px;
  align-items: center;
}

.upload-btn {
  background: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(0, 255, 157, 0.2) !important;
  color: #00ff9d !important;
  font-size: 12px !important;
}

.upload-btn:hover {
  background: rgba(0, 255, 157, 0.1) !important;
  border-color: rgba(0, 255, 157, 0.4) !important;
}

.bg-preview-wrap {
  position: relative;
  flex-shrink: 0;
}

.bg-preview {
  width: 60px;
  height: 48px;
  border-radius: 6px;
  border: 1px solid rgba(0, 255, 157, 0.2);
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;
}

.bg-remove-btn {
  position: absolute;
  top: -6px;
  right: -6px;
  width: 18px;
  height: 18px;
  min-width: 0;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 0, 0, 0.8) !important;
  border: none !important;
  color: white !important;
  font-size: 10px;
  border-radius: 50%;
}

.bg-preview-empty {
  width: 60px;
  height: 48px;
  border-radius: 6px;
  border: 1px dashed rgba(255, 255, 255, 0.15);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 2px;
  color: rgba(255, 255, 255, 0.25);
  font-size: 9px;
  flex-shrink: 0;
}

.preview-icon {
  font-size: 16px;
}

.panel-actions {
  display: flex;
  gap: 8px;
  padding: 12px 16px;
  border-top: 1px solid rgba(0, 255, 157, 0.1);
  flex-shrink: 0;
}

.reset-btn {
  background: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(255, 255, 255, 0.15) !important;
  color: rgba(255, 255, 255, 0.6) !important;
  font-size: 12px !important;
  height: 32px !important;
}

.reset-btn:hover {
  border-color: rgba(255, 255, 255, 0.3) !important;
  color: rgba(255, 255, 255, 0.9) !important;
}

.apply-btn {
  background: linear-gradient(135deg, #00ff9d 0%, #00c8ff 100%) !important;
  border: none !important;
  color: #0a0a0f !important;
  font-weight: 600 !important;
  font-size: 12px !important;
  height: 32px !important;
  box-shadow: 0 0 20px rgba(0, 255, 157, 0.3) !important;
}

.apply-btn:hover {
  box-shadow: 0 0 30px rgba(0, 255, 157, 0.5) !important;
}

.panel-body::-webkit-scrollbar {
  width: 4px;
}

.panel-body::-webkit-scrollbar-track {
  background: transparent;
}

.panel-body::-webkit-scrollbar-thumb {
  background: rgba(0, 255, 157, 0.2);
  border-radius: 2px;
}
</style>
