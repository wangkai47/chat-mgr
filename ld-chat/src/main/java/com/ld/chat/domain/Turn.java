package com.ld.chat.domain;

import com.ld.common.core.domain.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

public class Turn {
    private int turnNumber; // 轮次序号
    private String userInput; // 用户输入文本
    private Intent detectedIntent; // 检测到的用户意图
    private List<BaseEntity> entities; // 提取出的实体列表
    private String systemResponse; // 系统响应
    private LocalDateTime timestamp; // 发生时间
}
