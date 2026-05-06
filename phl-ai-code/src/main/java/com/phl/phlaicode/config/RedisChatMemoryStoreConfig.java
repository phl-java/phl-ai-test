package com.phl.phlaicode.config;

import cn.hutool.core.util.StrUtil;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis 持久化对话记忆
 */
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedisChatMemoryStoreConfig {

    private String host;

    private int port;

    private String password;

    private long ttl;//键的存活时间
    

    @Bean
    public RedisChatMemoryStore redisChatMemoryStore() {

        RedisChatMemoryStore.Builder builder = RedisChatMemoryStore.builder()
                .host(host)
                .port(port)
                .ttl(ttl);
        
        // 处理密码和用户配置
        if (StrUtil.isNotBlank(password)) {
            // 有密码的情况：设置用户和密码
            builder.user("default").password(password);
        }
        // 无密码的情况：不设置 user 和 password，避免传递空字符串导致连接问题
        
        return builder.build();
    }

}
