package dev.song.redis.cache;

import dev.song.redis.ShardRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by song on 17/8/3.
 */
@Configuration
@EnableCaching
public class RedisCacheConfig {

    public static final String redisCache = "redis-cache";
    public static final String redisCacheTemplate = "redis-cache-template";

    @Autowired
    private ShardRedisTemplate shardRedisTemplate;

    @Bean(name = redisCache)
    public CacheManager cacheManager() {
        Map<String,Integer> map = new ConcurrentHashMap<>();
        map.put(KeyCache.TEST_KEY, 1*60);  //1分钟失效
        map.put(KeyCache.MULTI_KEY, 2*60);  //2分钟失效

        ShardRedisCacheManager manager = new ShardRedisCacheManager(shardRedisTemplate);
        manager.setDefaultExpiration(5*60); //默认5分钟
        manager.setExpires(map);
        return manager;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder("cache:");
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params)
                if (obj!=null){
                    sb.append(obj.toString());
                }
            return sb.toString();
        };

    }
}
