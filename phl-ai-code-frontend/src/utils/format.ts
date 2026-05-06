/**
 * 格式化工具函数
 */

/**
 * 确保 URL 包含协议前缀
 * @param url 输入的 URL
 * @param protocol 默认协议，默认为 'https://'
 * @returns 完整的 URL
 */
export function ensureProtocol(url: string, protocol: string = 'https://'): string {
  if (!url) {
    return url;
  }

  // 检查是否已经有协议前缀
  if (/^https?:\/\//i.test(url)) {
    return url;
  }

  // 检查是否为根路径或相对路径
  if (url.startsWith('/') || url.startsWith('./') || url.startsWith('../')) {
    return url;
  }

  // 添加协议前缀
  return protocol + url;
}

/**
 * 格式化封面图片 URL
 * @param coverUrl 封面图片 URL
 * @returns 格式化后的完整 URL
 */
export function formatCoverUrl(coverUrl: string): string {
  return ensureProtocol(coverUrl, 'https://');
}