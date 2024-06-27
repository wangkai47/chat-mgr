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


package com.ld.chat.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.SensitiveProcessor;
import cn.hutool.dfa.WordTree;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ld.chat.domain.SensitiveWord;
import com.ld.chat.mapper.SensitiveWordMapper;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Classname SensitiveWordHandler
 * @Description 敏感词处理器
 * @Date 2024/6/12 23:37
 * @Author wk
 */
@Slf4j
public class SensitiveWordHandler {
    
    private static final String CACHE_KEY = "wordTree";
    private static ConcurrentHashMap<String, WordTree> wordTreeConcurrentHashMap = new ConcurrentHashMap<>();

    /**
     * 敏感词缓存
     */
    private static final LoadingCache<String, WordTree> CACHE = CacheBuilder.newBuilder()
            // 设置并发级别为 CPU 核心数
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            // 过期时间为 12 小时
            .expireAfterWrite(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, WordTree>() {
                
                @Override
                public @NotNull WordTree load(@NotNull String str) {
                    log.warn("开始构建敏感词树");
                    WordTree wordTree = new WordTree();
                    SensitiveWordMapper sensitiveWordMapper = SpringUtil.getBean(SensitiveWordMapper.class);
                    List<SensitiveWord> wordList = sensitiveWordMapper.selectList(Wrappers.lambdaQuery(SensitiveWord.class).eq(SensitiveWord::getTenantId, str));
                    wordTree.addWords(wordList.stream().map(SensitiveWord::getName).collect(Collectors.toList()));
                    return wordTree;
                }
            });
    
    private static void initWordTreeGroup(Iterable<String> tenantIdList) {
        SensitiveWordMapper sensitiveWordMapper = SpringUtil.getBean(SensitiveWordMapper.class);
        int pageNo = 1, size = 1000;
        while (true) {
            Page<SensitiveWord> page = new Page(pageNo, size);
            Page<SensitiveWord> resultPage = sensitiveWordMapper
                    .selectPage(page, Wrappers.lambdaQuery(SensitiveWord.class)
                            .in(SensitiveWord::getTenantId, tenantIdList));
            pageNo++;
            if (CollUtil.isEmpty(resultPage.getRecords())) {
                break;
            }
            Map<String, List<SensitiveWord>> group = resultPage.getRecords().stream().collect(Collectors.groupingBy(SensitiveWord::getTenantId));
            group.forEach((k, v) -> {
                WordTree wordTree = wordTreeConcurrentHashMap.getOrDefault(k, new WordTree());
                wordTree.addWords(v.stream().map(SensitiveWord::getName).collect(Collectors.toList()));
            });
        }
    }
    
    /**
     * 检查敏感词
     *
     * @return 敏感词列表
     */
    public static List<String> checkWord(String content) {
        WordTree wordTree = null;
        try {
            wordTree = CACHE.get(CACHE_KEY);
        } catch (Exception e) {
            log.error("获取敏感词树失败", e);
        }
        if (Objects.isNull(wordTree)) {
            return Collections.emptyList();
        }
        return wordTree.matchAll(content, -1, true, true);
    }
    
    /**
     * 检查敏感词
     *
     * @return 敏感词列表
     */
    public static List<String> checkWord(String content, String tenantId) {
        WordTree wordTree = null;
        try {
            wordTree = CACHE.get(tenantId);
        } catch (Exception e) {
            log.error("获取敏感词树失败", e);
        }
        if (Objects.isNull(wordTree)) {
            return Collections.emptyList();
        }
        return wordTree.matchAll(content, -1, true, true);
    }
    
    public static List<FoundWord> getFoundAllSensitive(String tenantId, String content, boolean isGreedMatch) {
        WordTree wordTree = null;
        try {
            wordTree = CACHE.get(tenantId);
        } catch (Exception e) {
            log.error("获取敏感词树失败", e);
        }
        if (Objects.isNull(wordTree)) {
            return Collections.emptyList();
        }
        return wordTree.matchAllWords(content, -1, true, isGreedMatch);
    }
    
    public static String sensitiveFilter(String tenantId, String text, boolean isGreedMatch, SensitiveProcessor sensitiveProcessor) {
        if (StrUtil.isEmpty(text)) {
            return text;
        }
        // 敏感词过滤场景下，不需要密集匹配
        final List<FoundWord> foundWordList = getFoundAllSensitive(tenantId, text, isGreedMatch);
        if (CollUtil.isEmpty(foundWordList)) {
            return text;
        }
        
        sensitiveProcessor = sensitiveProcessor == null ? new DefaultSensitiveProcessor() : sensitiveProcessor;
        
        final Map<Integer, FoundWord> foundWordMap = new HashMap<>(foundWordList.size(), 1);
        foundWordList.forEach(foundWord -> foundWordMap.put(foundWord.getStartIndex(), foundWord));
        final int length = text.length();
        final StringBuilder textStringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            final FoundWord fw = foundWordMap.get(i);
            if (fw != null) {
                textStringBuilder.append(sensitiveProcessor.process(fw));
                i = fw.getEndIndex();
            } else {
                textStringBuilder.append(text.charAt(i));
            }
        }
        log.info("触发敏感词替换，原文：{},替换后：{}", text, textStringBuilder);
        return textStringBuilder.toString();
    }
    
    private static class DefaultSensitiveProcessor implements SensitiveProcessor {
        
        @Override
        public String process(FoundWord foundWord) {
            return "";
        }
    }
}
