package net.myspring.future.modules.basic.manager;

import com.google.common.collect.Maps;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/20.
 */
@Component
public class DepotManager {

    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    public List<String>  filterDepotIds(){
        List<Depot> depotList=depotMapper.findByAccountId(RequestUtils.getAccountId());
        return CollectionUtil.extractToList(depotList,"id");
    }
}
