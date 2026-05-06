package com.phl.phlaicode.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DebugResult {
    private boolean success;
    private int retryCount;
    private String buildLog;
    private String errorLog;
    private String projectPath;
}
