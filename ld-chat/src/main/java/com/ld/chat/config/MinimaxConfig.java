package com.ld.chat.config;

import lombok.Data;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Data
@Component
@ConfigurationProperties(prefix = "llm.chat.minimax")
@RefreshScope
public class MinimaxConfig {

    private String url;

    private String groupId;

    private String apiKey;

    private long connectTimeout;

    private long readTimeout;

    private long writeTimeout;
}
