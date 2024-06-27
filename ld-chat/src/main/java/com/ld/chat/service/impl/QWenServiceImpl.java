package com.ld.chat.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.ld.chat.service.ILLMService;
import com.ld.common.constant.Constants;
import com.ld.common.utils.MessageUtils;
import com.ld.framework.manager.AsyncManager;
import com.ld.framework.manager.factory.AsyncFactory;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@ConditionalOnProperty(name = "llm.type", havingValue = "qwen")
public class QWenServiceImpl implements ILLMService {

    @Value("${llm.qwen.apiKey}")
    private String apiKey;

    public String call(String prompt) {
        Generation generation = new Generation();
        GenerationParam param = GenerationParam
                .builder()
                .apiKey(apiKey)
                .model(Generation.Models.QWEN_TURBO)
                .prompt(prompt)
                .build();
        GenerationResult result = null;
        try {
            result = generation.call(param);
        } catch (NoApiKeyException e) {
            //AsyncManager.me().execute(AsyncFactory.recordOper(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            log.error("模型调用失败，apiKey：" + apiKey, e);
        } catch (InputRequiredException e) {
            log.error("模型调用失败，prompt：" + prompt, e);
        }

        log.debug(JsonUtils.toJson(result));
        return result.getOutput().getText();
    }

    public void streamCall(String prompt, SseEmitter sseEmitter) {
        Generation generation = new Generation();
        GenerationParam param = GenerationParam
                .builder()
                .apiKey(apiKey)
                .model(Generation.Models.QWEN_TURBO)
                .prompt(prompt)
                .build();
        Flowable<GenerationResult> result = null;
        long startTime = System.currentTimeMillis();
        try {
            generation.streamCall(param, new ResultCallback<GenerationResult>() {
                @Override
                public void onEvent(GenerationResult message) {
                    System.out.println(message.getOutput().getText());
                    try {
                        sseEmitter.send(message.getOutput().getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception err) {
                    log.error("Exception occurred: {}", err.getMessage());
                }

                @Override
                public void onComplete() {
                    log.debug("qwen complete in {}ms", System.currentTimeMillis() - startTime);
                    sseEmitter.complete();
                }
            });
        } catch (NoApiKeyException e) {
            log.error("模型调用失败，NoApiKey：" + apiKey, e);
            sseEmitter.completeWithError(e);
        } catch (InputRequiredException e) {
            log.error("模型调用失败，prompt：" + prompt, e);
            sseEmitter.completeWithError(e);
        }
    }
}