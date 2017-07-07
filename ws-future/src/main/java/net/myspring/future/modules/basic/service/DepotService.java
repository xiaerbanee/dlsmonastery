package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.dto.CustomerDto;
import net.myspring.future.modules.basic.dto.DepotAccountDto;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.web.query.DepotAccountQuery;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class DepotService {
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private CloudClient cloudClient;


    public List<DepotDto> findShopList(DepotQuery depotQuery) {
        List<Depot> depotList = depotRepository.findByAccountId(RequestUtils.getAccountId());
        if(CollectionUtil.isNotEmpty(depotList)) {
            depotQuery.setDepotIdList(CollectionUtil.extractToList(depotList,"id"));
        }
        return depotRepository.findShopList(depotQuery);
    }

    public List<String> filterDepotIds(){
        return depotManager.filterDepotIds(RequestUtils.getAccountId());
    }

    public List<DepotDto> findStoreList(DepotQuery depotQuery) {
        List<Depot> depotList = depotRepository.findByAccountId(RequestUtils.getAccountId());
        if(CollectionUtil.isNotEmpty(depotList)) {
            depotQuery.setDepotIdList(CollectionUtil.extractToList(depotList,"id"));
        }
        return depotRepository.findStoreList(depotQuery);
    }

    public List<DepotDto> findByIds(List<String> ids){
        List<Depot> depotList=depotRepository.findByEnabledIsTrueAndIdIn(ids);
        List<DepotDto> depotDtoList= BeanUtil.map(depotList,DepotDto.class);
        cacheUtils.initCacheInput(depotDtoList);
        return depotDtoList;
    }

    public DepotDto findDto(String id) {
        DepotDto depotDto = depotRepository.findDto(id);
        cacheUtils.initCacheInput(depotDto);
        return depotDto;
    }

    public Page<DepotAccountDto> findDepotAccountList(Pageable pageable, DepotAccountQuery depotAccountQuery) {

        if(depotAccountQuery.getDutyDateRange() == null || depotAccountQuery.getDutyDateStart()==null || LocalDate.now().minusDays(70).isAfter(depotAccountQuery.getDutyDateStart()) ){
            throw new ServiceException("查询条件请选择为70天以内");
        }

        Page<DepotAccountDto> page = depotRepository.findDepotAccountList(pageable, depotAccountQuery);
        cacheUtils.initCacheInput(page.getContent());

        CustomerReceiveQuery customerReceiveQuery = new CustomerReceiveQuery();
        customerReceiveQuery.setDateStart(depotAccountQuery.getDutyDateStart());
        customerReceiveQuery.setDateEnd(depotAccountQuery.getDutyDateEnd());
        customerReceiveQuery.setCustomerIdList(CollectionUtil.extractToList(page.getContent(), "clientOutId"));
        List<CustomerReceiveDto> customerReceiveDtoList = cloudClient.getCustomerReceiveList(customerReceiveQuery);
        Map<String, CustomerReceiveDto> customerReceiveDtoMap = CollectionUtil.extractToMap(customerReceiveDtoList, "customerId");

        for(DepotAccountDto depotAccountDto : page.getContent()){
            CustomerReceiveDto customerReceiveDto = customerReceiveDtoMap.get(depotAccountDto.getClientOutId());
            if (customerReceiveDto != null){
                depotAccountDto.setQcys(customerReceiveDto.getBeginShouldGet());
                depotAccountDto.setQmys(customerReceiveDto.getEndShouldGet());
            }else {
                depotAccountDto.setQcys(BigDecimal.ZERO);
                depotAccountDto.setQmys(BigDecimal.ZERO);
            }

        }

        return page;
    }

    public SimpleExcelBook depotAccountExportDetail(DepotAccountQuery depotAccountQuery) {

        List<SimpleExcelSheet> sheets = new ArrayList<>();
        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "officeName", "所属机构"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "name", "客户名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qcys", "期初应收"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qmys", "期末应收"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "scbzj", "市场保证金"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "xxbzj", "形象押金"));

        List<DepotAccountDto> depotAccountList = findDepotAccountList(new PageRequest(0,10000),  depotAccountQuery).getContent();
        SimpleExcelSheet sheet = new SimpleExcelSheet("应收报表", depotAccountList, simpleExcelColumnList);
        ExcelUtils.doWrite(workbook, sheet);
        sheets.add(sheet);

        Set<String> clientOutIds = Sets.newHashSet();
        for(DepotAccountDto depotAccountDto : depotAccountList){

            if (StringUtils.isBlank(depotAccountDto.getClientOutId()) || clientOutIds.contains(depotAccountDto.getClientOutId())) {
                continue;
            }
            clientOutIds.add(depotAccountDto.getClientOutId());

            List<SimpleExcelColumn> depotAccountColumnList = Lists.newArrayList();

            depotAccountColumnList.add(new SimpleExcelColumn(workbook, "billType", "业务类型"));
            depotAccountColumnList.add(new SimpleExcelColumn(workbook, "billNo", "单据编号"));
            depotAccountColumnList.add(new SimpleExcelColumn(workbook, "date", "记账日期"));
            depotAccountColumnList.add(new SimpleExcelColumn(workbook, "materialName", "名称"));
            depotAccountColumnList.add(new SimpleExcelColumn(workbook, "qty", "数量"));
            depotAccountColumnList.add(new SimpleExcelColumn(workbook, "price", "单价"));
            depotAccountColumnList.add(new SimpleExcelColumn(workbook, "totalAmount", "金额"));
            depotAccountColumnList.add(new SimpleExcelColumn(workbook, "receiveAmount", "预收"));
            depotAccountColumnList.add(new SimpleExcelColumn(workbook, "shouldGet", "应收"));
            depotAccountColumnList.add(new SimpleExcelColumn(workbook, "endShouldGet", "期末"));
            depotAccountColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));

            //TODO 获取明细
            List<String> customerAccounts = new ArrayList<>();// k3cloudService.findCustomerAccount(AccountUtils.getCompany().getOutDbname(),depot.getOutId(), depot.getName(), dateStart, dateEnd);

            SimpleExcelSheet tmpSheet = new SimpleExcelSheet(depotAccountDto.getName(), customerAccounts, depotAccountColumnList);
            ExcelUtils.doWrite(workbook, tmpSheet);
            sheets.add(tmpSheet);
        }
        return  new SimpleExcelBook(workbook,"客户应收" + LocalDateUtils.format(depotAccountQuery.getDutyDateStart(), "yyyyMMdd") + "~" + LocalDateUtils.format(depotAccountQuery.getDutyDateEnd(), "yyyyMMdd")+".xlsx", sheets);
    }

    public SimpleExcelBook depotAccountExportConfirmation(DepotAccountQuery depotAccountQuery) {
        //TODO 写确认书需要增强excel的打印能力

        return null;
    }

    public SimpleExcelBook depotAccountExportAllDepots(DepotAccountQuery depotAccountQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "areaName", "所属办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "officeName", "所属机构"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "name", "客户名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qcys", "期初应收"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qmys", "期末应收"));

        List<DepotAccountDto> depotAccountList = findDepotAccountList(new PageRequest(0,10000),  depotAccountQuery).getContent();
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("应收列表", depotAccountList, simpleExcelColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);
        return  new SimpleExcelBook(workbook,"应收列表"+LocalDateUtils.format(LocalDate.now()) +".xlsx" ,simpleExcelSheet);
    }

    @Transactional
    public void scheduleSynArea(){
        List<Depot> depotList=depotRepository.findAll();
        List<DepotDto> depotDtoList=BeanUtil.map(depotList,DepotDto.class);
        cacheUtils.initCacheInput(depotDtoList);
        Map<String,DepotDto> depotDtoMap=CollectionUtil.extractToMap(depotDtoList,"id");
        for(Depot depot:depotList){
            depot.setAreaId(depotDtoMap.get(depot.getId()).getAreaId());
            depotRepository.save(depot);
        }
    }

    @Transactional
    public void synArea(DepotQuery depotQuery){
      depotQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
      List<Depot> depotList=depotRepository.findByFilter(depotQuery);
        List<DepotDto> depotDtoList=BeanUtil.map(depotList,DepotDto.class);
        cacheUtils.initCacheInput(depotDtoList);
        Map<String,DepotDto> depotDtoMap=CollectionUtil.extractToMap(depotDtoList,"id");
        for(Depot depot:depotList){
            depot.setAreaId(depotDtoMap.get(depot.getId()).getAreaId());
            depotRepository.save(depot);
        }
    }

    public List<DepotDto> findAdStoreDtoList() {
        String outGroupId = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.STORE_AD_GROUP_IDS.name()).getValue();
        List<DepotDto> adStoreDtoList = depotRepository.findAdStoreDtoList(outGroupId);
        cacheUtils.initCacheInput(adStoreDtoList);
        return adStoreDtoList;

    }

    public Depot findByName(String name){
        return  depotRepository.findByName(name);
    }

    public BdDepartment getDefaultDepartment(String depotId) {
        ClientDto clientDto = clientRepository.findByDepotId(depotId);
        if(clientDto == null || StringUtils.isBlank(clientDto.getOutId())){
            return null;
        }
        BdDepartment bdDepartment = cloudClient.findDepartmentByOutId(clientDto.getOutId());
        if(bdDepartment == null){
            return null;
        }
        return bdDepartment;

    }

    public Map<String,Long> getRecentMonthSaleAmount(String depotId, int monthQty) {
        LocalDate now = LocalDate.now();
        Map<String,Long>  recentMonthSaleAmountMap= Maps.newLinkedHashMap();
        for(int i=monthQty;i>0;i--){

            LocalDateTime dateStart=now.minusMonths(i).atStartOfDay();
            LocalDateTime dateEnd=now.minusMonths(i-1).atStartOfDay();

            Long saleQty=productImeRepository.countByEnabledIsTrueAndDepotIdAndRetailDateBetween(depotId, dateStart, dateEnd);
            String month = LocalDateTimeUtils.format(dateStart, "yyyy-MM");
            recentMonthSaleAmountMap.put(month, saleQty);
        }
        return recentMonthSaleAmountMap;
    }

    public Map<String, Integer> getCloudQtyMap(String storeId) {
        return depotManager.getCloudQtyMap(storeId);
    }

    public List<CustomerDto>  findOppoCustomers(){
        List<CustomerDto> customerDtos=depotRepository.findOppoCustomers();
        return customerDtos;
    }

    public List<DepotDto> findByClientId(String clientId){
        List<DepotDto> depotDtos = Lists.newArrayList();
        if(clientId == null){
            return depotDtos;
        }
        depotDtos = BeanUtil.map(depotRepository.findByClientId(clientId),DepotDto.class);
        return depotDtos;
    }

}
