package net.myspring.future.modules.basic.manager;

import com.google.common.collect.Maps;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/5/15.
 */
@Component
public class DepotManager {

    @Autowired
    private DepotRepository depotRepository;

    public Depot save(Depot depot) {
        if(StringUtils.isNotBlank(depot.getClientId())) {
            depot.setAdShop(true);
        }
        depotRepository.save(depot);
        if(StringUtils.isNotBlank(depot.getDelegateDepotId())) {
            Depot delegateDepot = depotRepository.findOne(depot.getDelegateDepotId());
            if(!depot.getId().equals(delegateDepot.getDelegateDepotId())) {
                delegateDepot.setDelegateDepotId(depot.getId());
                depotRepository.save(delegateDepot);
            }
        }
        return depot;
    }

    public List<String> filterDepotIds(){
        List<Depot> depotList=depotRepository.findByAccountId(RequestUtils.getAccountId());
        return CollectionUtil.extractToList(depotList,"id");
    }

    public boolean isAccess(Depot depot, boolean checkChain) {
        //TODO 修改判断逻辑DepotUtils.isAccess(fromDepot,true)
        return false;
    }

}
