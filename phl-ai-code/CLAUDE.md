# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

# PHL-AI-CODE - AI 代码生成平台

## 项目简介

PHL-AI-CODE 是一个基于 AI 的代码生成平台，用户通过自然语言描述需求，AI 自动生成前端代码并支持一键部署和在线预览。支持三种代码生成模式：HTML 单文件、HTML+CSS+JS 多文件、Vue3 工程项目。

## 技术栈

| 层面 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 3.5.10 |
| 开发语言 | Java | 21 |
| AI 框架 | LangChain4j | 1.1.0 |
| AI 模型 | Qwen (qwen3.5-plus via Alibaba Cloud) | - |
| ORM | MyBatis-Flex | 1.11.1 |
| 数据库 | MySQL | - |
| 缓存 | Redis + Caffeine | - |
| Session | Spring Session + Redis | - |
| API 文档 | Knife4j (OpenAPI3) | 4.5.0 |
| 对象存储 | 腾讯云 COS | 5.6.227 |
| 网页截图 | Selenium + WebDriverManager | 4.33.0 / 6.1.0 |
| 工具库 | Hutool | 5.8.38 |
| 响应式 | Reactor (Flux/Mono) | - |

## 项目结构

```
com.phl.phlaicode
├── PhlAiCodeApplication.java              # 启动类
├── ai/                                     # AI 核心模块
│   ├── AiCodeGeneratorService.java         # AI 代码生成服务接口
│   ├── AiCodeGeneratorServiceFactory.java  # AI 服务工厂（Caffeine 缓存）
│   ├── model/
│   │   ├── HtmlCodeResult.java             # HTML 代码结果模型
│   │   ├── MultiFileCodeResult.java        # 多文件代码结果模型
│   │   └── message/                        # 流式消息模型
│   │       ├── StreamMessage.java
│   │       ├── AiResponseMessage.java
│   │       ├── ToolRequestMessage.java
│   │       ├── ToolExecutedMessage.java
│   │       └── StreamMessageTypeEnum.java
│   └── tools/                              # AI 工具（供 AI 调用）
│       ├── FileWriteTool.java              # 文件写入
│       ├── FileReadTool.java               # 文件读取
│       ├── FileModifyTool.java             # 文件修改
│       ├── FileDeleteTool.java             # 文件删除
│       ├── FileDirReadTool.java            # 目录读取
│       └── ExitTool.java                   # 退出工具
├── annotation/
│   └── AuthCheck.java                      # 权限校验注解
├── aop/
│   └── AuthInterceptor.java                # 权限拦截器（AOP 环绕通知）
├── common/                                 # 公共类
│   ├── BaseResponse.java                   # 统一响应封装
│   ├── ResultUtils.java                    # 响应工具类
│   ├── DeleteRequest.java
│   └── PageRequest.java
├── config/                                 # 配置类
│   ├── CorsConfig.java                     # 跨域配置
│   ├── CosClientConfig.java                # 腾讯云 COS 配置
│   ├── RedisChatMemoryStoreConfig.java     # Redis 对话记忆存储配置
│   └── ReasoningStreamingChatModelConfig.java # 推理模型配置
├── constant/
│   ├── AppConstant.java                    # 应用常量
│   └── UserConstant.java                   # 用户常量
├── controller/                             # 控制器层
│   ├── AppController.java                  # 应用控制器
│   ├── UserController.java                 # 用户控制器
│   ├── ChatHistoryController.java          # 对话历史控制器
│   └── StaticResourceController.java       # 静态资源控制器
├── core/                                   # 核心业务模块
│   ├── AiCodeGeneratorFacade.java          # AI 代码生成门面（统一入口）
│   ├── CodeFileSaver.java
│   ├── builder/
│   │   └── VueProjectBuilder.java          # Vue 项目构建器
│   ├── handler/                            # 流式处理器
│   │   ├── StreamHandlerExecutor.java
│   │   ├── JsonMessageStreamHandler.java   # JSON 消息流处理器（Vue 项目）
│   │   └── SimpleTextStreamHandler.java    # 简单文本流处理器（HTML/多文件）
│   ├── parser/                             # 代码解析器
│   │   ├── CodeParser.java                 # 接口
│   │   ├── CodeParserExecutor.java
│   │   ├── HtmlCodeParser.java
│   │   └── MultiFileCodeParser.java
│   └── saver/                              # 代码保存器（模板方法模式）
│       ├── CodeFileSaverTemplate.java
│       ├── CodeFileSaverExecutor.java
│       ├── HtmlCodeFileSaverTemplate.java
│       └── MultiFileCodeFileSaverTemplate.java
├── exception/                              # 异常处理
│   ├── ErrorCode.java
│   ├── BusinessException.java
│   ├── GlobalExceptionHandler.java
│   └── ThrowUtils.java
├── generator/
│   └── MyBatisCodeGenerator.java           # MyBatis 代码生成器
├── manager/
│   └── CosManager.java                     # COS 对象存储管理器
├── mapper/                                 # Mapper 接口
│   ├── AppMapper.java
│   ├── UserMapper.java
│   └── ChatHistoryMapper.java
├── model/
│   ├── dto/                                # 数据传输对象
│   │   ├── app/
│   │   ├── user/
│   │   └── chathistory/
│   ├── entity/                             # 实体类
│   │   ├── App.java
│   │   ├── User.java
│   │   └── ChatHistory.java
│   ├── enums/                              # 枚举
│   │   ├── CodeGenTypeEnum.java
│   │   ├── ChatHistoryMessageTypeEnum.java
│   │   └── UserRoleEnum.java
│   └── vo/                                 # 视图对象
│       ├── AppVO.java
│       ├── UserVO.java
│       └── LoginUserVO.java
├── service/                                # 服务层
│   ├── AppService.java
│   ├── UserService.java
│   ├── ChatHistoryService.java
│   └── impl/
│       ├── AppServiceImpl.java
│       ├── UserServiceImpl.java
│       └── ChatHistoryServiceImpl.java
└── util/
    └── WebScreenshotUtils.java             # 网页截图工具
```

