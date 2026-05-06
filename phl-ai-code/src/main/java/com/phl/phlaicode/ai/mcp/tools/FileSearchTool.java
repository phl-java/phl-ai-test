package com.phl.phlaicode.ai.mcp.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.File;

@Slf4j
@Component
public class FileSearchTool {

    @Tool("在指定目录中搜索匹配模式的文件（支持 glob 通配符）")
    public String searchFiles(
        @P("要搜索的目录路径") String directory,
        @P("文件名称模式，如 *.vue、*.java、*Controller*") String pattern
    ) {
        log.info("文件搜索: dir={}, pattern={}", directory, pattern);
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            return "错误：目录不存在: " + directory;
        }

        StringBuilder result = new StringBuilder();
        searchRecursive(dir, pattern.replace("*", ""), result);

        if (result.length() == 0) {
            return "未找到匹配的文件: " + pattern;
        }
        return "找到以下文件:\n" + result.toString();
    }

    private void searchRecursive(File dir, String pattern, StringBuilder result) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                searchRecursive(f, pattern, result);
            } else if (pattern.isEmpty() || f.getName().contains(pattern)) {
                result.append(f.getAbsolutePath()).append("\n");
            }
        }
    }
}
