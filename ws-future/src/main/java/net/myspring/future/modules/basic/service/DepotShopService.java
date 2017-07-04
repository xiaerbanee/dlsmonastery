package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.common.enums.OutTypeEnum;
import net.myspring.future.common.enums.ShopDepositTypeEnum;
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
import net.myspring.future.modules.layout.domain.ShopDeposit;
import net.myspring.future.modules.layout.dto.ShopDepositDto;
import net.myspring.future.modules.layout.repository.ShopDepositRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by liuj on 2017/5/12.
 */
@Service
@Transactional(readOnly = true)
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
    @Autowired
    private ShopDepositRepository shopDepositRepository;

    public Page<DepotShopDto> findPage(Pageable pageable, DepotShopQuery depotShopQuery) {
        depotShopQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        if (StringUtils.isNotBlank(depotShopQuery.getOfficeId())) {
            depotShopQuery.getOfficeIdList().addAll(officeClient.getChildOfficeIds(depotShopQuery.getOfficeId()));
        }
        Page<DepotShopDto> page = depotShopRepository.findPage(pageable, depotShopQuery);
        Map<String, ShopDepositDto> scbzjMap = Maps.newHashMap();
        Map<String, ShopDepositDto> xxbzjMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(page.getContent())) {
            List<ShopDeposit> scbzjList = shopDepositRepository.findByTypeAndShopIdIn(ShopDepositTypeEnum.市场保证金.name(), CollectionUtil.extractToList(page.getContent(), "id"));
            List<ShopDeposit> xxbzjList = shopDepositRepository.findByTypeAndShopIdIn(ShopDepositTypeEnum.形象保证金.name(), CollectionUtil.extractToList(page.getContent(), "id"));
            xxbzjMap = CollectionUtil.extractToMap(BeanUtil.map(xxbzjList,ShopDepositDto.class), "shopId");
            scbzjMap = CollectionUtil.extractToMap(BeanUtil.map(scbzjList,ShopDepositDto.class), "shopId");
        }
        for (DepotShopDto depotShopDto : page.getContent()) {
            depotShopDto.getDepositMap().put("xxbzj", xxbzjMap.get(depotShopDto.getId()) == null ? BigDecimal.ZERO : xxbzjMap.get(depotShopDto.getId()).getAmount());
            depotShopDto.getDepositMap().put("scbzj", scbzjMap.get(depotShopDto.getId()) == null ? BigDecimal.ZERO : scbzjMap.get(depotShopDto.getId()).getAmount());
        }
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

    @Transactional
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

    @Transactional
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

    @Transactional
    public void logicDelete(String id) {
        depotShopRepository.logicDelete(id);
    }

    public SimpleExcelBook findSimpleExcelSheet(DepotShopQuery depotShopQuery)  {
        Workbook workbook = new SXSSFWorkbook(10000);
        List<DepotShopDto> depotShopDtoList = depotShopRepository.findFilter(depotShopQuery);
        cacheUtils.initCacheInput(depotShopDtoList);
        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"name","门店名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"clientName","金蝶名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"areaName","办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"officeName","机构"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"areaType","地区属性"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"pricesystemName","价格体系"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"chainName","连锁体系"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"contator","联系人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"mobilePhone","手机号码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"salePointType","门店属性"));
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("门店信息",depotShopDtoList,simpleExcelColumnList);
        ExcelUtils.doWrite(workbook,simpleExcelSheet);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"门店信息"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        return simpleExcelBook;
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
