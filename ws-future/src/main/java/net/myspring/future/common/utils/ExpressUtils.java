package net.myspring.future.common.utils;

import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExpressUtils {
    @Autowired
    private RedisTemplate redisTemplate;

    public Integer getExpressPrintQty(Integer totalBillQty) {

        Integer config = Integer.valueOf(CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.EXPRESS_PRINT_QTY.name()).getValue());
        if(0 == totalBillQty % config){
            return totalBillQty / config;
        } else{
            return totalBillQty / config + 1;
        }
    }
}
