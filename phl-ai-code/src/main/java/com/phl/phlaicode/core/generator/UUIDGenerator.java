package com.phl.phlaicode.core.generator;

import com.mybatisflex.core.keygen.IKeyGenerator;

import java.util.UUID;

/**
 * UUID主键生成器
 * 生成标准的UUID字符串作为主键
 */
public class UUIDGenerator implements IKeyGenerator {
    
    @Override
    public Object generate(Object entity, String keyColumn) {
        return UUID.randomUUID().toString().replace("-", "");
    }
}