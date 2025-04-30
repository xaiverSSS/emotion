package com.xaiver.emotion;

import com.xaiver.emotion.service.SchemaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class EmotionApplicationTests {
    @Autowired
    private SchemaService schemaService;
    @Test
    void test() {
        schemaService.generate("enterprise_record");
    }

}
