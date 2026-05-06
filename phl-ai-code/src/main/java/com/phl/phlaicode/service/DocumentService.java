package com.phl.phlaicode.service;

import com.phl.phlaicode.model.entity.Document;
import java.util.List;

public interface DocumentService {
    Long addDocument(Document document);
    List<Document> listByAppId(Long appId);
    Document getById(Long id);
    boolean deleteById(Long id);
}
