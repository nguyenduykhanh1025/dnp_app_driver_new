package vn.com.irtech.eport.common.config.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.common.utils.spring.SpringUtils;

import java.util.concurrent.ConcurrentHashMap;

// @Service
public class RedisCacheManager implements CacheManager {
    @Value("${eport.cacheExpireTime}")
    private final long cacheExpireTime = 3600;

    private final ConcurrentHashMap<String, Cache> caches = new ConcurrentHashMap<>();

    private final RedisTemplate redisTemplate = SpringUtils.getBean("redisTemplate");

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        Cache cache = caches.get(name);
        if (cache == null) {
            synchronized (this) {
                cache = new RedisCache<K, V>(name, cacheExpireTime, redisTemplate);
                caches.put(name, cache);
            }
        }
        return cache;
    }
}
