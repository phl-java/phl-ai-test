package com.phl.phlaicode.controller;

import com.phl.phlaicode.annotation.AuthCheck;
import com.phl.phlaicode.common.BaseResponse;
import com.phl.phlaicode.common.ResultUtils;
import com.phl.phlaicode.model.entity.Document;
import com.phl.phlaicode.service.DocumentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentController {

    @Resource
    private DocumentService documentService;

    @PostMapping("/upload")
    public BaseResponse<Long> uploadDocument(
        @RequestParam Long appId,
        @RequestParam("file") MultipartFile file) throws IOException {

        Document doc = new Document();
        doc.setAppId(appId);
        doc.setFileName(file.getOriginalFilename());
        doc.setFileType(file.getContentType());
        doc.setContent(new String(file.getBytes(), StandardCharsets.UTF_8));

        Long id = documentService.addDocument(doc);
        return ResultUtils.success(id);
    }

    @GetMapping("/list")
    public BaseResponse<List<Document>> listDocuments(@RequestParam Long appId) {
        return ResultUtils.success(documentService.listByAppId(appId));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteDocument(@RequestParam Long id) {
        return ResultUtils.success(documentService.deleteById(id));
    }
}
