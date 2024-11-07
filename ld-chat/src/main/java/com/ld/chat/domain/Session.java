package com.ld.chat.domain;

import java.util.List;

public class Session {
    private String sessionId; // 会话唯一标识
    private String userId; // 当前会话的用户信息
    private DialogueContext context; // 当前会话的上下文信息
    private boolean isActive; // 会话是否活跃
}
