package com.phl.phlaicode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching//启用缓存
@SpringBootApplication
@MapperScan("com.phl.phlaicode.mapper")
public class PhlAiCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhlAiCodeApplication.class, args);
        System.err.println("启动成功");
    }

}
