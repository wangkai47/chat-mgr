package com.ld.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Classname OpenAIConfig
 * @Description TODO
 * @Date 2023/7/13 16:18
 * @Author jerrylin
 */

@Data
@Component
@ConfigurationProperties(prefix = "llm.chat.zpai")
@RefreshScope
public class ZpAIConfig {
    
    private String zpApiKey;
    
    /**
     * 大模型对话地址
     */
    private String zpApiUrl;
    
    private Float temperature = 0.8f;
    
    private Float topP = 0.9f;
    
    private String modelType = "chatglm_std";
}
