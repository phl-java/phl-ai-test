package com.phl.phlaicode.core;

import com.phl.phlaicode.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;
    @Test
    void generateVueProjectCodeStream() {
        Flux<String> codeStream =
                aiCodeGeneratorFacade.generateAndSaveCodeStream("做一个博客网站，总代码量不超过 200行",
                CodeGenTypeEnum.VUE_PROJECT, 2L, "qwen");
        List<String> result = codeStream.collectList().block();
//验证结果
        Assertions.assertNotNull(result);
        String completeContent = String.join(  "", result);
        Assertions.assertNotNull(completeContent);
    }


    @Test
    void  aa(){
        List<String> str = List.of("晚上好！", "今天晚上吃什么？", "吃火锅吧！","我想吃烧烤","好");

        List<String> reversed = str.reversed();
        System.err.println(reversed);
    }

























}