## 核心业务流程

### 应用创建
1. 用户提交 `initPrompt`（需求描述）
2. 系统创建应用记录，应用名称取 initPrompt 前 12 位
3. 默认代码生成类型为 `VUE_PROJECT`
4. 返回应用 ID

### AI 代码生成（核心）
```
用户发送消息
  → AppController.chatToGenCode(appId, message)
    → AppService.chatToGenCode()
      → 1. 参数校验、权限校验
      → 2. 保存用户消息到 chat_history
      → 3. AiCodeGeneratorFacade.generateAndSaveCodeStream()
        → 根据 codeGenType 分发：
          ├── HTML / MULTI_FILE
          │   → AiCodeGeneratorService（流式调用 Qwen）
          │   → 收集完整代码 → 解析 → 保存文件
          └── VUE_PROJECT
            → AiCodeGeneratorService（带工具调用的流式推理）
            → AI 通过 FileWriteTool 逐个写入项目文件
      → 4. StreamHandlerExecutor 分发流式处理
          ├── VUE_PROJECT → JsonMessageStreamHandler
          └── HTML / MULTI_FILE → SimpleTextStreamHandler
      → 5. SSE 流式返回给前端
```

### 代码生成模式
| 模式 | 枚举值 | AI 模型 | 说明 |
|------|--------|---------|------|
| HTML 单文件 | `html` | qwen3.5-plus (streaming) | 生成单个 HTML 文件，JSON 格式返回 |
| 多文件 | `multi_file` | qwen3.5-plus (streaming) | 生成 HTML + CSS + JS 三个文件，JSON 格式返回 |
| Vue 工程 | `vue_project` | qwen3.5-plus (reasoning) | 生成完整 Vue3 + Vite 工程，通过工具调用逐文件写入 |

### 应用部署
1. 用户请求部署应用
2. 系统生成/复用 `deployKey`（6 位随机字符串）
3. Vue 项目：执行 `npm install` + `npm run build`
4. 复制产物到部署目录 `{CODE_DEPLOY_ROOT_DIR}/{deployKey}/`
5. 返回访问 URL

### 对话记忆
- 使用 **Redis** 作为 LangChain4j 的 ChatMemoryStore 持久化存储
- 每个应用独立的对话记忆窗口（最多 20 条消息）
- 使用 **Caffeine** 本地缓存 AI 服务实例（最多 1000 个，30 分钟过期）
- 启动时从数据库加载历史对话到内存

## API 接口

接口根路径：`/api`，端口：`8123`

