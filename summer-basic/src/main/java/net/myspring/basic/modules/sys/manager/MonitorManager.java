package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.Monitor;
import net.myspring.basic.modules.sys.mapper.MonitorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
@Component
public class MonitorManager {

    @Autowired
    private MonitorMapper monitorMapper;

    @Cacheable(value = "monitors",key="#p0")
    public Monitor findOne(String id) {
        return monitorMapper.findOne(id);
    }

    @Cacheable(value = "monitors",key="#p0.id")
    public Monitor save(Monitor monitor){
        monitorMapper.save(monitor);
        return  monitor;
    }

    @CachePut(value = "monitors",key="#p0.id")
    public Monitor update(Monitor monitor){
        monitorMapper.update(monitor);
        return  monitorMapper.findOne(monitor.getId());
    }

    @CacheEvict(value = "monitors",key="#p0")
    public int deleteById(String id) {
        return monitorMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}
