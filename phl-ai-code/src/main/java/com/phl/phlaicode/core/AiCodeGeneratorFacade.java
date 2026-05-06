package com.phl.phlaicode.core;


import cn.hutool.json.JSONUtil;
import com.phl.phlaicode.ai.AiCodeGeneratorService;
import com.phl.phlaicode.ai.AiCodeGeneratorServiceFactory;
import com.phl.phlaicode.ai.model.HtmlCodeResult;
import com.phl.phlaicode.ai.model.MultiFileCodeResult;
import com.phl.phlaicode.ai.model.message.AiResponseMessage;
import com.phl.phlaicode.ai.model.message.BuildStatusMessage;
import com.phl.phlaicode.ai.model.message.ToolExecutedMessage;
import com.phl.phlaicode.ai.model.message.ToolRequestMessage;
import com.phl.phlaicode.constant.AppConstant;
import com.phl.phlaicode.core.builder.VueProjectBuilder;
import com.phl.phlaicode.core.parser.CodeParserExecutor;
import com.phl.phlaicode.core.saver.CodeFileSaverExecutor;
import com.phl.phlaicode.exception.BusinessException;
import com.phl.phlaicode.exception.ErrorCode;
import com.phl.phlaicode.model.enums.CodeGenTypeEnum;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * 门面模式,后面要调用方法都走这里
 * AI 代码生成门面类，组合代码生成和保存功能
 */
@Slf4j
@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;

    @Resource
    private VueProjectBuilder vueProjectBuilder;

    @Resource
    private AutoDebugLoop autoDebugLoop;

    /**
     * 统一入口：根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @param appId           应用 ID
     * @param modelType       模型类型（qwen / deepseek）
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId, String modelType) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "生成类型不能为空");
        }
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum, modelType);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 统一入口：根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @param appId           应用 ID
     * @param modelType       模型类型（qwen / deepseek）
     * @return 保存的目录
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId, String modelType) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "生成类型不能为空");
        }
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum, modelType);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            case VUE_PROJECT -> {
                TokenStream tokenStream = aiCodeGeneratorService.generateVueProjectCodeStream(appId, userMessage);
                yield processTokenStream(tokenStream, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 将 TokenStream 转换为 Flux<String>，并传递工具调用信息
     *
     * @param tokenStream TokenStream 对象
//     * @param appId       应用 ID
     * @return Flux<String> 流式响应
     */
    private Flux<String> processTokenStream(TokenStream tokenStream,Long appId) {
        return Flux.create(sink -> {
            tokenStream.onPartialResponse((String partialResponse) -> {
                        AiResponseMessage aiResponseMessage = new AiResponseMessage(partialResponse);
                        sink.next(JSONUtil.toJsonStr(aiResponseMessage));
                    })
                    .onPartialToolExecutionRequest((index, toolExecutionRequest) -> {
                        log.debug("部分工具调用请求：index={}, name={}, args={}", 
                            index, toolExecutionRequest.name(), toolExecutionRequest.arguments());
                        
                        ToolRequestMessage toolRequestMessage = new ToolRequestMessage(toolExecutionRequest);
                        sink.next(JSONUtil.toJsonStr(toolRequestMessage));
                    })
                    .onCompleteToolExecutionRequest((index, toolExecutionRequest) -> {
                        log.info("完整工具调用请求：index={}, name={}, args={}", 
                            index, toolExecutionRequest.name(), toolExecutionRequest.arguments());
                    })
                    .onToolExecuted((ToolExecution toolExecution) -> {
                        log.info("工具已执行：{}", toolExecution.request().name());
                        ToolExecutedMessage toolExecutedMessage = new ToolExecutedMessage(toolExecution);
                        sink.next(JSONUtil.toJsonStr(toolExecutedMessage));
                    })
                    .onCompleteResponse(response -> {
                        log.info("AI 响应完成，开始构建...");
                        String projectPath = AppConstant.CODE_OUTPUT_ROOT_DIR + "/vue_project_" + appId;

                        sink.next(JSONUtil.toJsonStr(
                            new BuildStatusMessage("build_status", "BUILDING", "正在构建项目...", 0, 3)));

                        try {
                            vueProjectBuilder.buildProject(projectPath);

                            DebugResult debugResult = autoDebugLoop.run(projectPath, () -> {
                                log.info("构建失败，等待 AI 修复...");
                            });

                            if (debugResult.isSuccess()) {
                                sink.next(JSONUtil.toJsonStr(
                                    new BuildStatusMessage("build_status", "DONE", "构建成功", debugResult.getRetryCount(), 3)));
                            } else {
                                sink.next(JSONUtil.toJsonStr(
                                    new BuildStatusMessage("build_status", "FAILED", "构建失败", debugResult.getRetryCount(), 3)));
                            }
                        } catch (Exception e) {
                            log.error("构建过程异常", e);
                            sink.next(JSONUtil.toJsonStr(
                                new BuildStatusMessage("build_status", "FAILED", "构建异常: " + e.getMessage(), 0, 3)));
                        }

                        sink.complete();
                    })
                    .onError((Throwable error) -> {
                        log.error("AI 响应错误", error);
                        error.printStackTrace();
                        sink.error(error);
                    })
                    .start();
        });
    }


    /**
     * 通用流式代码处理方法
     *
     * @param codeStream  代码流
     * @param codeGenType 代码生成类型
     * @param appId 应用 ID
     * @return 流式响应
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenType, Long appId) {
        // 字符串拼接器，用于当流式返回所有的代码之后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream.doOnNext(chunk -> {
            // 实时收集代码片段
            codeBuilder.append(chunk);
        }).doOnComplete(() -> {
            // 流式返回完成后，保存代码
            try {
                String completeCode = codeBuilder.toString();
                // 使用执行器解析代码
                Object parsedResult = CodeParserExecutor.executeParser(completeCode, codeGenType);
                // 使用执行器保存代码
                File saveDir = CodeFileSaverExecutor.executeSaver(parsedResult, codeGenType, appId);
                log.info("保存成功，目录为：{}", saveDir.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存失败: {}", e.getMessage());
            }
        });
    }
}
