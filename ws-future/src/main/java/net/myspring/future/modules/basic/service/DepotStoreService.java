package net.myspring.future.modules.basic.service;

import com.google.common.collect.Maps;
import net.myspring.cloud.modules.kingdee.domain.BdStock;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.dto.DepotReportDto;
import net.myspring.future.modules.basic.dto.DepotStoreDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.web.form.DepotStoreForm;
import net.myspring.future.modules.basic.web.query.DepotStoreQuery;
import net.myspring.future.modules.crm.web.query.ReportQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/5/12.
 */
@Service
@Transactional(readOnly = true)
public class DepotStoreService {
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CloudClient cloudClient;

    public Page<DepotStoreDto> findPage(Pageable pageable, DepotStoreQuery depotStoreQuery){
        depotStoreQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<DepotStoreDto> page=depotStoreRepository.findPage(pageable,depotStoreQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<DepotStoreDto> findFilter(DepotStoreQuery depotStoreQuery){
        List<DepotStoreDto> list=depotStoreRepository.findFilter(depotStoreQuery);
        cacheUtils.initCacheInput(list);
        return list;
    }

    public DepotStoreDto findOne(DepotStoreDto depotStoreDto) {
        if(!depotStoreDto.isCreate()) {
            DepotStore depotStore =depotStoreRepository.findOne(depotStoreDto.getId());
            depotStoreDto= BeanUtil.map(depotStore,DepotStoreDto.class);
            cacheUtils.initCacheInput(depotStoreDto);
        }
        return depotStoreDto;
    }

    @Transactional
    public DepotStore save(DepotStoreForm depotStoreForm) {
        DepotStore depotStore;
        //保存depot
        Depot depot = BeanUtil.map(depotStoreForm.getDepotForm(), Depot.class);
        depot.setNamePinyin(StringUtils.getFirstSpell(depot.getName()));
        depotRepository.save(depot);
        //保存depotStore
        if(depotStoreForm.isCreate()) {
            depotStoreForm.setDepotId(depot.getId());
            depotStore = BeanUtil.map(depotStoreForm,DepotStore.class);
            depotStoreRepository.save(depotStore);
        } else {
            depotStore = depotStoreRepository.findOne(depotStoreForm.getId());
            ReflectionUtil.copyProperties(depotStoreForm,depotStore);
            depotStore.setDepotId(depot.getId());
            depotStoreRepository.save(depotStore);
        }
        return depotStore;
    }

    public Integer setReportData(List<DepotStoreDto> depotStoreList,ReportQuery reportQuery) {
        reportQuery.setDepotIds(CollectionUtil.extractToList(depotStoreList,"depotId"));
        List<DepotReportDto> depotReportList = depotStoreRepository.findStoreReport(reportQuery);
        Map<String,DepotReportDto> depotReportMap= CollectionUtil.extractToMap(depotReportList,"depotId");
        for(DepotStoreDto depotStore:depotStoreList){
            DepotReportDto depotReportDto=depotReportMap.get(depotStore.getDepotId());
            if(depotReportDto!=null){
                depotStore.setQty(depotReportDto.getQty());
            }
        }
        return setPercentage(depotStoreList);
    }

    public Map<String,Integer> getReportDetail(ReportQuery reportQuery) {
        Map<String,Integer> map= Maps.newHashMap();
        List<DepotReportDto> depotReportList = depotStoreRepository.findStoreReport(reportQuery);
        if(CollectionUtil.isNotEmpty(depotReportList)){
            for(DepotReportDto depotReport:depotReportList){
                String key=depotReport.getProductName();
                if(!map.containsKey(key)){
                    map.put(key,0);
                }
                map.put(key,map.get(key)+1);
            }
        }
        return map;
    }

    private Integer setPercentage(List<DepotStoreDto> depotStoreList) {
        Integer sum = 0;
        for (DepotStoreDto depotStore : depotStoreList) {
            sum +=  depotStore.getQty();
        }
        for (DepotStoreDto depotStore : depotStoreList) {
            depotStore.setPercentage(StringUtils.division(sum, depotStore.getQty()));
        }
        return sum;
    }

    @Transactional
    public void logicDelete(String id) {
        depotStoreRepository.logicDelete(id);
    }

    @Transactional
    public void syn() {
        List<BdStock> bdstocks = cloudClient.getAllStock();
        List<DepotStore> outIdDepotStoreList=depotStoreRepository.findByOutIdIn(CollectionUtil.extractToList(bdstocks,"FStockId"));
        Map<String,DepotStore> depotStoreMap=CollectionUtil.extractToMap(outIdDepotStoreList,"outId");
        List<Depot> depotList=depotRepository.findByNameList(CollectionUtil.extractToList(bdstocks,"FName"));
        Map<String,Depot> depotMap=CollectionUtil.extractToMap(depotList,"name");
        for(BdStock bdStock:bdstocks){
            DepotStore store=depotStoreMap.get(bdStock.getFStockId());
            if(store==null){
                store=new DepotStore();
            }
            Depot depot=depotMap.get(bdStock.getFName());
            if(depot==null){
                depot=new Depot();
            }
            store.setOutId(bdStock.getFStockId());
            store.setOutGroupId(bdStock.getFGroup());
            store.setOutGroupName(bdStock.getFGroupName());
            store.setOutDate(bdStock.getFModifyDate());
            store.setOutCode(bdStock.getFNumber());
            store.setEnabled(true);
            depotStoreRepository.save(store);
            depot.setName(bdStock.getFName());
            store.setEnabled(true);
            depot.setIsHidden(false);
            depot.setNamePinyin(StringUtils.getFirstSpell(depot.getName()));
            depot.setDepotStoreId(store.getId());
            depotRepository.save(depot);
            store.setDepotId(depot.getId());
            depotStoreRepository.save(store);
        }
    }
}
