package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.common.enums.OutTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.dto.DepotReportDto;
import net.myspring.future.modules.basic.dto.DepotStoreDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.form.DepotStoreForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
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
@Transactional
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

    public Page<DepotStoreDto> findPage(Pageable pageable, DepotStoreQuery depotStoreQuery){
        Page<DepotStoreDto> page=depotStoreRepository.findPage(pageable,depotStoreQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DepotStoreForm getForm(DepotStoreForm depotStoreForm) {
        if(!depotStoreForm.isCreate()) {
            DepotStore depotStore =depotStoreRepository.findOne(depotStoreForm.getId());
            depotStoreForm= BeanUtil.map(depotStore,DepotStoreForm.class);
            Depot depot=depotRepository.findOne(depotStoreForm.getDepotId());
            depotStoreForm.setDepotForm(BeanUtil.map(depot,DepotForm.class));
            cacheUtils.initCacheInput(depotStoreForm);
        }
        return depotStoreForm;
    }

    public DepotStore save(DepotStoreForm depotStoreForm) {
        DepotStore depotStore;
        DepotForm depotForm = depotStoreForm.getDepotForm();
        //保存depot
        Depot depot = BeanUtil.map(depotForm, Depot.class);
        depot.setNamePinyin(StringUtils.getFirstSpell(depotStoreForm.getDepotForm().getName()));
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

    public List<DepotStoreDto> setReportData(List<DepotStoreDto> depotStoreList,ReportQuery reportQuery) {
        List<DepotReportDto> depotReportList = depotStoreRepository.findStoreReport(reportQuery);
        Map<String,DepotReportDto> map= CollectionUtil.extractToMap(depotReportList,"depotId");
        for(DepotStoreDto depotStore:depotStoreList){
            DepotReportDto depotReportDto=map.get(depotStore.getDepotId());
            depotStore.setQty(depotReportDto.getQty());
        }
        setPercentage(depotStoreList);
        return depotStoreList;
    }

    public Map<String,Integer> getReportDetail(ReportQuery reportQuery) {
        Map<String,Integer> map= Maps.newHashMap();
        List<DepotReportDto> depotReportList = depotStoreRepository.findStoreReport(reportQuery);
        if(CollectionUtil.isNotEmpty(depotReportList)){
            for(DepotReportDto depotReport:depotReportList){
                String key=depotReport.getProductTypeName();
                if(!map.containsKey(key)){
                    map.put(key,0);
                }
                map.put(key,map.get(key)+1);
            }
        }
        return map;
    }

    private void setPercentage(List<DepotStoreDto> depotStoreList) {
        Integer sum = 0;
        for (DepotStoreDto depotStore : depotStoreList) {
            sum +=  depotStore.getQty();
        }
        for (DepotStoreDto depotStore : depotStoreList) {
            depotStore.setPercentage(StringUtils.division(sum, depotStore.getQty()));
        }
    }

    public void logicDelete(String id) {
        depotStoreRepository.logicDelete(id);
    }


}
