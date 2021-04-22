package com.docky.wiki.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Docky
 * @date 2021/4/22 14:14
 */
@Component
public class RedisUtil {

    private final Logger log = LoggerFactory.getLogger(RedisUtil.class);
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * true: 不存在，放一个KEY
     * false: 已存在
     *
     * @param key
     * @param second
     * @return
     */
    public boolean validateRepeat(String key, long second) {
        if (redisTemplate.hasKey(key)) {
            log.info("key已经存在：{}", key);
            return false;
        } else {
            log.info("key不存在,放入：{},过期{}秒", key, second);
            redisTemplate.opsForValue().set(key, key, second, TimeUnit.SECONDS);
            return true;
        }
    }
}
