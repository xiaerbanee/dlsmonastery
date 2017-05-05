package net.myspring.basic.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.myspring.basic.modules.sys.dto.DictEnumCacheDto;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuj on 2017/5/3.
 */
public class DictEnumUtil {
    private static ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

    public static List<DictEnumCacheDto> findByCateogry(RedisTemplate redisTemplate, String category) {
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        byte[] bytes = (byte[]) redisTemplate.opsForValue().get(("dictEnums:"+category).getBytes());
        List<DictEnumCacheDto> dictEnumDto = ObjectMapperUtils.readValue(objectMapper,new String(bytes), ArrayList.class);
        return dictEnumDto;
    }

}
