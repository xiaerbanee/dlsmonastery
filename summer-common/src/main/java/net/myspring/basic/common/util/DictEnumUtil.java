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
        RedisCallback<List<Object>> pipelineCallback = connection -> {
            connection.openPipeline();
            connection.get(("dictEnums:"+category).getBytes());
            return connection.closePipeline();
        };
        List<byte[]> cacheList = (List<byte[]>) redisTemplate.execute(pipelineCallback);
        String json = new String(cacheList.get(0));
        List<DictEnumCacheDto> dictEnumDto = ObjectMapperUtils.readValue(json, ArrayList.class);
        return dictEnumDto;
    }

}
