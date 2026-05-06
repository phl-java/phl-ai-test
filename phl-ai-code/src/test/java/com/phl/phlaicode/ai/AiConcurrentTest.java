package com.phl.phlaicode.ai;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class AiConcurrentTest {

    @Resource
    private AiCodeGenTypeRoutingServiceFactory routingServiceFactory;

    @Test
    public void testConcurrent() throws InterruptedException {
        String[] prompts = {
                "做一个简单的html网页",
                "做一个多页面网站",
                "做一个vue管理系统"
        };
        //使用虚拟线程并发执行
        Thread[] threads = new Thread[prompts.length];
        for (int i = 0; i < prompts.length; i++) {
            final String prompt = prompts[i];
            final int index = i + 1;
            threads[i] = Thread.ofVirtual().start(()->{
                AiCodeGenTypeRoutingService se = routingServiceFactory.createAiCodeGenTypeRoutingService();
                var result = se.routeCodeGenType(prompt);
                log.info("线程Thread {}：{}-》", index,prompt, result.getValue());
            });
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }













}
