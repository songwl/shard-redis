package dev.song.redis;

import dev.song.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisShardInfo;

/**
 * Created by song on 17/9/19.
 */
public class ShardRedisTemplateTest extends BaseTest{

    @Autowired
    private ShardRedisTemplate shardRedisTemplate;

    @Test
    public void testShardRedisSet() {
        for (int i=0;i<10;++i) {
            shardRedisTemplate.set("key"+i, "Please Call me key"+i);
        }
    }

    @Test
    public void testShardInfo() {
        for (int i=0;i<10;++i) {
            JedisShardInfo shardInfo = shardRedisTemplate.getShardInfo("key"+i);
            System.out.println("shardingInfo="+shardInfo.getHost()+":"+shardInfo.getPort());
        }
    }

    @Test
    public void testShardRedisGet() {
        for (int i=0;i<10;++i) {
            System.out.println(shardRedisTemplate.get("key"+i));
        }
    }
}