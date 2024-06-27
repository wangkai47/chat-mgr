package com.ld.chat.service;

import com.ld.chat.domain.ChatTurn;
import com.ld.chat.dto.ChatDialogReq;
import com.ld.common.core.domain.model.LoginUser;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface IChatService {
    String create(LoginUser loginUser);
    ChatTurn chat(LoginUser loginUser, String sessionId, String query);
    boolean over(String sessionId);
    SseEmitter dialog(ChatDialogReq dialogReq);
}
