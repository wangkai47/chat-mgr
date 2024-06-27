package com.ld.chat.domain;

import com.ld.common.core.domain.BaseEntity;

import java.util.List;
import java.util.Map;

public class DialogueContext {
    private Intent currentIntent; // 当前对话的意图
    private Map<String, BaseEntity> entities; // 存储识别到的实体，键为实体类型，值为实体对象
    private List<String> conversationHistory; // 对话历史，存储每一轮的用户输入和系统响应
    private Map<String, Object> sessionAttributes; // 会话属性，用于存储跨轮次的临时数据

}
