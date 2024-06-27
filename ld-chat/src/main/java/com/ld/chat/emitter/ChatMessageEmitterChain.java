package com.ld.chat.emitter;

import cn.hutool.extra.spring.SpringUtil;
import com.ld.chat.config.ChatConfig;
import com.ld.chat.domain.LLMChatReq;
import com.ld.common.enums.ApiTypeEnum;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author wk
 * @date 2023-06-10
 * 正常发送消息链路，最后一个节点
 */
public class ChatMessageEmitterChain extends AbstractResponseEmitterChain {
    
    @Override
    public void doChain(LLMChatReq request, SseEmitter emitter) {
        ApiTypeEnum apiTypeEnum = SpringUtil.getBean(ChatConfig.class).getApiTypeEnum();
        ResponseEmitter responseEmitter = null;
        if (apiTypeEnum == ApiTypeEnum.LS_KEY) {
            //responseEmitter = SpringUtil.getBean(LsApiResponseEmitter.class);
        } else if (apiTypeEnum == ApiTypeEnum.OPEN_AI_KEY) {
            //responseEmitter = SpringUtil.getBean(OpenAiKeyResponseEmitter.class);
        } else {
            //responseEmitter = SpringUtil.getBean(ZpAiKeyResponseEmitter.class);
        }
        responseEmitter.requestToResponseEmitter(request, emitter);
    }
}
