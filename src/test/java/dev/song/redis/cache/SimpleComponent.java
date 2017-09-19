package dev.song.redis.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by song on 17/9/12.
 */
@Component
public class SimpleComponent {

    private static final Logger log = LoggerFactory.getLogger(SimpleComponent.class);

    @Cacheable(value = KeyCache.TEST_KEY, key = "''+#uuid")
    public String newString(String uuid){
        log.info("SimpleComponent new a string with "+uuid);
        return "SimpleComponent-"+uuid;
    }

    @CacheEvict(value = KeyCache.TEST_KEY, key = "''+#uuid")
    public void removeString(String uuid){
        log.info("SimpleComponent remove a string with "+uuid);
    }
}
