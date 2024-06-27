package com.ld.chat.utils;

import cn.hutool.core.util.IdUtil;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author wk
 * 2024/6/12 09:30
 * @since chatx
 */
public class EmitterUtils {

    public static final String EMITTER_ID_PREFIX="ld-llm-";

    public static final String EMITTER_TYPE="add";

    public static final String EMITTER_FINISH_TYPE="finish";

    /**
     * 默认追加响应数据
     * @param data 数据
     * @return eventBuilder
     */
    public static SseEmitter.SseEventBuilder eventBuilder(String data){
        return eventBuilder(EMITTER_TYPE,data);
    }
    /**
     * 默认追加响应数据
     * @param data 数据
     * @return eventBuilder
     */
    public static SseEmitter.SseEventBuilder eventErrorBuilder(String data){
        return eventBuilder(EMITTER_FINISH_TYPE,data);
    }

    /**
     * 默认追加响应数据
     * @param type 事件类型
     * @param data 数据
     * @return EventBuilder
     */
    public static SseEmitter.SseEventBuilder eventBuilder(String type,String data){
        String id = EMITTER_ID_PREFIX + IdUtil.getSnowflakeNextIdStr();
        return SseEmitter.event()
                .data(data)
                .id(id)
                .name(type);
    }
}
