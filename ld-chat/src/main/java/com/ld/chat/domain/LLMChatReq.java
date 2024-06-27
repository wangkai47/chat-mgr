package com.ld.chat.domain;

import com.ld.common.enums.ApiTypeEnum;
import kotlin.jvm.Transient;
import lombok.Data;

/**
 * @Classname LLMChatReq
 * @Description 大模型对话
 * @Date 2024/6/11
 * @Author wk
 */
@Data
public class LLMChatReq {
    
    private String conversationId;
    private String tenantId;
    private String prompt;
    @Transient
    private String originalQuestion;
    private ModelOption modelparam;
    private String modeltype;
    private ApiTypeEnum apiTypeEnum;
    
    @Data
    public static class ModelOption {
        
        private String parentMsgId;
        private Float temp;
        private String type;
    }
}
