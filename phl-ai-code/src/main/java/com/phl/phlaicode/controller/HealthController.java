package com.phl.phlaicode.controller;

import com.phl.phlaicode.common.BaseResponse;
import com.phl.phlaicode.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health/")
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success("ok");
    }
}
