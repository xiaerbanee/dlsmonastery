package net.myspring.basic.common.utils;

import com.google.common.collect.Lists;
import net.myspring.util.cahe.CacheReadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by liuj on 2017/4/2.
 */
@Component
public class CacheUtils {
    @Autowired
    private RedisTemplate redisTemplate;

    public void initCacheInput(Object object) {
        CacheReadUtils.initCacheInput(redisTemplate,object);
    }

    public void initCacheInput(Collection objects) {
        CacheReadUtils.initCacheInput(redisTemplate,objects);
    }


}
