package net.myspring.cloud.common.utils;

import com.google.common.collect.Maps;
import net.myspring.util.cahe.CacheReadUtils;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    public void initCache(String keyPrefix, List<Object> entities) {
        initCache(keyPrefix,entities,"id");
    }

    public  void  initCache(String keyPrefix, List<Object> entities,String keyField) {
        if(CollectionUtil.isNotEmpty(entities)) {
            Map<byte[],byte[]> tuple = Maps.newHashMap();
            for(Object object:entities) {
                if(object != null) {
                    String key = keyPrefix + ":" + ReflectionUtil.getFieldValue(object,keyField);
                    tuple.put(redisTemplate.getKeySerializer().serialize(key),redisTemplate.getValueSerializer().serialize(object));
                }
            }
            if(tuple.size()>0) {
                RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
                redisConnection.mSet(tuple);
                RedisConnectionUtils.releaseConnection(redisConnection,redisTemplate.getConnectionFactory());
            }
        }
    }
}
