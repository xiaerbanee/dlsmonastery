package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.Pricesystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by lihx on 2017/4/20.
 */
@Component
public class PricesystemManager {
    @Autowired
    private PricesystemManager pricesystemManager;

    @Cacheable(value = "pricesystems",key="#p0")
    public Pricesystem findOne(String id) {
        return pricesystemManager.findOne(id);
    }
}
