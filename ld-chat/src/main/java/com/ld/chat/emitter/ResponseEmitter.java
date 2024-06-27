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


package com.ld.chat.emitter;

import com.ld.chat.domain.LLMChatReq;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author wk
 * @date 2024-06-10
 * 响应内容
 */
public interface ResponseEmitter {
    
    /**
     * 消息请求转 Emitter
     *
     * @param chatDialogReq 消息处理请求
     * @param emitter       ResponseBodyEmitter
     */
    void requestToResponseEmitter(LLMChatReq chatDialogReq, SseEmitter emitter);
}
