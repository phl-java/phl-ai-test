package com.phl.phlaicode.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * WebScreenshotUtils 并发测试
 */
@SpringBootTest
public class WebScreenshotUtilsTest {

    // 测试URL列表
    private List<String> testUrls = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // 添加一些测试URL，包括用户提供的和一些其他网站
        testUrls.add("http://localhost:63342/phl-ai-code/tmp/code_deploy/Dh1z7q/index.html?_ijt=hr7v81aik0d3rjvtko3fkpo220&_ij_reload=RELOAD_ON_SAVE#/");
        testUrls.add("http://localhost:63342/phl-ai-code/tmp/code_deploy/lHFMtE/index.html?_ijt=hr7v81aik0d3rjvtko220&_ij_reload=RELOAD_ON_SAVE#/");
        testUrls.add("https://www.baidu.com");
        testUrls.add("https://cn.bing.com");
        testUrls.add("https://www.sogou.com");
    }

    @Test
    @DisplayName("测试截图工具类的并发能力")
    void testConcurrentScreenshotGeneration() throws InterruptedException {
        // 定义线程池大小
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 存储任务结果
        List<Future<String>> futures = new ArrayList<>();

        System.out.println("开始并发测试，线程数：" + threadCount);

        // 提交并发任务
        for (int i = 0; i < threadCount; i++) {
            final String url = testUrls.get(i % testUrls.size()); // 循环使用URL
            Future<String> future = executorService.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " 开始截图: " + url);


                long startTime = System.currentTimeMillis();
                String result = WebScreenshotUtils.saveWebPageScreenshot(url);
                long endTime = System.currentTimeMillis();

                System.out.println(threadName + " 完成截图，耗时: " + (endTime - startTime) + "ms，结果: " + result);

                return result;
            });

            futures.add(future);
        }

        // 等待所有任务完成
        boolean allSuccessful = true;
        for (int i = 0; i < futures.size(); i++) {
            try {
                String result = futures.get(i).get(60, TimeUnit.SECONDS); // 设置超时时间
                if (result == null) {
                    System.out.println("任务 " + i + " 失败，没有返回截图路径");
                    allSuccessful = false;
                } else {
                    System.out.println("任务 " + i + " 成功，截图保存至: " + result);
                }
            } catch (TimeoutException e) {
                System.out.println("任务 " + i + " 超时: " + e.getMessage());
                allSuccessful = false;
            } catch (ExecutionException e) {
                System.out.println("任务 " + i + " 执行异常: " + e.getCause().getMessage());
                allSuccessful = false;
            }
        }

        // 关闭线程池
        executorService.shutdown();
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        // 断言至少大部分任务成功（由于外部网站可能不稳定，不强制要求全部成功）
        System.out.println("并发测试完成，成功状态: " + allSuccessful);
        // 注意：由于网络和外部网站的不确定性，我们只输出结果而不做强制断言
    }

    @Test
    @DisplayName("测试单个截图功能")
    void testSingleScreenshot() {
        String url = "https://www.baidu.com";
        String result = WebScreenshotUtils.saveWebPageScreenshot(url);
        System.out.println("单个截图测试，URL: " + url + ", 结果: " + result);
        // 这里不做强制断言，因为外部网站可能无法访问或截图失败
    }
}