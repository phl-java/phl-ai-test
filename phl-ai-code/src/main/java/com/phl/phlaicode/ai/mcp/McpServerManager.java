package com.phl.phlaicode.ai.mcp;

import com.phl.phlaicode.ai.mcp.tools.DatabaseQueryTool;
import com.phl.phlaicode.ai.mcp.tools.FileSearchTool;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class McpServerManager {

    private final Map<String, Object> tools = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("MCP Server Manager 初始化完成");
        registerBuiltinTools();
    }

    public void registerTool(String name, Object toolInstance) {
        tools.put(name, toolInstance);
        log.info("MCP 工具已注册: {}", name);
    }

    public Object getTool(String name) {
        return tools.get(name);
    }

    public Map<String, Object> getAllTools() {
        return tools;
    }

    @PreDestroy
    public void shutdown() {
        log.info("MCP Server Manager 关闭");
        tools.clear();
    }

    private void registerBuiltinTools() {
        registerTool("database_query", new DatabaseQueryTool());
        registerTool("file_search", new FileSearchTool());
    }
}
