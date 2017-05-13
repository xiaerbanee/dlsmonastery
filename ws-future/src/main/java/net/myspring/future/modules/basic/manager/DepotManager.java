package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.mapper.DepotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by lihx on 2017/4/20.
 */
@Component
public class DepotManager {
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private RedisTemplate redisTemplate;


}
