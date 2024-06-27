package com.ld.chat.domain;

import java.util.Map;

public class Intent {
    private String name; // 意图的名称
    private double confidenceScore; // 系统对匹配该意图的置信度
    private Map<String, Object> slots; // 关联的槽位（slot）信息，用于存储实体值
}
