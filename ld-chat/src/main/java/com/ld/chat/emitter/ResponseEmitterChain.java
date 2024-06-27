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
 * @author jerry
 * @date 2023-07-10
 * ResponseBodyEmitter 链路
 * 责任链模式实现
 */
public interface ResponseEmitterChain {
    
    /**
     * 处理请求
     *
     * @param request 请求对象
     * @param emitter 响应对象
     */
    void doChain(LLMChatReq request, SseEmitter emitter);
    
    /**
     * 设置下一个处理器
     *
     * @param next 下一个处理器
     */
    void setNext(ResponseEmitterChain next);
    
    /**
     * 获取下一个处理器
     *
     * @return 下一个处理器
     */
    ResponseEmitterChain getNext();
    
    /**
     * 获取前一个处理器
     *
     * @return 前一个处理器
     */
    ResponseEmitterChain getPrev();
    
    /**
     * 设置前一个处理器
     *
     * @param prev 前一个处理器
     */
    void setPrev(ResponseEmitterChain prev);
}
