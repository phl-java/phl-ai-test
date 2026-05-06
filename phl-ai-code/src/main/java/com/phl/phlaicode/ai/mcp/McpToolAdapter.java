package com.phl.phlaicode.ai.mcp;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class McpToolAdapter {

    public static ToolSpecification toToolSpecification(Object toolInstance, Method method) {
        Tool toolAnnotation = method.getAnnotation(Tool.class);
        if (toolAnnotation == null) return null;

        String description = String.join("", toolAnnotation.value());
        return ToolSpecification.builder()
                .name(toolAnnotation.name())
                .description(description)
                .build();
    }

    public static Object[] adaptTools(List<Object> mcpTools) {
        return mcpTools.toArray();
    }
}
