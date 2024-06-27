package com.ld.common.core.cache.impl;

import com.ld.common.core.cache.DataCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "spring.redis.enabled", havingValue = "false")
@EnableScheduling
public class VMCache implements DataCache {
    private Map<String, Long> expireMap = new HashMap<>();
    private Map<String, Object> cache = new HashMap<>();
    @Override
    public <T> void setCacheObject(String key, T value) {
        cache.put(key, value);
    }

    @Override
    public <T> void setCacheObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
        cache.put(key, value);
        expireMap.put(key, System.currentTimeMillis() + timeUnit.toMillis(timeout));
    }

    @Override
    public boolean expire(String key, long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public boolean expire(String key, long timeout, TimeUnit unit) {
        expireMap.put(key, System.currentTimeMillis() + unit.toMillis(timeout));
        return true;
    }

    @Override
    public long getExpire(String key) {
        return expireMap.get(key) == null ? 0 : expireMap.get(key);
    }

    @Override
    public Boolean hasKey(String key) {
        return cache.containsKey(key);
    }

    @Override
    public <T> T getCacheObject(String key) {
        return (T) cache.get(key);
    }

    @Override
    public boolean deleteObject(String key) {
        if (cache.remove(key) == null) {
            return false;
        } else {
            expireMap.remove(key);
            return true;
        }
    }

    @Override
    public boolean deleteObject(Collection collection) {
        AtomicInteger countRemoved = new AtomicInteger();
        collection.forEach(item -> {
            if (item != null) {
                // 尝试从cache和expireMap中移除item，并安全地更新计数器
                if (cache.remove(item) != null) {
                    countRemoved.incrementAndGet();
                    expireMap.remove(item);
                }
            }
        });
        return countRemoved.intValue() > 0;
    }

    @Override
    public <T> long setCacheList(String key, List<T> dataList) {
        cache.put(key, dataList);
        return dataList.size();
    }

    @Override
    public <T> List<T> getCacheList(String key) {
        return (List<T>) cache.get(key);
    }

    @Override
    public <T> void setCacheSet(String key, Set<T> dataSet) {
        cache.put(key, dataSet);
    }

    @Override
    public <T> Set<T> getCacheSet(String key) {
        return (Set<T>) cache.get(key);
    }

    @Override
    public <T> void setCacheMap(String key, Map<String, T> dataMap) {
        cache.put(key, dataMap);

    }

    @Override
    public <T> Map<String, T> getCacheMap(String key) {
        return (Map<String, T>) cache.get(key);
    }

    @Override
    public <T> void setCacheMapValue(String key, String hKey, T value) {
        if (cache.containsKey(key)) {
            ((Map<String, T>) cache.get(key)).put(hKey, value);
        } else {
            Map<String, T> map = new HashMap<>();
            map.put(hKey, value);
            cache.put(key, map);
        }
    }

    @Override
    public <T> T getCacheMapValue(String key, String hKey) {
        Map<String, T> map = (Map<String, T>) cache.get(key);
        if (map != null) {
            return map.get(key);
        }
        return null;
    }

    @Override
    public <T> List<T> getMultiCacheMapValue(String key, Collection<Object> hKeys) {
        List<T> list = new ArrayList<>();
        Map<String, T> map = (Map<String, T>) cache.get(key);
        if (map != null) {
            map.forEach((k, v) -> {
                list.add(v);
            });
            return list;
        }
        return null;
    }

    @Override
    public boolean deleteCacheMapValue(String key, String hKey) {
        if (cache.containsKey(key)) {
            ((Map) cache.get(key)).remove(hKey);
            return true;
        }
        return false;
    }

    @Override
    public Collection<String> keys(String pattern) {
        Collection<String> collection = cache.keySet().stream().filter(item -> {
            return item.startsWith(pattern);
        }).collect(Collectors.toCollection(ArrayList::new));
        return collection;
    }

    @Scheduled(fixedRate = 60000)
    public void checkCache() {
        if (expireMap.isEmpty()) {
            return;
        }
        // 每隔1分钟检查一次缓存中的过期项并删除
        Iterator<Map.Entry<String, Long>> iterator = expireMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if (entry.getValue() != null && entry.getValue() < System.currentTimeMillis()) {
                cache.remove(entry.getKey());
                iterator.remove();
            }
        }
    }
}
