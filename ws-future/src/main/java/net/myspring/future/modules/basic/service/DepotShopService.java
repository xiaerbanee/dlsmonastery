package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.common.enums.OutTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.client.TownClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.dto.DepotReportDetailDto;
import net.myspring.future.modules.basic.dto.DepotReportDto;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotShopRepository;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.form.DepotShopForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.basic.web.query.DepotShopQuery;
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
public class DepotShopService {
    @Autowired
    private DepotShopRepository depotShopRepository;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private TownClient townClient;

    public Page<DepotShopDto> findPage(Pageable pageable, DepotShopQuery depotShopQuery) {
        depotShopQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        if (StringUtils.isNotBlank(depotShopQuery.getOfficeId())) {
            depotShopQuery.getOfficeIdList().addAll(officeClient.getChildOfficeIds(depotShopQuery.getOfficeId()));
        }
        Page<DepotShopDto> page = depotShopRepository.findPage(pageable, depotShopQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DepotShopForm getForm(DepotShopForm depotShopForm) {
        if (!depotShopForm.isCreate()) {
            DepotShop depotShop = depotShopRepository.findOne(depotShopForm.getId());
            depotShopForm = BeanUtil.map(depotShop, DepotShopForm.class);
            cacheUtils.initCacheInput(depotShopForm);
        }
        return depotShopForm;
    }

    public DepotShopDto findOne(String id){
        DepotShopDto depotShopDto;
        if (StringUtils.isBlank(id)){
            depotShopDto = new DepotShopDto();
        }else {
            depotShopDto = depotShopRepository.findDto(id);
            cacheUtils.initCacheInput(depotShopDto);
        }
        return depotShopDto;
    }

    public DepotDto findShop(String id){
        DepotDto depotDto;
        if (StringUtils.isBlank(id)){
            depotDto = new DepotDto();
        }
        else{
            Depot depot = depotRepository.findOne(id);
            depotDto = BeanUtil.map(depot,DepotDto.class);
            cacheUtils.initCacheInput(depotDto);
        }
        return depotDto;
    }

    public DepotShop save(DepotShopForm depotShopForm) {
        DepotShop depotShop;
        if(StringUtils.isNotBlank(depotShopForm.getTownId())){
            depotShopForm.setTownName(townClient.findOne(depotShopForm.getTownId()).getTownName());
        }
        if (depotShopForm.isCreate()) {
            Depot depot = depotRepository.findOne(depotShopForm.getDepotId());
            depotShopForm.setId(depot.getDepotShopId());
        }
        depotShop = depotShopRepository.findOne(depotShopForm.getId());
        ReflectionUtil.copyProperties(depotShopForm, depotShop);
        depotShopRepository.save(depotShop);
        return depotShop;
    }

    public Depot saveDepot(DepotForm depotForm) {
        Depot depot;
        depotForm.setNamePinyin(StringUtils.getFirstSpell(depotForm.getName()));
        if (depotForm.isCreate()) {
            depot = BeanUtil.map(depotForm, Depot.class);
            depotManager.save(depot);
            DepotShop depotShop = new DepotShop();
            depotShop.setDepotId(depot.getId());
            depotShopRepository.save(depotShop);
            depot.setDepotShopId(depotShop.getId());
            depotRepository.save(depot);
        } else {
            depot = depotRepository.findOne(depotForm.getId());
            ReflectionUtil.copyProperties(depotForm, depot);
            depotManager.save(depot);
        }
        return depot;
    }


    public void logicDelete(String id) {
        depotShopRepository.logicDelete(id);
    }

    public List<DepotReportDto> setReportData(ReportQuery reportQuery) {
        reportQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
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

    public DepotReportDetailDto getReportDataDetail(ReportQuery reportQuery) {
        DepotReportDetailDto depotReportDetail = new DepotReportDetailDto();
        List<DepotReportDto> depotReportList = getProductImeReportList(reportQuery);
        depotReportDetail.setDepotReportList(depotReportList);
        Map<String, Integer> map = Maps.newHashMap();
        for (DepotReportDto depotReportDto : depotReportList) {
            if (!map.containsKey(depotReportDto.getProductName())) {
                map.put(depotReportDto.getProductName(), 1);
            } else {
                map.put(depotReportDto.getProductName(), map.get(depotReportDto.getProductName()) + 1);
            }
        }
        depotReportDetail.setProductQtyMap(map);
        cacheUtils.initCacheInput(depotReportList);
        return depotReportDetail;
    }

    public List<DepotReportDto> getProductImeReportList(ReportQuery reportQuery) {
        List<DepotReportDto> depotReportList = Lists.newArrayList();
        if (OutTypeEnum.电子保卡.name().equals(reportQuery.getOutType())) {
            if ("销售报表".equals(reportQuery.getType())) {
                depotReportList = depotShopRepository.findBaokaSaleReport(reportQuery);
            } else if ("库存报表".equals(reportQuery.getType())) {
                depotReportList = depotShopRepository.findBaokaStoreReport(reportQuery);
            }
        } else {
            if ("销售报表".equals(reportQuery.getType())) {
                depotReportList = depotShopRepository.findSaleReport(reportQuery);
            } else if ("库存报表".equals(reportQuery.getType())) {
                depotReportList = depotShopRepository.findStoreReport(reportQuery);
            }
        }
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
}
