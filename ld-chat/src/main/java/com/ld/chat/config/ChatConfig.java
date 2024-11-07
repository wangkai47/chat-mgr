package com.ld.chat.config;

import cn.hutool.core.lang.Opt;
import com.ld.common.enums.ApiTypeEnum;
import lombok.Data;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author wk
 * @date 2024-3-22
 * 聊天配置参数
 */
@Data
@Component
@ConfigurationProperties(prefix = "llm.chat")
@RefreshScope
public class ChatConfig {

    private String type;
    
    /************************************************************************************/
    /***以下配置为流控处理******************************************************************/
    /**
     * 全局时间内最大请求次数，默认无限制
     */
    private Integer maxRequest;
    
    /**
     * 全局最大请求时间间隔（秒）
     */
    private Integer maxRequestSecond;
    
    /**
     * ip 时间内最大请求次数，默认无限制
     */
    private Integer ipMaxRequest;
    
    /**
     * ip 最大请求时间间隔（秒）
     */
    private Integer ipMaxRequestSecond;
    
    /**
     * 限制上下文对话的问题数量，默认不限制
     */
    private Integer limitQuestionContextCount;
    /***********************************************************************************/
    
    /**
     * vectorApiUrl
     */
    private String vectorApiUrl;
    /**
     * chunk上下文检索范围
     */
    private Integer chunkRange;
    /**
     * 向量分数
     */
    private Float score = 0.8f;
    
    /**
     * 是否经过向量检索文档
     */
    private Boolean directChat;
    /**
     * 超时毫秒
     */
    private Long timeoutMs;
    
    /**
     * 获取全局时间内最大请求次数
     *
     * @return 最大请求次数
     */
    public Integer getMaxRequest() {
        return Opt.ofNullable(maxRequest).orElse(0);
    }
    
    /**
     * 获取全局最大请求时间间隔（秒）
     *
     * @return 时间间隔
     */
    public Integer getMaxRequestSecond() {
        return Opt.ofNullable(maxRequestSecond).orElse(0);
    }
    
    /**
     * 获取 ip 时间内最大请求次数
     *
     * @return 最大请求次数
     */
    public Integer getIpMaxRequest() {
        return Opt.ofNullable(ipMaxRequest).orElse(0);
    }
    
    /**
     * 获取 ip 最大请求时间间隔（秒）
     *
     * @return 时间间隔
     */
    public Integer getIpMaxRequestSecond() {
        return Opt.ofNullable(ipMaxRequestSecond).orElse(0);
    }
    
    /**
     * 获取限制的上下文对话数量
     *
     * @return 限制数量
     */
    public Integer getLimitQuestionContextCount() {
        return Opt.ofNullable(limitQuestionContextCount).orElse(0);
    }


    private long connectTimeout;

    private long readTimeout;

    private long writeTimeout;

    @Bean
    public OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        return builder.build();
    }
    
}
