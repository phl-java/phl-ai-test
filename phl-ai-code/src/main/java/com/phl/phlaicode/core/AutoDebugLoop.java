package com.phl.phlaicode.core;

import com.phl.phlaicode.constant.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AutoDebugLoop {

    private static final int MAX_RETRIES = 3;
    private static final long BUILD_TIMEOUT_MS = 120_000;
    private static final long INSTALL_TIMEOUT_MS = 60_000;

    public DebugResult run(String projectPath, Runnable fixCodeAction) {
        String buildLog = "";
        int retryCount = 0;

        while (retryCount <= MAX_RETRIES) {
            BuildResult buildResult = buildProject(projectPath);
            buildLog = buildResult.getOutputLog();

            if (buildResult.isSuccess()) {
                return new DebugResult(true, retryCount, buildLog, null, projectPath);
            }

            if (retryCount >= MAX_RETRIES) {
                return new DebugResult(false, retryCount, buildLog, buildResult.getErrorLog(), projectPath);
            }

            retryCount++;
            log.info("构建失败，自动修复第 {}/{} 次", retryCount, MAX_RETRIES);

            if (fixCodeAction != null) {
                fixCodeAction.run();
            }
        }

        return new DebugResult(false, retryCount, buildLog, "超出最大重试次数", projectPath);
    }

    public BuildResult buildProject(String projectPath) {
        try {
            boolean isVueProject = new File(projectPath, "package.json").exists();
            if (isVueProject) {
                executeCommand("npm install", projectPath, INSTALL_TIMEOUT_MS);
                String output = executeCommand("npm run build", projectPath, BUILD_TIMEOUT_MS);
                return new BuildResult(true, null, 0, output);
            }
            return new BuildResult(true, null, 0, "无需构建");
        } catch (Exception e) {
            return new BuildResult(false, e.getMessage(), -1, "");
        }
    }

    private String executeCommand(String command, String workingDir, long timeoutMs) throws Exception {
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File(workingDir));
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            pb.command("cmd.exe", "/c", command);
        } else {
            pb.command("sh", "-c", command);
        }
        pb.redirectErrorStream(true);
        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        boolean finished = process.waitFor(timeoutMs, TimeUnit.MILLISECONDS);
        if (!finished) {
            process.destroyForcibly();
            throw new RuntimeException("命令执行超时: " + command);
        }

        int exitCode = process.exitValue();
        if (exitCode != 0) {
            throw new RuntimeException("命令失败，退出码: " + exitCode + ":\n" + output);
        }

        return output.toString();
    }
}
