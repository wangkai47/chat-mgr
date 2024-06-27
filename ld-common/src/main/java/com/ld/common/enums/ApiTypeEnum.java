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


package com.ld.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hncboy
 * @date 2023-3-22
 * API 类型枚举
 */
@AllArgsConstructor
public enum ApiTypeEnum {
    
    /**
     * API_KEY
     */
    OPEN_AI_KEY("ApiKey", "ChatGPTAPI"),
    
    /**
     * ACCESS_TOKEN
     */
    LS_KEY("LsKey", "lsChatAPI"),
    /**
     * 智谱APIKEY
     */
    ZP_KEY("ZpKey", "zpChatAPI");
    
    private static final Set<ApiTypeEnum> LIMIT_SET = Stream.of(OPEN_AI_KEY, ZP_KEY).collect(Collectors.toSet());
    
    @Getter
    private final String name;
    
    @Getter
    @JsonValue
    private final String message;
    
    public static boolean limitApiCount(ApiTypeEnum apiTypeEnum) {
        return LIMIT_SET.contains(apiTypeEnum);
    }
    
}
