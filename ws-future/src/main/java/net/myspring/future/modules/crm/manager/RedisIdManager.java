package net.myspring.future.modules.crm.manager;

import net.myspring.future.modules.crm.repository.GoodsOrderRepository;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RedisIdManager {
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    private GoodsOrderRepository goodsOrderRepository;

    private final Long initialValue = -1L;

    public synchronized String getNextGoodsOrderBusinessId(LocalDate localDate) {
        String key = "goodsOrderBusinessId:" + LocalDateUtils.format(localDate);
        String businessId;
        RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key,jedisConnectionFactory,initialValue);
        if(redisAtomicLong.get()==initialValue) {
            businessId = goodsOrderRepository.findMaxBusinessId(localDate);
            if(businessId==null) {
                businessId = IdUtils.getNextBusinessId(businessId,localDate);
            }
            redisAtomicLong.set(Long.valueOf(businessId));
        }else {
            businessId = String.valueOf(redisAtomicLong.incrementAndGet());
        }
        return businessId;
    }


}
