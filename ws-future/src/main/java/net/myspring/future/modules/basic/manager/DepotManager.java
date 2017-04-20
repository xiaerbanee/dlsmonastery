package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by lihx on 2017/4/20.
 */
@Component
public class DepotManager {
    @Autowired
    private DepotMapper depotMapper;

    @Cacheable(value = "depots",key="#p0")
    public Depot findOne(String id) {
        return depotMapper.findOne(id);
    }
}
