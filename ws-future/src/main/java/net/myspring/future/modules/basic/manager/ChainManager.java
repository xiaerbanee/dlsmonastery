package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.mapper.ChainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by lihx on 2017/4/20.
 */
@Component
public class ChainManager {
    @Autowired
    private ChainMapper chainMapper;

    @Cacheable(value = "accounts",key="#p0")
    public Chain findOne(String id) {
        return chainMapper.findOne(id);
    }


}