### 应用相关
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/app/add` | 创建应用 | 登录用户 |
| POST | `/app/update` | 更新应用 | 应用所有者 |
| POST | `/app/delete` | 删除应用 | 应用所有者 |
| POST | `/app/get` | 获取应用详情 | 无 |
| POST | `/app/list/page/vo` | 分页查询应用列表 | 无 |
| POST | `/app/my/list/page/vo` | 查询我的应用 | 登录用户 |
| POST | `/app/chatToGenCode` | AI 对话生成代码（SSE） | 应用所有者 |
| POST | `/app/deploy` | 部署应用 | 应用所有者 |
| POST | `/app/admin/update` | 管理员更新应用 | 管理员 |

### 用户相关
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/user/register` | 用户注册 | 无 |
| POST | `/user/login` | 用户登录 | 无 |
| POST | `/user/get/login` | 获取当前登录用户 | 登录用户 |
| POST | `/user/logout` | 用户登出 | 登录用户 |
| POST | `/user/update` | 更新用户信息 | 登录用户 |
| POST | `/user/list/page/vo` | 分页查询用户列表 | 管理员 |

### 对话历史相关
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/chat_history/list` | 查询应用对话历史 | 无 |

### 静态资源
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/static/{deployKey}/[fileName]` | 访问已部署的静态资源 |

## 配置说明

### application.yml
```yaml
server:
  port: 8123
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yu_ai_code_mother
    driver-class-name: com.mysql.cj.jdbc.Driver
  data:
    redis:
      host: localhost
      port: 6379
  session:
    store-type: redis
    timeout: 30d
```

### application-local.yml
需要配置以下敏感信息：
```yaml
langchain4j:
  open-ai:
    chat-model:
      api-key: ${QWEN_API_KEY}
    streaming-chat-model:
      api-key: ${QWEN_API_KEY}
    reasoning-streaming-chat-model:
      api-key: ${QWEN_API_KEY}

cos:
  client:
    accessKey: ${COS_ACCESS_KEY}
    secretKey: ${COS_SECRET_KEY}
    bucket: phl-ai-code-1330784580
    region: ap-beijing
```

## 安全机制

| 机制 | 实现方式 |
|------|----------|
| 认证 | Session + Cookie（30 天有效期） |
| 授权 | AOP + `@AuthCheck` 注解，支持管理员角色校验 |
| 密码加密 | MD5 + 盐值 |
| 逻辑删除 | 所有实体使用 `isDelete` 字段 |
| 跨域 | CorsConfig 全局开放 |

## 设计模式

| 模式 | 位置 | 说明 |
|------|------|------|
| 门面模式 | `AiCodeGeneratorFacade` | 统一代码生成入口，屏蔽内部复杂性 |
| 工厂模式 | `AiCodeGeneratorServiceFactory` | 根据 appId 和生成类型创建 AI 服务实例 |
| 模板方法模式 | `CodeFileSaverTemplate` | 代码文件保存的标准流程，子类实现具体逻辑 |
| 策略模式 | `StreamHandlerExecutor` / `CodeParserExecutor` | 根据类型选择流处理器/解析器 |
| 建造者模式 | LangChain4j `AiServices.builder()` | 构建 AI 服务实例 |

## Prompt 模板
位于 `src/main/resources/prompt/` 目录：

| 文件 | 说明 |
|------|------|
| `codegen-html-system-prompt.txt` | HTML 单文件生成提示词，要求返回 JSON 格式 |
| `codegen-multi-file-system-prompt.txt` | 多文件生成提示词，要求返回 JSON 格式 |
| `codegen-vue-project-system-prompt.txt` | Vue 工程生成提示词，要求通过文件写入工具创建项目 |

Vue 工程默认技术栈：Vue3 + Vite + Vue Router（hash 路由模式）

## 常用命令

- `mvn clean compile` - 编译项目
- `mvn clean test` - 运行测试
- `mvn clean package` - 打包项目
- `mvn spring-boot:run` - 启动项目
- `mvn clean install` - 安装依赖
- `java -jar target/phl-ai-code-0.0.1-SNAPSHOT.jar` - 运行打包后的应用

## 环境要求
- JDK 21
- MySQL 8.0+
- Redis 6.0+
- Node.js 18+（Vue 项目构建需要）