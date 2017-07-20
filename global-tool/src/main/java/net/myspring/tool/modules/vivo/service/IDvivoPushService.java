package net.myspring.tool.modules.vivo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.client.OfficeClient;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.FutureDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.domain.OfficeEntity;
import net.myspring.tool.common.utils.CacheUtils;
import net.myspring.tool.modules.vivo.domain.*;
import net.myspring.tool.modules.vivo.dto.SCustomerDto;
import net.myspring.tool.modules.vivo.dto.SPlantCustomerStockDetailDto;
import net.myspring.tool.modules.vivo.dto.SPlantCustomerStockDto;
import net.myspring.tool.modules.vivo.dto.VivoCustomerSaleImeiDto;
import net.myspring.tool.modules.vivo.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class IDvivoPushService {

    @Autowired
    private CompanyConfigClient companyConfigClient;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private SZonesM13e00Repository sZonesM13e00Repository;
    @Autowired
    private SCustomersM13e00Repository sCustomersM13e00Repository;
    @Autowired
    private SPlantCustomerStockRepository sPlantCustomerStockRepository;
    @Autowired
    private SPlantCustomerStockDetailRepository sPlantCustomerStockDetailRepository;
    @Autowired
    private VivoPlantProductsRepository vivoPlantProductsRepository;
    @Autowired
    private SPlantStockStoresM13e00Repository sPlantStockStoresM13e00Repository;
    @Autowired
    private SPlantStockSupplyM13e00Repository sPlantStockSupplyM13e00Repository;
    @Autowired
    private SPlantStockDealerM13e00Repository sPlantStockDealerM13e00Repository;
    @Autowired
    private SProductItemStocksM13e00Repository sProductItemStocksM13e00Repository;
    @Autowired
    private SProductItem000M13e00Repository sProductItem000M13e00Repository;
    @Autowired
    private SStoresM13e00Repository sStoresM13e00Repository;
    @Autowired
    private VivoCustomerSaleImeiRepository vivoCustomerSaleImeiRepository;
    @Autowired
    private SPlantEndProductSaleM13e00Repository sPlantEndProductSaleM13e00Repository;
    @Autowired
    private CacheUtils cacheUtils;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @FactoryDataSource
    @Transactional
    public  void pushIDvivoZonesData(){
        List<OfficeEntity> officeEntityList = officeClient.findAll();
        Map<String,List<SZonesM13e00>>  zonesMap=Maps.newHashMap();
        for(OfficeEntity officeEntity:officeEntityList){
            if(StringUtils.isBlank(officeEntity.getAgentCode())){
                continue;
            }
            SZonesM13e00 sZonesM13e00 = new SZonesM13e00();
            sZonesM13e00.setZoneId(getZoneId(officeEntity.getAgentCode(),officeEntity.getId()));
            sZonesM13e00.setZoneName(officeEntity.getName());
            sZonesM13e00.setShortcut(officeEntity.getAgentCode());
            String[] parentIds = officeEntity.getParentIds().split(CharConstant.COMMA);
            sZonesM13e00.setZoneDepth(parentIds.length);
            StringBuilder zonePath = new StringBuilder(CharConstant.VERTICAL_LINE);
            for(String parentId:parentIds){
                zonePath.append(getZoneId(officeEntity.getAgentCode(),parentId)).append(CharConstant.VERTICAL_LINE);
            }
            zonePath.append(getZoneId(officeEntity.getAgentCode(),officeEntity.getId())).append(CharConstant.VERTICAL_LINE);
            sZonesM13e00.setZonePath(zonePath.toString());
            sZonesM13e00.setFatherId(getZoneId(officeEntity.getAgentCode(),officeEntity.getParentId()));
            sZonesM13e00.setSubCount(officeEntity.getChildCount());
            sZonesM13e00.setZoneTypes(CharConstant.EMPTY);
            if(!zonesMap.containsKey(officeEntity.getAgentCode())){
                List<SZonesM13e00> list=Lists.newArrayList();
                zonesMap.put(officeEntity.getAgentCode(),list);
            }
            zonesMap.get(officeEntity.getAgentCode()).add(sZonesM13e00);
        }
        logger.info("开始上抛机构数据"+ LocalDateTime.now());
        sZonesM13e00Repository.deleteIDvivoZones();
        for(String agentCode:zonesMap.keySet()){
            sZonesM13e00Repository.batchSaveIDvivo(agentCode,zonesMap.get(agentCode));
        }
        logger.info("上抛机构数据完成"+LocalDateTime.now());
    }

    @FutureDataSource
    public List<SCustomerDto> getVivoCustomersData(String date){
        List<SCustomerDto> sCustomerDtoList = sCustomersM13e00Repository.findIDvivoCustomers(LocalDateUtils.parse(date));
        cacheUtils.initCacheInput(sCustomerDtoList);
        return sCustomerDtoList;
    }

    @FactoryDataSource
    @Transactional
    public void pushIDVivoSCustomersData(List<SCustomerDto> sCustomerDtos){
        String mainCode = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).split(CharConstant.COMMA)[0].replace("\"","");
        Map<String,List<SCustomersM13e00>> customerMap = Maps.newHashMap();
         for(SCustomerDto futureCustomerDto :sCustomerDtos){
            SCustomersM13e00 sCustomersM13e00 = new SCustomersM13e00();
            String customerId = futureCustomerDto.getCustomerId();
            String agentCode=futureCustomerDto.getAgentCode();
            sCustomersM13e00.setCustomerLevel(futureCustomerDto.getCustomerLevel());
            if(futureCustomerDto.getCustomerLevel() == 1){
                sCustomersM13e00.setCustomerId(StringUtils.getFormatId(customerId,mainCode+"D","00000"));
                sCustomersM13e00.setCustomerName(futureCustomerDto.getAreaName());
            }else {
                sCustomersM13e00.setCustomerId(StringUtils.getFormatId(customerId,mainCode+"C","00000"));
                sCustomersM13e00.setCustomerName(futureCustomerDto.getCustomerName());
                sCustomersM13e00.setCustomerStr4(StringUtils.getFormatId(futureCustomerDto.getCustomerStr4(),mainCode+"D","00000"));
            }
            if("R250082".equals(agentCode)){
                sCustomersM13e00.setCustomerStr4(agentCode + "K0000");
            }
            sCustomersM13e00.setZoneId(getZoneId(agentCode,futureCustomerDto.getZoneId()));
            sCustomersM13e00.setCompanyId(agentCode);
            sCustomersM13e00.setRecordDate(futureCustomerDto.getRecordDate());
            sCustomersM13e00.setCustomerStr1(futureCustomerDto.getCustomerStr1());
            sCustomersM13e00.setCustomerStr10(agentCode);
            if(!customerMap.containsKey(agentCode)){
                List<SCustomersM13e00> list=Lists.newArrayList();
                customerMap.put(agentCode,list);
            }
            customerMap.get(agentCode).add(sCustomersM13e00);
        }
        logger.info("开始上抛客户数据"+LocalDateTime.now());
        sCustomersM13e00Repository.deleteIDvivoCustomers();
        for(String agentCode:customerMap.keySet()){
            List<SCustomersM13e00> sCustomersM13e00List=customerMap.get(agentCode);
            sCustomersM13e00Repository.batchIDvivoSave(sCustomersM13e00List,agentCode);
        }
        logger.info("上抛客户数据完成"+LocalDateTime.now());
    }

    @FutureDataSource
    public List<SPlantCustomerStockDto> getCustomerStockData(String date){
        LocalDate dateStart = LocalDateUtils.parse(date).minusYears(1);
        LocalDate dateEnd = LocalDateUtils.parse(date).plusDays(1);
        List<SPlantCustomerStockDto> sPlantCustomerStockDtoList = sPlantCustomerStockRepository.findIDvivoCustomerStockData(dateStart,dateEnd);
        cacheUtils.initCacheInput(sPlantCustomerStockDtoList);
        return sPlantCustomerStockDtoList;
    }

    @FactoryDataSource
    @Transactional
    public void pushCustomerStockData(List<SPlantCustomerStockDto> sPlantCustomerStockDtoList,Map<String,String> productColorMap,String date){
        String dateStart = LocalDateUtils.format(LocalDateUtils.parse(date));
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(dateStart));

        Map<String,List<SPlantStockSupplyM13e00>> supplyMap=Maps.newHashMap();
        Map<String,List<SPlantStockDealerM13e00>> dealerMap=Maps.newHashMap();
        Map<String,List<SPlantStockStoresM13e00>> storesMap=Maps.newHashMap();

        for (SPlantCustomerStockDto sPlantCustomerStockDto : sPlantCustomerStockDtoList) {
            int customerLevel = sPlantCustomerStockDto.getCustomerLevel();
            String colorId = productColorMap.get(sPlantCustomerStockDto.getProductId());
            String agentCode=sPlantCustomerStockDto.getAgentCode();
            if (StringUtils.isBlank(colorId)){
                continue;
            }
            if (customerLevel == 1) {
                SPlantStockStoresM13e00 sPlantStockStoresM13e00 = new SPlantStockStoresM13e00();
                sPlantStockStoresM13e00.setCompanyId(agentCode);
                sPlantStockStoresM13e00.setStoreId(agentCode+"K0000");
                sPlantStockStoresM13e00.setProductId(colorId);
                sPlantStockStoresM13e00.setSumStock(0);
                sPlantStockStoresM13e00.setUseAbleStock(sPlantCustomerStockDto.getUseAbleStock());
                sPlantStockStoresM13e00.setBad(0);
                sPlantStockStoresM13e00.setCreatedTime(LocalDateTimeUtils.format(LocalDateTime.now()));
                sPlantStockStoresM13e00.setAccountDate(dateStart);
                if(!storesMap.containsKey(agentCode)){
                    List<SPlantStockStoresM13e00> list = Lists.newArrayList();
                    storesMap.put(agentCode,list);
                }
                storesMap.get(agentCode).add(sPlantStockStoresM13e00);
            }
            if (customerLevel == 2) {
                String supplyId = StringUtils.getFormatId(sPlantCustomerStockDto.getCustomerId(),"D","00000");
                SPlantStockSupplyM13e00 sPlantStockSupplyM13e00 = new SPlantStockSupplyM13e00();
                sPlantStockSupplyM13e00.setCompanyId(agentCode);
                sPlantStockSupplyM13e00.setSupplyId(supplyId);
                sPlantStockSupplyM13e00.setProductId(colorId);
                sPlantStockSupplyM13e00.setCreatedTime(LocalDateTimeUtils.format(LocalDateTime.now()));
                sPlantStockSupplyM13e00.setSumStock(0);
                sPlantStockSupplyM13e00.setUseAbleStock(sPlantCustomerStockDto.getUseAbleStock());
                sPlantStockSupplyM13e00.setBad(0);
                sPlantStockSupplyM13e00.setAccountDate(dateStart);
                if(!supplyMap.containsKey(agentCode)){
                    List<SPlantStockSupplyM13e00> list = Lists.newArrayList();
                    supplyMap.put(agentCode,list);
                }
                supplyMap.get(agentCode).add(sPlantStockSupplyM13e00);
            }
            if (customerLevel == 3) {
                String dealerId = StringUtils.getFormatId(sPlantCustomerStockDto.getCustomerId(),"C","00000");
                SPlantStockDealerM13e00 sPlantStockDealerM13e00 = new SPlantStockDealerM13e00();
                sPlantStockDealerM13e00.setCompanyId(agentCode);
                sPlantStockDealerM13e00.setDealerId(dealerId);
                sPlantStockDealerM13e00.setProductId(colorId);
                sPlantStockDealerM13e00.setSumStock(0);
                sPlantStockDealerM13e00.setUseAbleStock(sPlantCustomerStockDto.getUseAbleStock());
                sPlantStockDealerM13e00.setBad(0);
                sPlantStockDealerM13e00.setCreatedTime(LocalDateTimeUtils.format(LocalDateTime.now()));
                sPlantStockDealerM13e00.setAccountDate(dateStart);
                if(!dealerMap.containsKey(agentCode)){
                    List<SPlantStockDealerM13e00> list = Lists.newArrayList();
                    dealerMap.put(agentCode,list);
                }
                dealerMap.get(agentCode).add(sPlantStockDealerM13e00);
            }
        }

        logger.info("IDVIVO:大库库存数据上抛开始" + LocalDateTime.now());
        for(String agentCode:storesMap.keySet()){
            List<SPlantStockStoresM13e00> sPlantStockStoresM13e00List =storesMap.get(agentCode);
            sPlantStockStoresM13e00Repository.deleteIDvivoByAccountDate(dateStart,dateEnd,agentCode);
            sPlantStockStoresM13e00Repository.batchIDvivoSave(sPlantStockStoresM13e00List,agentCode);
        }
        logger.info("IDVIVO:代理商库存数据上抛开始" + LocalDateTime.now());
        for(String agentCode:supplyMap.keySet()){
            List<SPlantStockSupplyM13e00> sPlantStockSupplyM13e00List =supplyMap.get(agentCode);
            sPlantStockSupplyM13e00Repository.deleteIDvivoByAccountDate(dateStart,dateEnd,agentCode);
            sPlantStockSupplyM13e00Repository.batchIDvivoSave(sPlantStockSupplyM13e00List,agentCode);
        }
        logger.info("IDVIVO:经销商库存数据上抛开始" + LocalDateTime.now());
        for(String agentCode:dealerMap.keySet()){
            List<SPlantStockDealerM13e00> sPlantStockDealerM13e00List =dealerMap.get(agentCode);
            sPlantStockDealerM13e00Repository.deleteIDvivoByAccountDate(dateStart,dateEnd,agentCode);
            sPlantStockDealerM13e00Repository.batchIDvivoSave(sPlantStockDealerM13e00List,agentCode);
        }
    }

    @FutureDataSource
    public List<SPlantCustomerStockDetailDto> getCustomerStockDetailData(String date){
        LocalDate dateStart = LocalDateUtils.parse(date).minusYears(1);
        LocalDate dateEnd = LocalDateUtils.parse(date).plusDays(1);
        List<SPlantCustomerStockDetailDto> sPlantCustomerStockDetailDtoList = sPlantCustomerStockDetailRepository.findIDVivoCustomerStockDetailData(dateStart,dateEnd);
        cacheUtils.initCacheInput(sPlantCustomerStockDetailDtoList);
        return sPlantCustomerStockDetailDtoList;
    }

    @FactoryDataSource
    @Transactional
    public void pushCustomerStockDetailData(List<SPlantCustomerStockDetailDto> sPlantCustomerStockDetailDtoList,Map<String,String> productColorMap,String date){
        LocalDate dateStart = LocalDateUtils.parse(date).minusYears(1);
        LocalDate dateEnd = LocalDateUtils.parse(date).plusDays(1);
        Map<String,List<SProductItemStocksM13e00>> stockMap=Maps.newHashMap();
        Map<String,List<SProductItem000M13e00>> itemMap=Maps.newHashMap();
        for (SPlantCustomerStockDetailDto sPlantCustomerStockDetailDto : sPlantCustomerStockDetailDtoList){
            int customerLevel = sPlantCustomerStockDetailDto.getCustomerLevel();
            String colorId = productColorMap.get(sPlantCustomerStockDetailDto.getProductId());
            String agentCode=sPlantCustomerStockDetailDto.getAgentCode();
            String productNo=sPlantCustomerStockDetailDto.getIme();
            if(StringUtils.isBlank(colorId)){
                continue;
            }
            if (customerLevel == 1){
                SProductItemStocksM13e00 sProductItemStocksM13e00 = new SProductItemStocksM13e00();
                sProductItemStocksM13e00.setCompanyId(agentCode);
                sProductItemStocksM13e00.setProductId(colorId);
                sProductItemStocksM13e00.setProductNo(productNo);
                sProductItemStocksM13e00.setStoreId(agentCode+"K0000");
                sProductItemStocksM13e00.setStatus("在渠道中");
                sProductItemStocksM13e00.setStatusInfo(agentCode+"K0000");
                sProductItemStocksM13e00.setAgentCode(agentCode);
                sProductItemStocksM13e00.setUpdateTime(date);
                if(!stockMap.containsKey(agentCode)){
                    List<SProductItemStocksM13e00> list=Lists.newArrayList();
                    stockMap.put(agentCode,list);
                }
                stockMap.get(agentCode).add(sProductItemStocksM13e00);
            } else {
                SProductItem000M13e00 sProductItem000M13e00 = new SProductItem000M13e00();
                sProductItem000M13e00.setCompanyId(agentCode);
                sProductItem000M13e00.setProductId(colorId);
                sProductItem000M13e00.setProductNo(productNo);
                sProductItem000M13e00.setAgentCode(agentCode);
                if(customerLevel == 2){
                    if("R250082".equals(agentCode)) {
                        sProductItem000M13e00.setStoreId(agentCode + "K0000");
                    }else{
                        sProductItem000M13e00.setStoreId(StringUtils.getFormatId(sPlantCustomerStockDetailDto.getStoreId(), agentCode + "D", "00000"));
                    }
                    sProductItem000M13e00.setStatusInfo(sProductItem000M13e00.getStoreId());
                }
                if(customerLevel==3){
                    if("R250082".equals(agentCode)) {
                        sProductItem000M13e00.setStoreId(agentCode + "K0000");
                    }else{
                        sProductItem000M13e00.setStoreId(StringUtils.getFormatId(sPlantCustomerStockDetailDto.getStoreId(), agentCode + "D", "00000"));
                    }
                    sProductItem000M13e00.setStatusInfo(StringUtils.getFormatId(sPlantCustomerStockDetailDto.getCustomerId(), agentCode + "C", "00000"));
                }
                sProductItem000M13e00.setStatus("在渠道中");
                sProductItem000M13e00.setUpdateTime(date);
                if(!itemMap.containsKey(agentCode)){
                    List<SProductItem000M13e00> list=Lists.newArrayList();
                    itemMap.put(agentCode,list);
                }
                itemMap.get(agentCode).add(sProductItem000M13e00);
            }
        }
        logger.info("IDVIVO:库存串码明细数据上抛开始" + LocalDateTime.now());
        for(String agentCode:stockMap.keySet()){
            List<SProductItemStocksM13e00> sProductItemStocksM13e00List =stockMap.get(agentCode);
//            sProductItemStocksM13e00Repository.deleteIDvivoByUpdateTime(date,LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1)),agentCode);
            sProductItemStocksM13e00Repository.batchIDvivoSave(sProductItemStocksM13e00List,agentCode);
        }
        logger.info("IDVIVO:库存串码明细数据上抛结束" + LocalDateTime.now());
        logger.info("IDVIVO:渠道串码明细数据上抛开始" + LocalDateTime.now());
        for(String agentCode:itemMap.keySet()){
            List<SProductItem000M13e00> sProductItem000M13e00List =itemMap.get(agentCode);
//            sProductItem000M13e00Repository.deleteIDvivoByUpdateTime(date,LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1)),agentCode);
            sProductItem000M13e00Repository.batchIDvivoSave(sProductItem000M13e00List,agentCode);
        }
        logger.info("IDVIVO:渠道串码明细数据上抛结束" + LocalDateTime.now());
    }

    //获取仓库数据
    @FutureDataSource
    public List<SStoresM13e00> getCustomerStoreData(){
        List<SStoresM13e00> sStoresM13e00sList= sStoresM13e00Repository.findIDvivoStore();
        cacheUtils.initCacheInput(sStoresM13e00sList);
        return sStoresM13e00sList;
    }

    //上抛仓库数据
    @FactoryDataSource
    @Transactional
    public void pushCustomerStoresData(List<SStoresM13e00> sStoresM13e00sList){
        Map<String,List<SStoresM13e00>> storeMap= Maps.newHashMap();
        if(CollectionUtil.isNotEmpty(sStoresM13e00sList)){
            for(SStoresM13e00 storesM13e00:sStoresM13e00sList){
                String agentCode=storesM13e00.getAgentCode();
                if(!storeMap.containsKey(agentCode)){
                    List<SStoresM13e00> list = Lists.newArrayList();
                    storeMap.put(agentCode,list);
                }
                storeMap.get(agentCode).add(storesM13e00);
            }
            logger.info("IDVIVO:仓库数据上抛开始" + LocalDateTime.now());
            for(String agentCode:storeMap.keySet()){
                sStoresM13e00Repository.deleteIDvivoStores(agentCode);
                List<SStoresM13e00> sStoresM13e00List=storeMap.get(agentCode);
                sStoresM13e00Repository.batchIDvivoSave(sStoresM13e00List,agentCode);
            }
            logger.info("IDVIVO:仓库数据上抛结束" + LocalDateTime.now());
        }
    }

    //获取核销数据
    @FutureDataSource
    public List<VivoCustomerSaleImeiDto> getProductImeSaleData(String date){
        if (StringUtils.isBlank(date)){
            date = LocalDateUtils.format(LocalDate.now());
        }
        String dateStart = date;
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<VivoCustomerSaleImeiDto>  vivoCustomerSaleImeiDtos= vivoCustomerSaleImeiRepository.findProductSaleImei(dateStart,dateEnd);
        cacheUtils.initCacheInput(vivoCustomerSaleImeiDtos);
        return vivoCustomerSaleImeiRepository.findProductSaleImei(dateStart,dateEnd);
    }

    @FactoryDataSource
    @Transactional
    public void pushProductImeSaleData(List<VivoCustomerSaleImeiDto> vivoCustomerSaleImeiDtoList,Map<String,String> productColorMap,String date){
        if (StringUtils.isBlank(date)){
            date = LocalDateUtils.format(LocalDate.now());
        }
        Map<String,List<SPlantEndProductSaleM13e00>> saleMap=Maps.newHashMap();
        String dateStart = date;
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        for (VivoCustomerSaleImeiDto vivoCustomerSaleImeiDto : vivoCustomerSaleImeiDtoList){
            if (StringUtils.isBlank(productColorMap.get(vivoCustomerSaleImeiDto.getProductId()))){
                continue;
            }
            String agentCode=vivoCustomerSaleImeiDto.getAgentCode();
            SPlantEndProductSaleM13e00 sPlantEndProductSaleM13e00 = new SPlantEndProductSaleM13e00();
            sPlantEndProductSaleM13e00.setCompanyID(agentCode);
            sPlantEndProductSaleM13e00.setEndBillID(String.valueOf(System.currentTimeMillis()));
            sPlantEndProductSaleM13e00.setProductID(productColorMap.get(vivoCustomerSaleImeiDto.getProductId()));
            sPlantEndProductSaleM13e00.setSaleCount(1);
            sPlantEndProductSaleM13e00.setImei(vivoCustomerSaleImeiDto.getImei());
            sPlantEndProductSaleM13e00.setBillDate(vivoCustomerSaleImeiDto.getSaleTime());
            sPlantEndProductSaleM13e00.setDealerID(StringUtils.getFormatId(vivoCustomerSaleImeiDto.getShopId(),agentCode+"C","00000"));
            sPlantEndProductSaleM13e00.setCreatedTime(LocalDateTimeUtils.format(LocalDateTime.now()));
            if(!saleMap.containsKey(agentCode)){
                List<SPlantEndProductSaleM13e00> list=Lists.newArrayList();
                saleMap.put(agentCode,list);
            }
            saleMap.get(agentCode).add(sPlantEndProductSaleM13e00);
        }
        logger.info("IDVIVO:开始上抛核销记录数据" + LocalDateTime.now());
        for(String agentCode:saleMap.keySet()){
            List<SPlantEndProductSaleM13e00> sPlantEndProduuctSaleImeiList=saleMap.get(agentCode);
            sPlantEndProductSaleM13e00Repository.deleteIDvivoByBillDate(dateStart,dateEnd,agentCode);
            sPlantEndProductSaleM13e00Repository.batchIDvivoSave(sPlantEndProduuctSaleImeiList);
        }
        logger.info("IDVIVO:上抛核销记录结束" + LocalDateTime.now());
    }


    @LocalDataSource
    public Map<String,String> getProductColorMap(){
        System.err.println("CompanyName:"+DbContextHolder.get().getCompanyName()+"DataSourceType:"+DbContextHolder.get().getDataSourceType());
        List<VivoPlantProducts> vivoPlantProductList= vivoPlantProductsRepository.findAllByProductId();
        Map<String, String> productColorMap = Maps.newHashMap();

        for (VivoPlantProducts vivoPlantProduct : vivoPlantProductList){
            String colorId = vivoPlantProduct.getColorId();
            String productId = vivoPlantProduct.getProductId();
            String lxProductId = vivoPlantProduct.getLxProductId();
            if (StringUtils.isNotBlank(colorId) && StringUtils.isNotBlank(productId)){
                productColorMap.put(productId,colorId);
            }
            if (StringUtils.isNotBlank(colorId) && StringUtils.isNotBlank(lxProductId)){
                productColorMap.put(lxProductId,colorId);
            }
        }
        return productColorMap;
    }

    private String getZoneId(String mainCode,String id){
        return StringUtils.getFormatId(id,mainCode,"0000");
    }

}
