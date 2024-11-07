package com.ld.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class LLMChatResponse {
    private String conversationId;
    private boolean isFinished;
    private boolean isIncrement;
    private String text;
}
