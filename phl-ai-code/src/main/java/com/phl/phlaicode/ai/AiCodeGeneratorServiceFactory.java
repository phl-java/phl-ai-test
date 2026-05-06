package com.phl.phlaicode.ai;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.phl.phlaicode.ai.guardrail.PromptSafetyInputGuardrail;
import com.phl.phlaicode.ai.guardrail.RetryOutputGuardrail;
import com.phl.phlaicode.ai.tools.*;
import com.phl.phlaicode.exception.BusinessException;
import com.phl.phlaicode.exception.ErrorCode;
import com.phl.phlaicode.model.enums.CodeGenTypeEnum;
import com.phl.phlaicode.service.ChatHistoryService;
import com.phl.phlaicode.util.SpringContextUtil;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

//工厂模式

/**
 * AI 服务创建工厂
 */
@Configuration
@Slf4j
public class AiCodeGeneratorServiceFactory {

    @Resource(name = "openAiChatModel")
    private ChatModel chatModel;

    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    @Resource
    private ChatHistoryService chatHistoryService;

    @Resource
    private ToolManager toolManager;

    /**
     * AI 服务实例缓存
     * 缓存策略：
     * - 最大缓存 1000 个实例
     * - 写入后 30 分钟过期
     * - 访问后 10 分钟过期                       map 缓存服务
     */
    private final Cache<String, AiCodeGeneratorService> serviceCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(30))//30分钟过期
            .expireAfterAccess(Duration.ofMinutes(10))//访问后10分钟过期
            .removalListener((key, value, cause) -> {
                log.debug("AI 服务实例被移除，缓存键: {}, 原因: {}", key, cause);
            })
            .build();


    /**
     * 根据 appId 获取服务（为了兼容老逻辑）
     *
     * @param appId
     * @return
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId) {
        return getAiCodeGeneratorService(appId, CodeGenTypeEnum.HTML, null);
    }

    /**
     * 根据 appId 获取服务
     *
     * @param appId       应用 id
     * @param codeGenType 生成类型
     * @return
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenType) {
        String cacheKey = buildCacheKey(appId, codeGenType, null);
        return serviceCache.get(cacheKey, key -> createAiCodeGeneratorService(appId, codeGenType, null));
    }

    public AiCodeGeneratorService getAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenType, String modelType) {
        String cacheKey = buildCacheKey(appId, codeGenType, modelType);
        return serviceCache.get(cacheKey, key -> createAiCodeGeneratorService(appId, codeGenType, modelType));
    }

    /**
     * 创建新的 AI 服务实例
     *
     * @param appId           应用程序 ID
     * @param codeGenTypeEnum 代码生成类型
     * @param modelType       模型类型（qwen / deepseek）
     * @return AI 代码生成服务
     */
    private AiCodeGeneratorService createAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenTypeEnum, String modelType) {
        log.info("为appid： {} 创建新的 AI 代码生成服务", appId);
        //根据id构建独立会话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                .id(appId)
                .chatMemoryStore(redisChatMemoryStore)
                .maxMessages(20)
                .build();
        //从数据库中加载对话历史到记忆中
        chatHistoryService.loadChatHistoryToMemory(appId, chatMemory, 20);

        ModelTypeEnum modelTypeEnum = ModelTypeEnum.getEnumByValue(modelType);

        return switch (codeGenTypeEnum) {
            // Vue 项目生成，使用流式对话模型
            case VUE_PROJECT -> {
                StreamingChatModel reasoningStreamingChatModel = modelTypeEnum == ModelTypeEnum.DEEPSEEK
                        ? SpringContextUtil.getBean("deepSeekStreamingChatModel", StreamingChatModel.class)
                        : SpringContextUtil.getBean("reasoningStreamingChatModelPrototype", StreamingChatModel.class);
                ChatModel vueChatModel = modelTypeEnum == ModelTypeEnum.DEEPSEEK
                        ? SpringContextUtil.getBean("deepSeekChatModel", ChatModel.class)
                        : chatModel;
                yield  AiServices.builder(AiCodeGeneratorService.class)
                    .chatModel(vueChatModel)
                    .streamingChatModel(reasoningStreamingChatModel)
                    .chatMemoryProvider(memoryId -> chatMemory)
                    .tools(toolManager.getAllTools())
                    //处理工具调用幻觉问题
                    .hallucinatedToolNameStrategy(toolExecutionRequest ->
                            ToolExecutionResultMessage.from(toolExecutionRequest,
                                    "Error:there is no tool called"+toolExecutionRequest.name()))
                    .inputGuardrails(new PromptSafetyInputGuardrail())//输入护轨
//                    .outputGuardrails(new RetryOutputGuardrail())//输出护轨，为了流式输出这里不使用护轨
                    .build();
            }
            // HTML 和 多文件生成，使用流式对话模型
            case HTML, MULTI_FILE ->{
                StreamingChatModel openAiStreamingChatModel = modelTypeEnum == ModelTypeEnum.DEEPSEEK
                        ? SpringContextUtil.getBean("deepSeekStreamingChatModel", StreamingChatModel.class)
                        : SpringContextUtil.getBean("streamingChatModelPrototype", StreamingChatModel.class);
                ChatModel htmlChatModel = modelTypeEnum == ModelTypeEnum.DEEPSEEK
                        ? SpringContextUtil.getBean("deepSeekChatModel", ChatModel.class)
                        : chatModel;
                yield  AiServices.builder(AiCodeGeneratorService.class)
                    .chatModel(htmlChatModel)
                    .streamingChatModel(openAiStreamingChatModel)
                    .chatMemoryProvider(memoryId -> chatMemory)
                    .inputGuardrails(new PromptSafetyInputGuardrail())//输入护轨
//                    .outputGuardrails(new RetryOutputGuardrail())//输出护轨
                    .build();
            }
            default ->
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型" + codeGenTypeEnum.getValue());
        };
    }

    /**
     * 创建 AI 代码生成服务
     *
     * @return AI 代码生成服务
     */
    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService() {
        return getAiCodeGeneratorService(0);
    }

    /**
     * 构建缓存键
     *
     * @param appId
     * @param codeGenTypeEnum
     * @return
     */
    private String buildCacheKey(long appId, CodeGenTypeEnum codeGenTypeEnum, String modelType) {
        return appId + "_" + codeGenTypeEnum.getValue() + "_" + (modelType != null ? modelType : "qwen");
    }


}
