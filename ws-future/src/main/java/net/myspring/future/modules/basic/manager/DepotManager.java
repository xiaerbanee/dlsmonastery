package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public List<String> getDepotIds(String accountId) {
        List<Depot> depotList=depotMapper.findByAccountId(accountId);
        return CollectionUtil.extractToList(depotList,"id");
    }

}
