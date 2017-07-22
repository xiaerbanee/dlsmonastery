package net.myspring.cloud.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.DynamicRedisConnectionFactory;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

/**
 * Created by liuj on 2017/3/24.
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

    @Autowired
    private Environment environment;
    @Value("${companyNames}")
    private String[] companyNames;

    @Bean
    public DynamicRedisConnectionFactory redisConnectionFactory() {
        Map<String,JedisConnectionFactory> targetConnections = Maps.newHashMap();
        for(String companyName:companyNames) {
            targetConnections.put(companyName,getConnectionFactory("spring.redis." + companyName));
        }
        DynamicRedisConnectionFactory dynamicRedisConnectionFactory = new DynamicRedisConnectionFactory();
        dynamicRedisConnectionFactory.setTargetConnections(targetConnections);
        dynamicRedisConnectionFactory.setDefaultLookupKey(companyNames[0]);
        return dynamicRedisConnectionFactory;
    }

    private JedisConnectionFactory getConnectionFactory(String prefix) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(environment.getProperty(prefix + ".host"));
        jedisConnectionFactory.setPort(environment.getProperty(prefix + ".port",Integer.class));
        jedisConnectionFactory.setPassword(environment.getProperty(prefix + ".password"));
        jedisConnectionFactory.setDatabase(environment.getProperty(prefix + ".database",Integer.class));
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(environment.getProperty(prefix + ".max-idle",Integer.class));
        jedisPoolConfig.setMaxTotal(environment.getProperty(prefix + ".max-total",Integer.class));
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }


    @Bean
    RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setUsePrefix(true);
        redisCacheManager.setTransactionAware(true);
        return redisCacheManager;
    }
}