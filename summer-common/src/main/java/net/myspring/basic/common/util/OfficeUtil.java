package net.myspring.basic.common.util;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.util.cahe.CacheReadUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OfficeUtil {

    public static OfficeDto findOne(RedisTemplate redisTemplate, String id) {
        Map<String,Object> map= CacheReadUtils.getCache(redisTemplate,"offices:" + id);
        if(map != null) {
            OfficeDto officeDto = new OfficeDto();
            officeDto.setJointType((String) map.get("jointType"));
            officeDto.setId((String) map.get("id"));
            officeDto.setName((String) map.get("name"));
            officeDto.setOfficeRuleId((String) map.get("officeRuleId"));
            officeDto.setJointLevel((String) map.get("jointLevel"));
            officeDto.setParentId((String) map.get("parentId"));
            officeDto.setParentIds((String) map.get("parentIds"));
            officeDto.setType((String) map.get("type"));
            officeDto.setAreaId((String) map.get("areaId"));
            if(map.get("point")!=null){
                officeDto.setPoint(new BigDecimal((double)((List) map.get("point")).get(1)));
            }else {
                officeDto.setPoint(BigDecimal.ZERO);
            }
            return officeDto;
        } else {
            return null;
        }
    }
}
