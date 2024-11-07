package com.ld.chat.service;

import com.ld.chat.dto.ChatDialogReq;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface IChatService {
    String generateConversationId();
    boolean over(String sessionId);
    SseEmitter dialog(ChatDialogReq dialogReq);
}
