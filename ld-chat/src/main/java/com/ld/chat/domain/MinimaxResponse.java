package com.ld.chat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "Minimax回复的消息")
public class MinimaxResponse {
    private String id;

    private List<Choice> choices;

    private long created;

    private String model;

    private String object;

    private Usage usage;

    private boolean inputSensitive;

    private boolean outputSensitive;

    private int inputSensitiveType;

    private int outputSensitiveType;

    @JsonProperty("base_resp")
    private BaseResp baseResp;

    @Data
    public static class Choice {
        private String finishReason;

        private int index;

        private Delta delta;

        private Message message;
    }

    @Data
    public static class Delta {
        private String content;

        private String role;
    }

    @Data
    public static class Message {
        private String content;

        private String role;
    }

    @Data
    public static class Usage {
        private int totalTokens;
    }

    @Data
    public static class BaseResp {
        private int statusCode;

        private String statusMsg;
    }
}