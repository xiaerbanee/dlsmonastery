package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.common.dto.NameValueDto;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.DepotUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.basic.client.*;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.mapper.*;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.layout.domain.ShopAttribute;
import net.myspring.future.modules.layout.domain.ShopDeposit;
import net.myspring.future.modules.layout.mapper.ShopAttributeMapper;
import net.myspring.future.modules.layout.mapper.ShopDepositMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
}