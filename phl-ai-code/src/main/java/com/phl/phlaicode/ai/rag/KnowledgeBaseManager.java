package com.phl.phlaicode.ai.rag;

import com.phl.phlaicode.model.entity.Document;
import com.phl.phlaicode.service.DocumentService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class KnowledgeBaseManager {

    @Resource
    private DocumentService documentService;

    @Tool("上传文档到应用知识库，供 AI 生成代码时参考")
    public String ingestDocument(
        @P("应用 ID") Long appId,
        @P("文档内容") String content,
        @P("文档文件名") String fileName
    ) {
        Document doc = new Document();
        doc.setAppId(appId);
        doc.setFileName(fileName);
        doc.setFileType(fileName.substring(fileName.lastIndexOf('.') + 1));
        doc.setContent(content);
        documentService.addDocument(doc);
        log.info("文档已导入知识库: appId={}, fileName={}", appId, fileName);
        return "文档已成功导入知识库: " + fileName;
    }

    @Tool("从应用知识库中搜索相关文档内容，用于代码生成参考")
    public String searchKnowledge(
        @P("应用 ID") Long appId,
        @P("搜索关键词") String query,
        @P("返回结果数量上限，默认 3") Integer topK
    ) {
        if (topK == null || topK <= 0) topK = 3;
        List<Document> docs = documentService.listByAppId(appId);
        List<String> results = new ArrayList<>();
        for (Document doc : docs) {
            if (doc.getContent() != null && doc.getContent().contains(query)) {
                results.add("【" + doc.getFileName() + "】\n" + doc.getContent());
                if (results.size() >= topK) break;
            }
        }

        if (results.isEmpty()) {
            return "未在知识库中找到匹配 \"" + query + "\" 的文档";
        }
        return "找到以下相关文档：\n\n" + String.join("\n---\n", results);
    }
}
