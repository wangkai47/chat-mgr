package com.ld.chat.service.impl;

import cn.hutool.http.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ld.chat.listener.MinimaxEventSourceListener;
import com.ld.chat.service.ILLMService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Slf4j
@Service
@ConditionalOnProperty(name = "llm.type", havingValue = "minimax")
public class MinimaxServiceImpl implements ILLMService {

    private String promptTemplate = "姓名:张伟\n" +
            "年龄:55岁\n" +
            "性别:男\n" +
            "职业:退休工人\n" +
            "居住地区:乡镇\n" +
            "健康状况:\n" +
            "高血压\n" +
            "糖尿病\n" +
            "腰酸\n" +
            "腰痛\n" +
            "精神不振\n" +
            "脱发、掉发\n" +
            "脂肪肝\n" +
            "生活习惯:\n" +
            "抽烟\n" +
            "喝酒\n" +
            "久坐加班\n" +
            "购买动机:由于长期工作压力和不良生活习惯，张伟开始关注自己的健康状况，希望通过购买保健类产品来改善身体状况。\n" +
            "购买能力:由于退休，张伟有一定的经济基础，但对价格较为敏感。\n" +
            "咨询目的:保健类产品的疗效和副作用，产品的正规性和安全性，产品的使用时长和效果，价格和性价比，产品的真伪和标识";

    @Value("${llm.chat.minimax.url}")
    private String url;

    @Value("${llm.chat.minimax.groupId}")
    private String groupId;

    @Value("${llm.chat.minimax.apiKey}")
    private String apiKey;

    @Autowired
    private OkHttpClient okHttpClient;

    @Override
    public String call(String prompt) {
        return null;
    }

    @Override
    public void streamCall(String chatCompletion, SseEmitter sseEmitter) {
        okhttp3.Headers.Builder headers = new okhttp3.Headers.Builder();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", "abab5.5-chat");
        bodyMap.put("stream", true);
        //bodyMap.put("tokens_to_generate", 1024);
        //bodyMap.put("reply_constraints", Map.of("sender_type", "BOT", "sender_name", "虚拟客户"));
        bodyMap.put("messages", new Object[] {
                Map.of(
                        "role", "user",
                        "content", chatCompletion
                )
        });
        bodyMap.put("bot_setting", new Object[]{
                Map.of(
                        "bot_name", "智能助理",
                        "content", "灵动智能助理是一款由灵动数上研发的大型语言模型。灵动数上是一家中国科技公司，一直致力于进行大模型应用落地，为企业AI+赋能。"
                )
        });
        try {
            EventSource.Factory factory = EventSources.createFactory(this.okHttpClient);
            String requestBody = (new ObjectMapper()).writeValueAsString(bodyMap);
            Request request = new Request.Builder()
                    .url(url)
                    .headers(headers.build())
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody))
                    .build();
            //log.debug("url={}, key={}", url, apiKey);
            if (log.isDebugEnabled()) {
                log.debug("rbody={}", requestBody);
            }

            // 创建事件
            EventSource eventSource = factory.newEventSource(request, new MinimaxEventSourceListener(sseEmitter));
        } catch (JsonProcessingException e) {
            log.error("请求参数解析异常：{}", chatCompletion, e);
        } catch (Exception e) {
            log.error("请求参数解析异常：{}", chatCompletion, e);
        }
    }
}
