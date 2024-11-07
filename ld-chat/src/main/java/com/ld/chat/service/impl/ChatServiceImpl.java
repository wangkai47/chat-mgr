package com.ld.chat.service.impl;

import com.ld.chat.domain.DialogueContext;
import com.ld.chat.dto.ChatDialogReq;
import com.ld.chat.service.IChatService;
import com.ld.chat.service.ILLMService;
import com.ld.common.constant.CacheConstants;
import com.ld.common.core.cache.DataCache;
import com.ld.common.utils.StringUtils;
import com.ld.common.utils.uuid.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
    private DataCache dataCache;

    private String getCacheKey(String sessionId) {
        return CacheConstants.CHAT_CONVERSATION_KEY + sessionId;
    }

    public String generateConversationId() {
        return IdUtils.fastSimpleUUID();
    }

    public String buildPrompt(String query, DialogueContext dialogueContext) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请回答以下问题：");
        prompt.append(query);
        List<DialogueContext.QAMessage> qaMessages = dialogueContext.getConversationHistory();
        if (qaMessages != null && qaMessages.size() > 0) {
            prompt.append("。基于以下对话内容：");
            qaMessages.forEach(item -> {
                prompt.append("\n");
                prompt.append(item.getRole());
                prompt.append(":").append(item.getMessage());
            });
        }

        return prompt.toString();
   }

   public boolean over(String sessionId) {
        return dataCache.deleteObject(getCacheKey(sessionId));
   }

   public SseEmitter dialog(ChatDialogReq dialogReq) {
       if (StringUtils.isEmpty(dialogReq.getConversationId())) {
           dialogReq.setConversationId(generateConversationId());
       }

       String cacheKey = getCacheKey(dialogReq.getConversationId());
       DialogueContext dialogueContext;
       if (dataCache.hasKey(cacheKey)) {
           dialogueContext = dataCache.getCacheObject(cacheKey);
       } else {
           dialogueContext = new DialogueContext();
       }

       String query = dialogReq.getQuestion();
       SseEmitter emitter = new SseEmitter(180000L);
       emitter.onCompletion(() -> log.debug("请求参数：{}，Front-end closed the emitter connection.", query));
       emitter.onTimeout(() -> log.error("请求参数：{}，Back-end closed the emitter connection.", query));
       String prompt = this.buildPrompt(query, dialogueContext);
       log.debug("prompt = {}", prompt);
       llmService.streamCall(prompt, emitter);
       return emitter;
   }
}




