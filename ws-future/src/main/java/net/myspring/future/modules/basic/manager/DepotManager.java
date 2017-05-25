package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
