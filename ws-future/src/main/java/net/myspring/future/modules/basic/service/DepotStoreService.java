package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
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

    public List<DepotReportDto> setReportData(ReportQuery reportQuery) {
        reportQuery.setOfficeIdList(officeClient.getOfficeFilterIds(RequestUtils.getRequestEntity().getOfficeId()));
        reportQuery.setDepotIdList(depotManager.filterDepotIds());
        DepotQuery depotQuery = BeanUtil.map(reportQuery, DepotQuery.class);
        List<Depot> depotList = depotRepository.findByFilter(depotQuery);
        List<DepotReportDto> depotReportList = getProductImeReportList(reportQuery);
        Map<String,DepotReportDto> map= CollectionUtil.extractToMap(depotReportList,"depotId");
        for(Depot depot:depotList){
            if(!map.containsKey(depot.getId())){
                DepotReportDto depotReport = new DepotReportDto();
                depotReport.setDepotId(depot.getId());
                depotReport.setQty(0);
                depotReport.setDepotName(depot.getName());
                depotReportList.add(depotReport);
            }
        }
        setPercentage(depotReportList);
        return depotReportList;
    }

    public List<DepotReportDto> getProductImeReportList(ReportQuery reportQuery) {
        List<DepotReportDto> depotReportList = Lists.newArrayList();
        return depotReportList;
    }

    private void setPercentage(List<DepotReportDto> depotReportList) {
        Integer sum = 0;
        for (DepotReportDto depotReport : depotReportList) {
            sum = sum + depotReport.getQty();
        }
        for (DepotReportDto depotReport : depotReportList) {
            depotReport.setPercent(StringUtils.division(sum, depotReport.getQty()));
        }
    }

    public void logicDelete(String id) {
        depotStoreRepository.logicDelete(id);
    }


}
