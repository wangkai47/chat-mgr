/*
 * THIS FILE IS PART OF Zhejiang LiShi Technology CO.,LTD.
 * Copyright (c) 2019-2023  Zhejiang LiShi Technology CO.,LTD.
 * It is forbidden to distribute or copy the code under this software without the consent of the Zhejiang LiShi Technology
 *
 *     https://www.lishiots.com/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.ld.chat.listener;

import cn.hutool.core.util.StrUtil;
import com.ld.chat.domain.LLMChatResponse;
import com.ld.chat.domain.MinimaxResponse;
import com.ld.chat.utils.ObjectMapperUtil;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author wk
 * @date 2024-07-10
 * 解析 MinimaxEventSourceListener
 */
@Slf4j
public class MinimaxEventSourceListener extends EventSourceListener {
    
    /**
     * 已经接收到的完整消息
     */
    private String receivedMessage = StrUtil.EMPTY;

    private final SseEmitter emitter;

    public MinimaxEventSourceListener(SseEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String originalData) {
        log.info("id: {}, type: {}, receive: {}", id, type, originalData);
        this.receivedMessage = originalData;
        if (StrUtil.isEmpty(originalData)) {
            return;
        }
        MinimaxResponse minimaxtResponse = ObjectMapperUtil.fromJson(originalData, MinimaxResponse.class);
        //log.debug("minimaxtResponse = {}", minimaxtResponse);
        if (Objects.nonNull(minimaxtResponse.getBaseResp())) {//最后的全量回复
            return;
        }
        if (Collections.isEmpty(minimaxtResponse.getChoices())) {//无回复内容
            return;
        }
        List<MinimaxResponse.Choice> choices = minimaxtResponse.getChoices();
        Boolean isEnd = Objects.isNull(choices.get(choices.size() - 1).getFinishReason()) ? false : true;
        StringBuffer message = new StringBuffer();
        choices.forEach(choice -> {
            if (Objects.nonNull(choice.getDelta())) {
                message.append(choice.getDelta().getContent());
            }
        });
        if (message.length() == 0) {
            return;
        }
        // 判断有没有结束
        //boolean isEnd = Objects.equals(originalData, "[DONE]");
        //isEnd = StrUtil.containsAny(type, "finish", "error", "interrupted");
        LLMChatResponse llmChatResponse = new LLMChatResponse(id, isEnd, true, message.toString());
        try {
            emitter.send(ObjectMapperUtil.toJson(llmChatResponse));
            log.debug("send msg: {}", llmChatResponse);
        } catch (IOException e) {
            log.error("消息发送异常，当前已接收消息：{}，异常堆栈：", minimaxtResponse, e);
        }
    }

    @Override
    public void onClosed(EventSource eventSource) {
        emitter.complete();
    }

    @Override
    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        String responseStr = null;
        try {
            if (Objects.nonNull(response) && Objects.nonNull(response.body())) {
                responseStr = response.body().string();
            }
            log.warn("消息发送异常，当前已接收消息：{}，响应内容：{}，异常堆栈：", receivedMessage, responseStr, t);
            response.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                emitter.complete();
            } catch (Exception ignored) {

            }
        }
    }
}
