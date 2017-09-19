package dev.song.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 17/8/2.
 */
@Configuration
@ConfigurationProperties(prefix = "redis")
public class ShardRedisConfig {

    private String shards;

    private JedisPoolConfig pool;

    public String getShards() {
        return shards;
    }

    public void setShards(String shards) {
        this.shards = shards;
    }

    public JedisPoolConfig getPool() {
        return pool;
    }

    public void setPool(JedisPoolConfig pool) {
        this.pool = pool;
    }

    @Bean("shardedJedisPool")
    public ShardedJedisPool shardedJedisPool(){
        List<JedisShardInfo> shardInfos = new ArrayList<>();

        String[] hostAndPortArr = shards.split(",");
        for (String a : hostAndPortArr) {
            String[] hostAndPort = a.split(":");
            JedisShardInfo jedisShardInfo = new JedisShardInfo(hostAndPort[0],Integer.valueOf(hostAndPort[1]));
            shardInfos.add(jedisShardInfo);
        }
        return new ShardedJedisPool(pool,shardInfos);
    }

    @Bean("shardRedisConnectionFactory")
    public ShardRedisConnectionFactory shardRedisConnectionFactory(@Qualifier("shardedJedisPool") ShardedJedisPool shardedJedisPool) {
        return new ShardRedisConnectionFactory(shardedJedisPool);
    }

    @Bean("shardRedisTemplate")
    public ShardRedisTemplate shardRedisTemplate(@Qualifier("shardRedisConnectionFactory") ShardRedisConnectionFactory shardRedisConnectionFactory) {
        ShardRedisTemplate shardRedisTemplate = new ShardRedisTemplate();
        shardRedisTemplate.setFactory(shardRedisConnectionFactory);
        return shardRedisTemplate;
    }
}
