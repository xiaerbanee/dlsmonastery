package net.myspring.future.modules.basic.manager;

import net.myspring.cloud.modules.kingdee.domain.StkInventory;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/5/15.
 */
@Component
public class DepotManager {

    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CloudClient cloudClient;

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

    public List<String> filterDepotIds(String accountId){
        List<Depot> depotList=depotRepository.findByAccountId(accountId);
        return CollectionUtil.extractToList(depotList,"id");
    }

    public boolean isAccess(String depotId, boolean checkChain,String accountId,String officeId) {
        Depot depot=depotRepository.findOne(depotId);
        List<String> depotIds = filterDepotIds(accountId);
        List<String> officeIds= RequestUtils.getOfficeIdList();
        if(CollectionUtil.isNotEmpty(depotIds)) {
            if(depotIds.contains(depot.getId())) {
                return true;
            }
        } else {
            if(CollectionUtil.isEmpty(officeIds)||officeIds.contains(depot.getOfficeId())) {
                return true;
            }
        }
        if(checkChain && StringUtils.isNotBlank(depot.getChainId())) {
            List<String> chainIds = getChainIds(accountId);
            if(CollectionUtil.isNotEmpty(chainIds) && chainIds.contains(depot.getChainId())) {
                return true;
            }
        }
        return false;
    }

    public List<String> getChainIds(String accountId) {
        DepotQuery depotQuery=new DepotQuery();
        depotQuery.setDepotIdList(filterDepotIds(accountId));
        List<String> chainIds = depotRepository.findChainIds(depotQuery);
        return chainIds;
    }


    public Map<String, Integer> getCloudQtyMap(String depotId){
        DepotStore depotStore = depotStoreRepository.findByEnabledIsTrueAndDepotId(depotId);
        if(depotStore == null){
            return new HashMap<>();
        }
        List<StkInventory> inventoryList = cloudClient.findInventoriesByDepotStoreOutIds(Collections.singletonList(depotStore.getOutId()));
        Map<String, Integer> result = new HashMap<>();
        for(StkInventory stkInventory : inventoryList){
            if(stkInventory.getFBaseQty() !=null && stkInventory.getFBaseQty() >0){
                result.put(stkInventory.getFMaterialId(),  stkInventory.getFBaseQty());
            }
        }

        return result;
    }
}
