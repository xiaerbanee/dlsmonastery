package net.myspring.basic.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by liuj on 2017/5/3.
 */
public class CompanyConfigUtil {
    private static ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

    public static CompanyConfigCacheDto findByCode(RedisTemplate redisTemplate,String companyId, String code) {
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        String key  = companyId + CharConstant.ENTER + code;
        CompanyConfigCacheDto companyConfigCacheDto = (CompanyConfigCacheDto) redisTemplate.opsForValue().get(("companyConfigCodes:" + key));
        if(companyConfigCacheDto==null) {
            companyConfigCacheDto = new CompanyConfigCacheDto();
        }
        return companyConfigCacheDto;
    }
}
