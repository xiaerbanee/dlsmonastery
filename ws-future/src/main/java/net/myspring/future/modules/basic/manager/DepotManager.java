package net.myspring.future.modules.basic.manager;

import com.google.common.collect.Lists;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.cloud.modules.kingdee.domain.StkInventory;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DepotManager {

    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private RedisTemplate redisTemplate;

    public Depot save(Depot depot) {
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

    public boolean isAccess(String depotId, boolean checkChain,String accountId) {
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

    private List<String> getChainIds(String accountId) {
        DepotQuery depotQuery=new DepotQuery();
        depotQuery.setDepotIdList(filterDepotIds(accountId));
        return depotRepository.findChainIds(depotQuery);
    }

    private List<String> getMergeStoreIds(){
        CompanyConfigCacheDto companyConfigCacheDto =  CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.MERGE_STORE_IDS.name());
        if(companyConfigCacheDto == null || StringUtils.isBlank(companyConfigCacheDto.getValue())){
            return null;
        }
        return StringUtils.getSplitList(companyConfigCacheDto.getValue(), ",");
    }

    public Map<String, Integer> getCloudQtyMap(String depotId){
        //针对昌东仓库做特殊处理
        String queryDepotId = depotId;
        List<String> mergeStoreIds = getMergeStoreIds();
        if(CollectionUtil.isNotEmpty(mergeStoreIds) && mergeStoreIds.get(1).equals(depotId)){
            queryDepotId = mergeStoreIds.get(0);
        }

        DepotStore depotStore = depotStoreRepository.findByEnabledIsTrueAndDepotId(queryDepotId);
        if(depotStore == null || StringUtils.isBlank(depotStore.getOutId())){
            throw new ServiceException("depotId没有对应的金蝶仓库");
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

    public Map<String, CustomerReceiveDto> getLatestCustomerReceiveMap(List<String> clientOutIdList) {
        CustomerReceiveQuery customerReceiveQuery = new CustomerReceiveQuery();
        customerReceiveQuery.setDateStart(LocalDate.now().plusDays(30));
        customerReceiveQuery.setDateEnd(customerReceiveQuery.getDateStart());
        customerReceiveQuery.setCustomerIdList(clientOutIdList);

        List<CustomerReceiveDto> customerReceiveDtoList = cloudClient.getCustomerReceiveList(customerReceiveQuery);
        Map<String, CustomerReceiveDto> customerReceiveDtoMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(customerReceiveDtoList)){
            customerReceiveDtoMap = CollectionUtil.extractToMap(customerReceiveDtoList, "customerId");
        }
        return customerReceiveDtoMap;
    }

    public CustomerReceiveDto getLatestCustomerReceive(String clientOutId) {
        CustomerReceiveQuery customerReceiveQuery = new CustomerReceiveQuery();
        customerReceiveQuery.setDateStart(LocalDate.now().plusDays(30));
        customerReceiveQuery.setDateEnd(customerReceiveQuery.getDateStart());
        customerReceiveQuery.setCustomerIdList(Lists.newArrayList(clientOutId));
        List<CustomerReceiveDto> customerReceiveDtoList = cloudClient.getCustomerReceiveList(customerReceiveQuery);
        return CollectionUtil.isNotEmpty(customerReceiveDtoList) ? customerReceiveDtoList.get(0) : null;
    }
}
