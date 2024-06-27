package com.ld.chat.emitter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.ld.chat.domain.LLMChatReq;
import com.ld.chat.dto.ChatReplyMessageVO;
import com.ld.chat.handler.SensitiveWordHandler;
import com.ld.chat.utils.EmitterUtils;
import com.ld.chat.utils.ObjectMapperUtil;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

/**
 * @author wk
 * @date 2024-06-10
 * 敏感词检测
 */
public class SensitiveWordEmitterChain extends AbstractResponseEmitterChain {
    
    @Override
    public void doChain(LLMChatReq request, SseEmitter emitter) {
        List<String> prompts = SensitiveWordHandler.checkWord(request.getPrompt(), request.getTenantId());
        try {
            // 取上一条消息的 parentMessageId 和 conversationId，使得敏感词检测未通过时不影响上下文
            ChatReplyMessageVO chatReplyMessageVO = ChatReplyMessageVO.onEmitterChainException(request);
            if (CollectionUtil.isNotEmpty(prompts)) {
                chatReplyMessageVO.setText(StrUtil.format("发送失败，包含敏感词{}", prompts));
                //emitter.send(ObjectMapperUtil.toJson(chatReplyMessageVO), MediaType.TEXT_EVENT_STREAM);
                emitter.send(EmitterUtils.eventErrorBuilder(ObjectMapperUtil.toJson(chatReplyMessageVO)));
                emitter.complete();
                return;
            }
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (getNext() != null) {
            getNext().doChain(request, emitter);
        }
    }
}
