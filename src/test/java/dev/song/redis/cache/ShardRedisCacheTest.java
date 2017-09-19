package dev.song.redis.cache;

import dev.song.BaseTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Created by song on 17/9/19.
 */
public class ShardRedisCacheTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ShardRedisCacheTest.class);

    @Autowired
    private SimpleComponent simpleComponent;

    @Test
    public void cacheTest() {
        String uuid = UUID.randomUUID().toString();
        for (int i=0;i<10;i++){
            log.info(simpleComponent.newString(uuid));

            if (i==6) {
                simpleComponent.removeString(uuid);
            }
        }
    }
}