package com.phl.phlaicode.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuildResult {
    private boolean success;
    private String errorLog;
    private int exitCode;
    private String outputLog;
}
