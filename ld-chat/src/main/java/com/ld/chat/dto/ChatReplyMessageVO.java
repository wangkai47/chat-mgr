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


package com.ld.chat.dto;

import com.ld.chat.domain.LLMChatReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

/**
 * @author jerry
 * @date 2023-3-23
 * 聊天回复的消息
 */
@Data
@ApiModel(value = "聊天回复的消息")
public class ChatReplyMessageVO {
    
    @ApiModelProperty(value = "当前消息 id")
    private String id;
    
    @ApiModelProperty(value = "父级消息 id")
    private String parentMessageId;
    
    @ApiModelProperty(value = "对话 id")
    private String conversationId;
    
    @ApiModelProperty(value = "回复的消息")
    private String text;
    
    /**
     * 当链路出现问题时 取上一条消息的 parentMessageId 和 conversationId，使得异常不影响上下文
     *
     * @param request 消息处理请求的实体 从中获取 parentMessageId 和 conversationId
     * @return 聊天回复的消息
     */
    public static ChatReplyMessageVO onEmitterChainException(LLMChatReq request) {
        ChatReplyMessageVO chatReplyMessageVO = new ChatReplyMessageVO();
        chatReplyMessageVO.setId(Optional.ofNullable(request.getModelparam()).orElse(new LLMChatReq.ModelOption()).getParentMsgId());
        chatReplyMessageVO.setConversationId(request.getConversationId());
        chatReplyMessageVO.setParentMessageId(null);
        return chatReplyMessageVO;
    }
}
