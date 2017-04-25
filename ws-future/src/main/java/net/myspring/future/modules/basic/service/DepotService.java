package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.future.common.dto.NameValueDto;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.service.ServiceException;
import net.myspring.future.common.utils.DepotUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.IdUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.basic.client.*;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.mapper.*;
import net.myspring.future.modules.basic.web.Query.DepotQuery;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.crm.model.DepotInventoryModel;
import net.myspring.future.modules.crm.model.ReceivableReportForDetail;
import net.myspring.future.modules.crm.model.ReceivableReportForSummary;
import net.myspring.future.modules.layout.domain.ShopAttribute;
import net.myspring.future.modules.layout.domain.ShopDeposit;
import net.myspring.future.modules.layout.mapper.ShopAttributeMapper;
import net.myspring.future.modules.layout.mapper.ShopDepositMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class DepotService {
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private PricesystemMapper pricesystemMapper;
    @Autowired
    private ChainMapper chainMapper;
    @Autowired
    private AdPricesystemMapper adPricesystemMapper;
    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;
    @Autowired
    private ShopDepositMapper shopDepositMapper;
    @Autowired
    private ShopAttributeMapper shopAttributeMapper;
    @Autowired
    private DictMapClient dictMapClient;
    @Autowired
    private DictEnumClient dictEnumClient;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private DistrictClient districtClient;
    @Autowired
    private CompanyConfigClient companyConfigClient;
    @Autowired
    private CloudClient cloudClient;


    public DepotDto findOne(String id) {
        Depot depot = depotMapper.findOne(id);
        DepotDto depotDto = BeanUtil.map(depot,DepotDto.class);
        return depotDto;
    }

    public Page<DepotDto> findPage(Pageable pageable, DepotQuery depotQuery) {
        Page<DepotDto> page = depotMapper.findPage(pageable, depotQuery);
        Map<String, ShopDeposit> scbzjMap = Maps.newHashMap();
        Map<String, ShopDeposit> xxbzjMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(page.getContent())) {
            List<ShopDeposit> scbzjList = shopDepositMapper.findByTypeAndShopIds(ShopDepositTypeEnum.市场保证金.name(), CollectionUtil.extractToList(page.getContent(), "id"));
            List<ShopDeposit> xxbzjList = shopDepositMapper.findByTypeAndShopIds(ShopDepositTypeEnum.形象保证金.name(), CollectionUtil.extractToList(page.getContent(), "id"));
            xxbzjMap = CollectionUtil.extractToMap(xxbzjList, "shopId");
            scbzjMap = CollectionUtil.extractToMap(scbzjList, "shopId");
        }
        Map<Integer, String> map = DepotTypeEnum.getMap();
        for (DepotDto depotDto : page.getContent()) {
            depotDto.getDepositMap().put("xxbzj", xxbzjMap.get(depotDto.getId()) == null ? BigDecimal.ZERO : xxbzjMap.get(depotDto.getId()).getAmount());
            depotDto.getDepositMap().put("scbzj", scbzjMap.get(depotDto.getId()) == null ? BigDecimal.ZERO : scbzjMap.get(depotDto.getId()).getAmount());
            if (depotDto != null && depotDto.getType() != null) {
                depotDto.setTypeLabel(map.get(depotDto.getType()));
            }
        }
        return page;
    }

    public DepotQuery getQueryProperty(DepotQuery depotQuery) {
        depotQuery.setTypeMap(DepotTypeEnum.getMap());
        depotQuery.setAreaList(officeClient.findAreaByType("100"));
        depotQuery.setAreaTypeList(dictMapClient.findDictMapByCategory(DictMapCategoryEnum.门店_地区属性.name()));
        depotQuery.setSpecialityStoreTypeList(dictMapClient.findDictMapByCategory(DictMapCategoryEnum.门店_体验店类型.name()));
        depotQuery.setPricesystemList(pricesystemMapper.findAll());
        depotQuery.setChainList(chainMapper.findAll());
        depotQuery.setAdPricesystemList(adPricesystemMapper.findAll());
        depotQuery.setExpressCompanyList(expressCompanyMapper.findAll());
        depotQuery.setBools(BoolEnum.getMap());
        return depotQuery;
    }

    public DepotForm getFormProperty(DepotForm depotForm) {
        depotForm.setDistrictDtoList(districtClient.findByParentId("100000"));
        depotForm.setChannelTypeList(dictMapClient.findDictMapByCategory(DictMapCategoryEnum.门店_渠道类型.name()));
        depotForm.setAreaTypeList(dictMapClient.findDictMapByCategory(DictMapCategoryEnum.门店_地区属性.name()));
        depotForm.setCarrierTypeList(dictMapClient.findDictMapByCategory(DictMapCategoryEnum.门店_运营商属性.name()));
        depotForm.getCarrierTypeList().add(new NameValueDto(" "," "));
        depotForm.setChainTypeList(dictMapClient.findDictMapByCategory(DictMapCategoryEnum.门店_连锁属性.name()));
        depotForm.getChainTypeList().add(new NameValueDto(" "," "));
        depotForm.setTurnoverTypeList(dictMapClient.findDictMapByCategory(DictMapCategoryEnum.门店_营业额分类.name()));
        depotForm.setSalePointTypeList(dictMapClient.findDictMapByCategory(DictMapCategoryEnum.门店_售点类型.name()));
        depotForm.setShopAreaTypeList(dictMapClient.findDictMapByCategory(DictMapCategoryEnum.门店_店面尺寸.name()));
        depotForm.setBusinessCenterTypeList(dictMapClient.findDictMapByCategory(DictMapCategoryEnum.门店_核心商圈.name()));
        depotForm.setShopMonthTotalList(dictEnumClient.findDictEnumByCategory(DictEnumCategoryEnum.SHOP_MONTH_TOTAL.getValue()));
        depotForm.setSpecialityStoreTypeList(dictMapClient.findDictMapByCategory(DictMapCategoryEnum.门店_体验店类型.name()));
        depotForm.setBoolMap(BoolEnum.getMap());
        depotForm.setPricesystemList( pricesystemMapper.findAll());
        depotForm.setChainList(chainMapper.findAll());
        return depotForm;
    }

    public List<DepotDto> findByOffice(String officeId) {
        List<Depot> depotList = depotMapper.findByOfficeId(officeId);
        List<DepotDto> depotDtoList = BeanUtil.map(depotList,DepotDto.class);
        return depotDtoList;
    }

    public List<DepotDto> findByIds(List<String> ids) {
        List<DepotDto> list = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(ids)) {
            List<Depot> depotList = depotMapper.findByIds(ids);
            list = BeanUtil.map(depotList,DepotDto.class);
        }
        return list;
    }

    public List<DepotDto> findByTypes(List<Integer> types) {
        List<Depot> depotList = depotMapper.findByTypes(types);
        List<DepotDto> depotDtoList = BeanUtil.map(depotList, DepotDto.class);
        return depotDtoList;
    }

    public List<DepotDto> findByOutGroupId(String outGroupId) {
        List<Depot> depotList = depotMapper.findByOutGroupId(outGroupId);
        List<DepotDto> depotDtoList = BeanUtil.map(depotList, DepotDto.class);
        return depotDtoList;
    }

    public List<DepotDto> findProxyShop(DepotQuery depotQuery){
        List<String> officeIds = officeClient.getOfficeIdsByAccountId(SecurityUtils.getAccountId());
        if(CollectionUtil.isNotEmpty(officeIds)){
            Set<String> areaIds= Sets.newHashSet();
            for(String officeId:officeIds){
                String areaId = officeClient.getOfficeIdByOfficeType(officeId, Const.OFFICE_TYPE_AREA);
                if(StringUtils.isNotBlank(areaId)){
                    areaIds.add(areaId);
                }
            }
            List<String> officeAndAreaIds = CollectionUtil.extractToList(officeClient.findByAreaIds(Lists.newArrayList(areaIds)),"id");
            depotQuery.setOfficeIdList(officeAndAreaIds);
        }
        List<Depot> depotList = depotMapper.findByFilter(depotQuery);
        return BeanUtil.map(depotList,DepotDto.class);
    }

    public  List<DepotDto> findAdShopBsc(String name){
        DepotQuery depotQuery = new DepotQuery();
        List<String> officeIds = officeClient.getOfficeIdsByAccountId(SecurityUtils.getAccountId());
        if(CollectionUtil.isNotEmpty(officeIds)){
            Set<String> areaIds = Sets.newHashSet();
            for(String officeId:officeIds){
                String areaId = officeClient.getOfficeIdByOfficeType(officeId, Const.OFFICE_TYPE_AREA);
                if(StringUtils.isNotBlank(areaId)){
                    areaIds.add(areaId);
                }
            }
            List<String> officeAndAreaIds = CollectionUtil.extractToList(officeClient.findByAreaIds(Lists.newArrayList(areaIds)),"id");
            depotQuery.setOfficeIdList(officeAndAreaIds);
        }
        depotQuery.setDepotName(name);
        depotQuery.setAdShopBsc(true);
        depotQuery.setTypeList(DepotUtils.getTypeValueByCategory(DepotCategoryEnum.AD_SHOP.name()));
        Set<DepotDto> depotSet = Sets.newHashSet();
        depotSet.addAll(BeanUtil.map(depotMapper.findByFilter(depotQuery),DepotDto.class));
        depotQuery.setAdShopBsc(false);
        depotSet.addAll(BeanUtil.map(depotMapper.findByFilter(depotQuery),DepotDto.class));
        List<DepotDto> depotDtoList = Lists.newArrayList(depotSet);
        return depotDtoList;
    }

    public List<DepotDto> setInventory(List<DepotDto> depotDtoList, DepotQuery depotQuery) {
        List<DepotInventoryModel> depotInventoryModels = depotMapper.findInventoryData(depotQuery);
        Map<String, List<DepotInventoryModel>> depotInventoryModelMap = Maps.newHashMap();
        for (DepotInventoryModel depotInventoryModel : depotInventoryModels) {
            if (!depotInventoryModelMap.containsKey(depotInventoryModel.getShopId())) {
                depotInventoryModelMap.put(depotInventoryModel.getShopId(), Lists.newArrayList());
            }
            depotInventoryModelMap.get(depotInventoryModel.getShopId()).add(depotInventoryModel);
        }
        for (DepotDto depotDto : depotDtoList) {
            DepotInventoryModel depotInventoryModel = new DepotInventoryModel();
            List<DepotInventoryModel> depotInventoryModelList = depotInventoryModelMap.get(depotDto.getId());
            if (CollectionUtil.isNotEmpty(depotInventoryModelList)) {
                for (DepotInventoryModel depotInventoryModelData : depotInventoryModelList) {
                    switch (depotInventoryModelData.getType()) {
                        case "核销库存":
                            depotInventoryModel.addSaleStockQty(depotInventoryModelData.getQty());
                            break;
                        case "电子保卡库存":
                            depotInventoryModel.addBaoKaStockQty(depotInventoryModelData.getQty());
                            break;
                        case "核销销量":
                            depotInventoryModel.addSaleSalesQty(depotInventoryModelData.getQty());
                            break;
                        case "电子保卡销量":
                            depotInventoryModel.addBaoKaSalesQty(depotInventoryModelData.getQty());
                            break;
                        default:
                            break;
                    }
                }
            }
            depotDto.setDepotInventoryModel(depotInventoryModel);
        }
        return depotDtoList;
    }

    public DepotDto setInventoryDetail(DepotDto depotDto, DepotQuery depotQuery) {
        List<DepotInventoryModel> depotInventoryModels = depotMapper.findInventoryData(depotQuery);
        Map<String, List<DepotInventoryModel>> depotInventoryModelMap = Maps.newHashMap();
        for (DepotInventoryModel depotInventoryModelData : depotInventoryModels) {
            if (!depotInventoryModelMap.containsKey(depotInventoryModelData.getProductTypeName())) {
                depotInventoryModelMap.put(depotInventoryModelData.getProductTypeName(), Lists.newArrayList());
            }
            depotInventoryModelMap.get(depotInventoryModelData.getProductTypeName()).add(depotInventoryModelData);
        }
        List<DepotInventoryModel> depotInventoryModelList = Lists.newArrayList();
        for (Map.Entry<String, List<DepotInventoryModel>> entry : depotInventoryModelMap.entrySet()) {
            DepotInventoryModel depotInventoryModel = new DepotInventoryModel();
            depotInventoryModel.setProductTypeName(entry.getKey());
            for (DepotInventoryModel depotInventoryModelData : entry.getValue()) {
                switch (depotInventoryModelData.getType()) {
                    case "核销库存":
                        depotInventoryModel.addSaleStockQty(depotInventoryModelData.getQty());
                        break;
                    case "电子保卡库存":
                        depotInventoryModel.addBaoKaStockQty(depotInventoryModelData.getQty());
                        break;
                    case "核销销量":
                        depotInventoryModel.addSaleSalesQty(depotInventoryModelData.getQty());
                        break;
                    case "电子保卡销量":
                        depotInventoryModel.addBaoKaSalesQty(depotInventoryModelData.getQty());
                        break;
                    default:
                        break;
                }
            }
            depotInventoryModelList.add(depotInventoryModel);
        }
        depotDto.setDepotInventoryModelList(depotInventoryModelList);
        return depotDto;
    }

    public List<Depot> findStores() {
        List<Integer> depotTypes = DepotUtils.getTypeValueByCategory(DepotCategoryEnum.STORE.name());
        return depotMapper.findByTypes(depotTypes);
    }

    public List<Depot> findByFilter(DepotQuery depotQuery) {
        List<Depot> depotList = depotMapper.findByFilter(depotQuery);
        return depotList;
    }

    public Depot save(DepotForm depotForm) {
        //修改门店属性
        Depot depot;
        if (CollectionUtil.isNotEmpty(depotForm.getShopAttributeList())) {
            Map<String, ShopAttribute> clientMap = CollectionUtil.extractToMap(depotForm.getShopAttributeList(), "typeName");
            List<ShopAttribute> shopAttributeList = shopAttributeMapper.findByShopId(depotForm.getId());
            Map<String, ShopAttribute> serverMap = CollectionUtil.extractToMap(shopAttributeList, "typeName");
            for (ShopAttribute shopAttribute : shopAttributeList) {
                if (clientMap.containsKey(shopAttribute.getTypeName())) {
                    shopAttribute.setTypeValue(clientMap.get(shopAttribute.getTypeName()).getTypeValue());
                    shopAttributeMapper.update(shopAttribute);
                } else {
                    shopAttributeMapper.logicDeleteOne(shopAttribute.getId());
                }
            }
            for (ShopAttribute shopAttribute : depotForm.getShopAttributeList()) {
                if (!serverMap.containsKey(shopAttribute.getTypeName())) {
                    shopAttribute.setShopId(depotForm.getId());
                    shopAttributeMapper.save(shopAttribute);
                }
            }
        }
        depotForm.setType(DepotUtils.getDepotType(depotForm));
        if (depotForm.isCreate()) {
            depot = BeanUtil.map(depotForm,Depot.class);
            depotMapper.save(depot);
        } else {
            depotMapper.updateForm(depotForm);
            depot = depotMapper.findOne(depotForm.getId());
        }
        depotMapper.deleteDepotAccount(depotForm.getId());
        if (CollectionUtil.isNotEmpty(depotForm.getAccountIdList())) {
            depotMapper.saveDepotAccount(depotForm.getId(), depotForm.getAccountIdList());
        }
        return depot;
    }

    @Transactional
    public void synStore(){
        String cloudName = companyConfigClient.findByCode(CompanyConfigCodeEnum.CLOUD_DB_NAME.getCode()).getValue();
        LocalDateTime dateTime = depotMapper.getMaxOutDate(DataOutTypeEnum.大库.name());
        String result = cloudClient.getSynStockData(cloudName, LocalDateTimeUtils.format(dateTime));
        List<Map<String, Object>> dataList = ObjectMapperUtils.readValue(result,List.class);
        if(CollectionUtil.isNotEmpty(dataList)) {
            for (Map<String, Object> map : dataList) {
                List<Depot> stores = depotMapper.findByOutTypeAndOutId(DataOutTypeEnum.大库.name(), map.get("outId").toString());
                Depot store = null;
                if (CollectionUtil.isEmpty(stores)) {
                    Depot local = depotMapper.findByName(map.get("name").toString());
                    if(local==null) {
                        store = new Depot();
                        store.setName(map.get("name").toString());
                        store.setVersion(0);
                        depotMapper.save(store);
                    }else{
                        store = local;
                    }
                } else {
                    Depot local = depotMapper.findAllByNameAndOutId(map.get("name").toString(),map.get("outId").toString());
                    if(local!=null){
                        store = local;
                    }else{
                        store = stores.get(0);
                    }
                }
                if(store !=null) {
                    store.setName(map.get("name").toString());
                    store.setOutType(DataOutTypeEnum.大库.name());
                    store.setOutId(map.get("outId").toString());
                    store.setOutGroupId(map.get("fgroup").toString());
                    store.setOutGroupName(map.get("groupName").toString());
                    store.setOutDate(LocalDateTimeUtils.parse(map.get("modifyDate").toString()));
                    store.setCode(map.get("code").toString());
                    store.setNamePinyin(StringUtils.getFirstSpell(store.getName()));

                    store.setType(DepotUtils.getDepotType(BeanUtil.map(store,DepotForm.class)));
                    depotMapper.update(store);
                }
            }
        }
    }

    @Transactional
    public void synShop(){
        String cloudName = companyConfigClient.findByCode(CompanyConfigCodeEnum.CLOUD_DB_NAME.getCode()).getValue();
        LocalDateTime dateTime=depotMapper.getMaxOutDate(DataOutTypeEnum.门店.name());
        String result = cloudClient.getSynCustomerData(cloudName, LocalDateTimeUtils.format(dateTime));
        List<Map<String, Object>> dataList = ObjectMapperUtils.readValue(result,List.class);
        if(CollectionUtil.isNotEmpty(dataList)) {
            for (Map<String, Object> map : dataList) {
                List<Depot> shops = depotMapper.findByOutTypeAndOutId(DataOutTypeEnum.门店.name(), map.get("outId").toString());
                Depot shop = null;
                if (CollectionUtil.isEmpty(shops)) {
                    Depot local = depotMapper.findByName(map.get("name").toString());
                    if(local==null) {
                        shop = new Depot();
                        shop.setName(map.get("name").toString());
                        shop.setVersion(0);
                        depotMapper.save(shop);
                    } else {
                        shop = local;
                    }
                } else {
                    Depot local = depotMapper.findAllByNameAndOutId(map.get("name").toString(),map.get("outId").toString());
                    if(local !=null){
                        shop=local;
                    }else{
                        shop = shops.get(0);
                    }
                }
                if(shop !=null) {
                    shop.setName(map.get("name").toString());
                    shop.setOutType(DataOutTypeEnum.门店.name());
                    shop.setOutId(map.get("outId").toString());
                    shop.setOutGroupId(map.get("fgroup").toString());
                    shop.setOutGroupName(map.get("groupName").toString());
                    shop.setOutDate(LocalDateTimeUtils.parse(map.get("modifyDate").toString()));
                    shop.setCode(map.get("code").toString());
                    shop.setNamePinyin(StringUtils.getFirstSpell(shop.getName()));
                    shop.setType(DepotUtils.getDepotType(BeanUtil.map(shop,DepotForm.class)));
                    depotMapper.update(shop);
                }
            }
        }
    }

    public List<DepotDto> findAdDepot(String adShopName, String billType) {
        List<DepotDto> depotList;
        DepotQuery depotQuery = new DepotQuery();//FilterUtils.getDepotFilter(AccountUtils.getAccountId());
        depotQuery.setName(adShopName);
        if ("POP".equals(billType)) {
            depotQuery.setAllowAdApply(true);
            depotList = BeanUtil.map(depotMapper.findByFilter(depotQuery),DepotDto.class);
        } else {
            //代理区域所有门店，直营区域只能是财务关联门店
            List<Integer> typeValueByCategory = DepotUtils.getTypeValueByCategory(DepotCategoryEnum.SHOP_PROXY_STORE.name());
            depotQuery.setTypeList(typeValueByCategory);
            depotList =  BeanUtil.map(depotMapper.findByFilter(depotQuery),DepotDto.class);
        }
        Map<Integer, String> map = DepotTypeEnum.getMap();
        for (DepotDto depotDto : depotList) {
            if (depotDto != null && depotDto.getType() != null) {
                depotDto.setTypeLabel(map.get(depotDto.getType()));
            }
        }
        return depotList;
    }

    public Page<DepotDto> findShopAccount(Pageable pageable, DepotQuery depotQuery) {
        Page<DepotDto> page = depotMapper.findDepotAccountPage(pageable, depotQuery);
        Map<String, ShopDeposit> scbzjMap = Maps.newHashMap();
        Map<String, ShopDeposit> xxbzjMap = Maps.newHashMap();
        Map<String, ShopDeposit> ysjyjMap = Maps.newHashMap();
        Map<String, ReceivableReportForSummary> dataStartMap = Maps.newHashMap();
        Map<String, ReceivableReportForSummary> dataEndMap = Maps.newHashMap();
        List<ReceivableReportForSummary> dataStartList = Lists.newArrayList();
        List<ReceivableReportForSummary> dataEndList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(page.getContent())) {
            String cloudName = companyConfigClient.findByCode(CompanyConfigCodeEnum.CLOUD_DB_NAME.getCode()).getValue();
            List<String> outIds = CollectionUtil.extractToList(page.getContent(), "outId");
            String dateStartJson = cloudClient.findShouldGet(cloudName, outIds, LocalDateUtils.format(depotQuery.getDutyDateStart()));
            String dateEndJson = cloudClient.findShouldGet(cloudName, outIds, LocalDateUtils.format(depotQuery.getDutyDateEnd().plusDays(1)));
            try{
                dataStartList = ObjectMapperUtils.getObjectMapper().readValue(dateStartJson, ObjectMapperUtils.getObjectMapper().getTypeFactory().constructParametricType(ArrayList.class, ReceivableReportForSummary.class));
                dataEndList = ObjectMapperUtils.getObjectMapper().readValue(dateEndJson, ObjectMapperUtils.getObjectMapper().getTypeFactory().constructParametricType(ArrayList.class, ReceivableReportForSummary.class));
                dataStartMap = CollectionUtil.extractToMap(dataStartList, "customerId");
                dataEndMap = CollectionUtil.extractToMap(dataEndList, "customerId");
            }catch (IOException e){
                throw  new ServiceException("金蝶返回数据解析失败");
            }
            List<ShopDeposit> scbzjList = shopDepositMapper.findByTypeAndShopIds(ShopDepositTypeEnum.市场保证金.name(), CollectionUtil.extractToList(page.getContent(), "id"));
            List<ShopDeposit> xxbzjList = shopDepositMapper.findByTypeAndShopIds(ShopDepositTypeEnum.形象保证金.name(), CollectionUtil.extractToList(page.getContent(), "id"));
            List<ShopDeposit> ysjyjxxbzjList = shopDepositMapper.findByTypeAndShopIds(ShopDepositTypeEnum.演示机押金.name(), CollectionUtil.extractToList(page.getContent(), "id"));
            xxbzjMap = CollectionUtil.extractToMap(xxbzjList, "shopId");
            scbzjMap = CollectionUtil.extractToMap(scbzjList, "shopId");
            ysjyjMap=CollectionUtil.extractToMap(ysjyjxxbzjList,"shopId");
        }
        for (DepotDto depotDto : page.getContent()) {
            depotDto.getDepositMap().put("qcys", dataStartMap.get(depotDto.getOutId())== null ? BigDecimal.ZERO : dataStartMap.get(depotDto.getOutId()).getBeginAmount());
            depotDto.getDepositMap().put("qmys", dataEndMap.get(depotDto.getOutId()) == null ? BigDecimal.ZERO : dataEndMap.get(depotDto.getOutId()).getBeginAmount());
            depotDto.getDepositMap().put("xxbzj", xxbzjMap.get(depotDto.getId()) == null ? BigDecimal.ZERO : xxbzjMap.get(depotDto.getId()).getAmount());
            depotDto.getDepositMap().put("scbzj", scbzjMap.get(depotDto.getId()) == null ? BigDecimal.ZERO : scbzjMap.get(depotDto.getId()).getAmount());
            depotDto.getDepositMap().put("ysjyj", ysjyjMap.get(depotDto.getId()) == null ? BigDecimal.ZERO : ysjyjMap.get(depotDto.getId()).getAmount());
        }
        return page;
    }

    public List<ReceivableReportForDetail> findShopAccountDetail(String customerId, String dateRange) {
        String cloudName = companyConfigClient.findByCode(CompanyConfigCodeEnum.CLOUD_DB_NAME.getCode()).getValue();
        List<ReceivableReportForDetail> receivableReportForDetailList = Lists.newArrayList();
        String json = cloudClient.receivableReportForDetailList(cloudName, customerId, dateRange);
        try{
            receivableReportForDetailList = ObjectMapperUtils.getObjectMapper().readValue(json, ObjectMapperUtils.getObjectMapper().getTypeFactory().constructParametricType(ArrayList.class, ReceivableReportForDetail.class));
        }catch (IOException e){
            throw  new ServiceException("金蝶返回数据解析失败");
        }
        return receivableReportForDetailList;
    }

    public  List<DepotDto> findSimpleExcelSheets(Workbook workbook, DepotQuery depotQuery) {
        List<DepotDto> depotList = depotMapper.findShopAccountExportPage(depotQuery);
        Map<String, ReceivableReportForSummary> dataStartMap = Maps.newHashMap();
        Map<String, ReceivableReportForSummary> dataEndMap = Maps.newHashMap();
        List<ReceivableReportForSummary> dataStartList = Lists.newArrayList();
        List<ReceivableReportForSummary> dataEndList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(depotList)) {
            List<String> outIds = CollectionUtil.extractToList(depotList, "outId");
            List<List<String>> outIdsList= IdUtils.splitList(outIds,400);
            for(int  i=0;i<outIdsList.size();i++) {
                String cloudName = companyConfigClient.findByCode(CompanyConfigCodeEnum.CLOUD_DB_NAME.getCode()).getValue();
                String dateStartJson = cloudClient.findShouldGet(cloudName, outIdsList.get(i), LocalDateUtils.format(depotQuery.getDutyDateStart()));
                String dateEndJson = cloudClient.findShouldGet(cloudName, outIdsList.get(i),  LocalDateUtils.format(depotQuery.getDutyDateEnd().plusDays(1)));
                try{
                    dataStartList = ObjectMapperUtils.getObjectMapper().readValue(dateStartJson, ObjectMapperUtils.getObjectMapper().getTypeFactory().constructParametricType(ArrayList.class, ReceivableReportForSummary.class));
                    dataEndList = ObjectMapperUtils.getObjectMapper().readValue(dateEndJson, ObjectMapperUtils.getObjectMapper().getTypeFactory().constructParametricType(ArrayList.class, ReceivableReportForSummary.class));
                    dataStartMap.putAll(CollectionUtil.extractToMap(dataStartList, "customerId"));
                    dataEndMap.putAll(CollectionUtil.extractToMap(dataEndList, "customerId"));
                }catch (IOException e){
                    throw  new ServiceException("金蝶返回数据解析失败");
                }
            }
        }
        for (DepotDto depotDto : depotList) {
            depotDto.getDepositMap().put("qcys", dataStartMap.get(depotDto.getOutId()) == null ? BigDecimal.ZERO :  dataStartMap.get(depotDto.getOutId()).getBeginAmount());
            depotDto.getDepositMap().put("qmys", dataEndMap.get(depotDto.getOutId()) == null ? BigDecimal.ZERO : dataEndMap.get(depotDto.getOutId()).getBeginAmount());
        }
        return depotList;
    }

    public SimpleExcelBook accountExport(Workbook workbook, DepotQuery depotQuery) {
        String dateRange = depotQuery.getDutyDateStart() + "~" + depotQuery.getDutyDateEnd();
        depotQuery.getDutyDateEnd().plusDays(1);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "name", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "office.name", "机构"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "area.name", "办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "depositMap.qcys", "期初应收"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "depositMap.qmys", "期末应收"));

        List<SimpleExcelColumn> simpleExcelSheetColumnList = Lists.newArrayList();
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "billType", "业务类型"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "billNo", "单据编号"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "date", "记账日期"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "materialName", "名称"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "quantity", "数量"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "price", "单价"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "amount", "金额"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "payableAmount", "预收"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "actualPayAmount", "应收"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "endAmount", "期末"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "note", "备注"));

        List<DepotDto> depotList = findSimpleExcelSheets(workbook,depotQuery);
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("应收报表~所有门店", depotList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"应收报表.xlsx",simpleExcelSheetList);

        SimpleExcelSheet simpleExcelSheetDetail = null;
        for(DepotDto depotDto : depotList){
            List<ReceivableReportForDetail> depotDetail=findShopAccountDetail(depotDto.getOutId(),dateRange);
            simpleExcelSheetDetail = new SimpleExcelSheet(depotDto.getName(),depotDetail,simpleExcelSheetColumnList);
            simpleExcelBook.getSimpleExcelSheets().add(simpleExcelSheetDetail);
        }
        return simpleExcelBook;
    }
}