package com.phl.phlaicode.controller;

import com.phl.phlaicode.common.BaseResponse;
import com.phl.phlaicode.common.ResultUtils;
import com.phl.phlaicode.exception.BusinessException;
import com.phl.phlaicode.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/app/visual")
public class VisualEditController {

    @PostMapping("/edit")
    public BaseResponse<String> visualEdit(@RequestBody VisualEditRequest request) {
        if (request.getAppId() == null || request.getSelector() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        log.info("可视化编辑请求: appId={}, selector={}, changes={}",
            request.getAppId(), request.getSelector(), request.getChanges());
        // TODO: Use AI to locate source file and modify code
        return ResultUtils.success("修改请求已提交，AI 正在处理中");
    }

    public static class VisualEditRequest {
        private Long appId;
        private String selector;
        private List<StyleChange> changes;

        public Long getAppId() { return appId; }
        public void setAppId(Long appId) { this.appId = appId; }
        public String getSelector() { return selector; }
        public void setSelector(String selector) { this.selector = selector; }
        public List<StyleChange> getChanges() { return changes; }
        public void setChanges(List<StyleChange> changes) { this.changes = changes; }

        public static class StyleChange {
            private String property;
            private String value;

            public String getProperty() { return property; }
            public void setProperty(String property) { this.property = property; }
            public String getValue() { return value; }
            public void setValue(String value) { this.value = value; }
        }
    }
}
