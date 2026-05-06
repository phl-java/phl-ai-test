package com.phl.phlaicode.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.phl.phlaicode.mapper.DocumentMapper;
import com.phl.phlaicode.model.entity.Document;
import com.phl.phlaicode.service.DocumentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Resource
    private DocumentMapper documentMapper;

    @Override
    public Long addDocument(Document document) {
        document.setCreateTime(LocalDateTime.now());
        document.setUpdateTime(LocalDateTime.now());
        document.setIsDelete(0);
        documentMapper.insert(document);
        return document.getId();
    }

    @Override
    public List<Document> listByAppId(Long appId) {
        return documentMapper.selectListByQuery(
            QueryWrapper.create()
                .eq("appId", appId)
                .eq("isDelete", 0)
        );
    }

    @Override
    public Document getById(Long id) {
        return documentMapper.selectOneById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        Document doc = new Document();
        doc.setId(id);
        doc.setIsDelete(1);
        return documentMapper.update(doc) > 0;
    }
}
