package com.xaiver.emotion.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class EmotionConfiguration {

    @Value("${force_convert.entity.java_type.timestamp2date}")
    private boolean forceEntityJavaTypeTimestamp2date;
}
