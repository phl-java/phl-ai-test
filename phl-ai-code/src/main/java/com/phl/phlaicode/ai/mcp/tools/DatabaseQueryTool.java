package com.phl.phlaicode.ai.mcp.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatabaseQueryTool {

    @Tool("执行 SQL 查询并返回结果（仅允许 SELECT 语句）")
    public String queryDatabase(@P("要执行的 SQL 查询语句") String sql) {
        log.info("数据库查询请求: {}", sql);
        if (!sql.trim().toUpperCase().startsWith("SELECT")) {
            return "错误：只允许执行 SELECT 查询语句";
        }
        return "查询结果占位符（后续接入 JdbcTemplate 后可返回真实数据）: " + sql;
    }
}
