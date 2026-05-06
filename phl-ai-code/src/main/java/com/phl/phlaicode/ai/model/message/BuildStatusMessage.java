package com.phl.phlaicode.ai.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuildStatusMessage {
    private String type = "build_status";
    private String stage;
    private String message;
    private Integer retryCount;
    private Integer maxRetries;
}
