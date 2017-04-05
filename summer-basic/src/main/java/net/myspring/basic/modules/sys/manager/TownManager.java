package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.Town;
import net.myspring.basic.modules.sys.mapper.TownMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
@Component
public class TownManager {

    @Autowired
    private TownMapper townMapper;

    @Cacheable(value = "towns",key="#p0")
    public Town findOne(String id) {
        return townMapper.findOne(id);
    }

    @CachePut(value = "towns",key="#p0.id")
    public Town save(Town town){
        townMapper.save(town);
        return  town;
    }

    @CachePut(value = "towns",key="#p0.id")
    public Town update(Town town){
        townMapper.update(town);
        return  townMapper.findOne(town.getId());
    }
}
