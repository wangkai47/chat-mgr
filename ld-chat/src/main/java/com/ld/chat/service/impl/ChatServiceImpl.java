package com.ld.chat.service.impl;

import com.ld.chat.domain.ChatSession;
import com.ld.chat.domain.ChatTurn;
import com.ld.chat.dto.ChatDialogReq;
import com.ld.chat.service.IChatService;
import com.ld.chat.service.IChatTurnService;
import com.ld.chat.mapper.ChatTurnMapper;
import com.ld.chat.service.ILLMService;
import com.ld.common.constant.CacheConstants;
import com.ld.common.core.cache.DataCache;
import com.ld.common.core.domain.model.LoginUser;
import com.ld.common.utils.SecurityUtils;
import com.ld.common.utils.StringUtils;
import com.ld.common.utils.uuid.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author wangkai
* @description 对话管理
* @createDate 2024-06-05 14:31:02
*/
@Slf4j
@Service
public class ChatServiceImpl implements IChatService {

    @Autowired
    private ILLMService llmService;

    @Autowired
    private ChatTurnMapper chatTurnMapper;

    @Autowired
    private DataCache dataCache;

    private String getCacheKey(String sessionId) {
        return CacheConstants.CHAT_SESSION_KEY + sessionId;
    }

    public String create(LoginUser loginUser) {
        String sessionId = IdUtils.fastSimpleUUID();
        ChatSession chatSession = new ChatSession();
        chatSession.setSessionId(sessionId);
        chatSession.setUserId(loginUser.getUserId());
        dataCache.setCacheObject(getCacheKey(sessionId), chatSession);
        return sessionId;
    }

    public ChatTurn chat(ChatTurn chatTurn) {
        if (StringUtils.isEmpty(chatTurn.getSessionId())) {
            chatTurn.setSessionId(IdUtils.fastSimpleUUID());
        }
        String cacheKey = getCacheKey(chatTurn.getSessionId());
        ChatSession chatSession;
        if (dataCache.hasKey(cacheKey)) {
            chatSession = dataCache.getCacheObject(cacheKey);
        } else {
            chatSession = new ChatSession();
            chatSession.setSessionId(chatTurn.getSessionId());
            chatSession.setTurns(new ArrayList<>());
        }

        String prompt = buildPrompt(chatTurn.getQuery(), chatSession.getTurns());
        String answer = llmService.call(prompt);

        chatTurn.setAnswer(answer);
        chatTurn.setOrderNo(chatSession.getTurns().size() + 1);
        chatTurn.setCreateTime(new Date());
        chatTurn.setUpdateTime(new Date());
        chatTurnMapper.insertChatTurn(chatTurn);

        chatSession.getTurns().add(chatTurn);
        dataCache.setCacheObject(cacheKey, chatSession);
        return chatTurn;
    }

    @Override
    public ChatTurn chat(LoginUser loginUser, String sessionId, String query) {
        ChatTurn chatTurn = new ChatTurn();
        chatTurn.setUserId(loginUser.getUserId());
        chatTurn.setSessionId(sessionId);
        chatTurn.setQuery(query);
        chatTurn.setCreateBy(loginUser.getUsername());
        chatTurn.setUpdateBy(loginUser.getUsername());
        return chat(chatTurn);
    }

    public String buildPrompt(String query, List<ChatTurn> turns) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请回答以下问题：");
        prompt.append(query);
        if (turns != null && turns.size() > 0) {
            prompt.append("。基于以下对话内容：");
            turns.forEach(turn -> {
                prompt.append("\n");
                prompt.append("“问：");
                prompt.append(turn.getQuery());
                prompt.append("”，“答：");
                prompt.append(turn.getAnswer());
                prompt.append("”");
            });
        }

        return prompt.toString();
   }

   public boolean over(String sessionId) {
        return dataCache.deleteObject(getCacheKey(sessionId));
   }

   public SseEmitter dialog(ChatDialogReq dialogReq) {
       String query = dialogReq.getQuestion();
       LoginUser loginUser = SecurityUtils.getLoginUser();
       SseEmitter emitter = new SseEmitter(180000L);
       emitter.onCompletion(() -> log.debug("请求参数：{}，Front-end closed the emitter connection.", query));
       emitter.onTimeout(() -> log.error("请求参数：{}，Back-end closed the emitter connection.", query));
       String prompt = this.buildPrompt(query, null);
       log.info("prompt = {}", prompt);
       llmService.streamCall(prompt, emitter);
       return emitter;
   }
}




