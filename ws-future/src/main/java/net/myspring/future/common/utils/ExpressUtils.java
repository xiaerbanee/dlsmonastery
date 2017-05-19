package net.myspring.future.common.utils;

import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by liuj on 2017/5/18.
 */
@Component
public class ExpressUtils {
    @Autowired
    private RedisTemplate redisTemplate;

    public Integer getExpressPrintQty(Integer totalBillQty) {
        Integer expressPrintQty = Integer.valueOf(CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.EXPRESS_PRINT_QTY.name()).getValue());
        if(0 == totalBillQty % expressPrintQty){
            expressPrintQty = totalBillQty / expressPrintQty;
        } else{
            expressPrintQty = totalBillQty / expressPrintQty + 1;
        }
        return expressPrintQty;
    }
}
