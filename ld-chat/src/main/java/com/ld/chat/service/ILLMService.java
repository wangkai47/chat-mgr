package com.ld.chat.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ILLMService {
    String call(String prompt);
    void streamCall(String prompt, SseEmitter sseEmitter);
}
