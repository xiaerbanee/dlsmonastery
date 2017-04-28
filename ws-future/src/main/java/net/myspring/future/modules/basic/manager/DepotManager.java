package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lihx on 2017/4/20.
 */
@Component
@CacheConfig(cacheNames = "depots")
public class DepotManager {
    @Autowired
    private DepotMapper depotMapper;

    @Cacheable(key="#p0")
    public Depot findOne(String id) {
        return depotMapper.findOne(id);
    }

    @Cacheable(key="#p0.id")
    public Depot save(Depot depot){
        depotMapper.save(depot);
        return  depot;
    }

    @CachePut(key="#p0.id")
    public Depot update(Depot depot){
        depotMapper.update(depot);
        return  depotMapper.findOne(depot.getId());
    }

    @CachePut(key="#p0.id")
    public Depot updateForm(DepotForm depotForm){
        depotMapper.updateForm(depotForm);
        return  depotMapper.findOne(depotForm.getId());
    }

    @CacheEvict(key="#p0")
    public int deleteById(String id) {
        return depotMapper.deleteById(id);
    }

    public List<String> getDepotIds(String accountId) {
        List<Depot> depotList=depotMapper.findByAccountId(accountId);
        return CollectionUtil.extractToList(depotList,"id");
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}
