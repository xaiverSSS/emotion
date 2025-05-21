package com.xaiver.emotion.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupListener implements CommandLineRunner {
    @Override
    public void run(String... args) {
        log.info("服务启动");
    }
}
