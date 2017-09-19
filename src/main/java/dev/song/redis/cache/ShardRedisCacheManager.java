package dev.song.redis.cache;

import dev.song.redis.ShardRedisTemplate;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by song on 17/9/12.
 */
public class ShardRedisCacheManager implements CacheManager {

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap(16);
    private volatile Set<String> cacheNames = Collections.emptySet();
    private int defaultExpiration;
    private Map<String, Integer> expires;
    private ShardRedisTemplate shardRedisTemplate;
    private RedisSerializer keySerializer;
    private RedisSerializer valueSerializer;

    public ShardRedisCacheManager(ShardRedisTemplate shardRedisTemplate) {
        this(shardRedisTemplate,Collections.emptySet());
    }

    public ShardRedisCacheManager(ShardRedisTemplate shardRedisTemplate,Set<String> cacheNames) {
        this.shardRedisTemplate = shardRedisTemplate;
        this.cacheNames = cacheNames;
        this.defaultExpiration = 0;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        return cache==null? createCache(name) : cache;
    }

    private synchronized Cache createCache(String cacheName) {
        if (keySerializer==null) {
            keySerializer = new StringRedisSerializer();
        }
        if (valueSerializer==null) {
            valueSerializer = new JdkSerializationRedisSerializer();
        }
        ShardRedisCache cache = new ShardRedisCache(cacheName,shardRedisTemplate,keySerializer,valueSerializer);
        cache.setDefaultExpiration(this.computeExpiration(cacheName));

        if (cacheNames==null || cacheNames.size()==0) {
            cacheNames = new LinkedHashSet<>();
        }
        cacheNames.add(cacheName);
        cacheMap.put(cacheName,cache);
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheNames;
    }

    public void setCacheNames(Set<String> cacheNames) {
        this.cacheNames = cacheNames;
    }

    public int getDefaultExpiration() {
        return defaultExpiration;
    }

    public void setDefaultExpiration(int defaultExpiration) {
        this.defaultExpiration = defaultExpiration;
    }

    public Map<String, Integer> getExpires() {
        return expires;
    }

    public void setExpires(Map<String, Integer> expires) {
        this.expires = expires;
    }

    public void setKeySerializer(RedisSerializer keySerializer) {
        this.keySerializer = keySerializer;
    }

    public void setValueSerializer(RedisSerializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    protected int computeExpiration(String name) {
        Integer expiration = null;
        if(this.expires != null) {
            expiration = this.expires.get(name);
        }
        return expiration != null?expiration.intValue():this.defaultExpiration;
    }
}
