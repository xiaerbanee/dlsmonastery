package net.myspring.basic.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.basic.modules.sys.dto.DictEnumCacheDto;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuj on 2017/5/3.
 */
public class CompanyConfigUtil {
    private static ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

    public static CompanyConfigCacheDto findByCode(RedisTemplate redisTemplate, String code) {
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        byte[] bytes = (byte[]) redisTemplate.opsForValue().get(("companyConfig:"+code).getBytes());
        CompanyConfigCacheDto companyConfigCacheDto = ObjectMapperUtils.readValue(objectMapper,new String(bytes), CompanyConfigCacheDto.class);
        return companyConfigCacheDto;
    }

}
