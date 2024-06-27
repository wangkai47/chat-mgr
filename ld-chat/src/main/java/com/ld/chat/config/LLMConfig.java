package com.ld.chat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "llm")
public class LLMConfig {
    private String type;
    private String apiKye;
    private String apiUrl;
}
