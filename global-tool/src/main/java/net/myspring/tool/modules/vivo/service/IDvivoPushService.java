package net.myspring.tool.modules.vivo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.client.OfficeClient;
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
    private SZonesRepository sZonesRepository;
    @Autowired
    private SCustomersRepository sCustomersRepository;
    @Autowired
    private SPlantCustomerStockRepository sPlantCustomerStockRepository;
    @Autowired
    private SPlantCustomerStockDetailRepository sPlantCustomerStockDetailRepository;
    @Autowired
    private VivoPlantProductsRepository vivoPlantProductsRepository;
    @Autowired
    private SPlantStockStoresRepository sPlantStockStoresRepository;
    @Autowired
    private SPlantStockSupplyRepository sPlantStockSupplyRepository;
    @Autowired
    private SPlantStockDealerRepository sPlantStockDealerRepository;
    @Autowired
    private SProductItemStocksRepository sProductItemStocksRepository;
    @Autowired
    private SProductItem000Repository sProductItem000Repository;
    @Autowired
    private SStoresRepository sStoresRepository;
    @Autowired
    private VivoCustomerSaleImeiRepository vivoCustomerSaleImeiRepository;
    @Autowired
    private SPlantEndProductSaleRepository sPlantEndProductSaleRepository;
    @Autowired
    private CacheUtils cacheUtils;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @FactoryDataSource
    @Transactional
    public  void pushIDvivoZonesData(){
        List<OfficeEntity> officeEntityList = officeClient.findAll();
        Map<String,List<SZones>>  zonesMap=Maps.newHashMap();
        for(OfficeEntity officeEntity:officeEntityList){
            if(StringUtils.isBlank(officeEntity.getAgentCode())){
                continue;
            }
            SZones sZones = new SZones();
            sZones.setZoneId(getZoneId(officeEntity.getAgentCode(),officeEntity.getId()));
            sZones.setZoneName(officeEntity.getName());
            sZones.setShortcut(officeEntity.getAgentCode());
            String[] parentIds = officeEntity.getParentIds().split(CharConstant.COMMA);
            sZones.setZoneDepth(parentIds.length);
            StringBuilder zonePath = new StringBuilder(CharConstant.VERTICAL_LINE);
            for(String parentId:parentIds){
                zonePath.append(getZoneId(officeEntity.getAgentCode(),parentId)).append(CharConstant.VERTICAL_LINE);
            }
            zonePath.append(getZoneId(officeEntity.getAgentCode(),officeEntity.getId())).append(CharConstant.VERTICAL_LINE);
            sZones.setZonePath(zonePath.toString());
            sZones.setFatherId(getZoneId(officeEntity.getAgentCode(),officeEntity.getParentId()));
            sZones.setSubCount(officeEntity.getChildCount());
            sZones.setZoneTypes(CharConstant.EMPTY);
            if(!zonesMap.containsKey(officeEntity.getAgentCode())){
                List<SZones> list=Lists.newArrayList();
                zonesMap.put(officeEntity.getAgentCode(),list);
            }
            zonesMap.get(officeEntity.getAgentCode()).add(sZones);
        }
        logger.info("开始上抛机构数据"+ LocalDateTime.now());
        sZonesRepository.deleteIDvivoZones();
        for(String agentCode:zonesMap.keySet()){
            sZonesRepository.batchSaveIDvivo(agentCode,zonesMap.get(agentCode));
        }
        logger.info("上抛机构数据完成"+LocalDateTime.now());
    }

    @FutureDataSource
    public List<SCustomerDto> getVivoCustomersData(String date){
        List<SCustomerDto> sCustomerDtoList = sCustomersRepository.findIDvivoCustomers(LocalDateUtils.parse(date));
        cacheUtils.initCacheInput(sCustomerDtoList);
        return sCustomerDtoList;
    }

    @FactoryDataSource
    @Transactional
    public void pushIDVivoSCustomersData(List<SCustomerDto> sCustomerDtos){
        String mainCode = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).split(CharConstant.COMMA)[0].replace("\"","");
        Map<String,List<SCustomers>> customerMap = Maps.newHashMap();
         for(SCustomerDto futureCustomerDto :sCustomerDtos){
            SCustomers sCustomers = new SCustomers();
            String customerId = futureCustomerDto.getCustomerId();
            String agentCode=futureCustomerDto.getAgentCode();
            sCustomers.setCustomerLevel(futureCustomerDto.getCustomerLevel());
            if(futureCustomerDto.getCustomerLevel() == 1){
                sCustomers.setCustomerId(StringUtils.getFormatId(customerId,mainCode+"D","00000"));
                sCustomers.setCustomerName(futureCustomerDto.getAreaName());
            }else {
                sCustomers.setCustomerId(StringUtils.getFormatId(customerId,mainCode+"C","00000"));
                sCustomers.setCustomerName(futureCustomerDto.getCustomerName());
                sCustomers.setCustomerStr4(StringUtils.getFormatId(futureCustomerDto.getCustomerStr4(),mainCode+"D","00000"));
            }
            if("R250082".equals(agentCode)){
                sCustomers.setCustomerStr4(agentCode + "K0000");
            }
            sCustomers.setZoneId(getZoneId(agentCode,futureCustomerDto.getZoneId()));
            sCustomers.setCompanyId(agentCode);
            sCustomers.setRecordDate(futureCustomerDto.getRecordDate());
            sCustomers.setCustomerStr1(futureCustomerDto.getCustomerStr1());
            sCustomers.setCustomerStr10(agentCode);
            if(!customerMap.containsKey(agentCode)){
                List<SCustomers> list=Lists.newArrayList();
                customerMap.put(agentCode,list);
            }
            customerMap.get(agentCode).add(sCustomers);
        }
        logger.info("开始上抛客户数据"+LocalDateTime.now());
        sCustomersRepository.deleteIDvivoCustomers();
        for(String agentCode:customerMap.keySet()){
            List<SCustomers> sCustomersList=customerMap.get(agentCode);
            sCustomersRepository.batchIDvivoSave(sCustomersList,agentCode);
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

        Map<String,List<SPlantStockSupply>> supplyMap=Maps.newHashMap();
        Map<String,List<SPlantStockDealer>> dealerMap=Maps.newHashMap();
        Map<String,List<SPlantStockStores>> storesMap=Maps.newHashMap();

        for (SPlantCustomerStockDto sPlantCustomerStockDto : sPlantCustomerStockDtoList) {
            int customerLevel = sPlantCustomerStockDto.getCustomerLevel();
            String colorId = productColorMap.get(sPlantCustomerStockDto.getProductId());
            String agentCode=sPlantCustomerStockDto.getAgentCode();
            if (StringUtils.isBlank(colorId)){
                continue;
            }
            if (customerLevel == 1) {
                SPlantStockStores sPlantStockStores = new SPlantStockStores();
                sPlantStockStores.setCompanyId(agentCode);
                sPlantStockStores.setStoreId(agentCode+"K0000");
                sPlantStockStores.setProductId(colorId);
                sPlantStockStores.setSumStock(0);
                sPlantStockStores.setUseAbleStock(sPlantCustomerStockDto.getUseAbleStock());
                sPlantStockStores.setBad(0);
                sPlantStockStores.setCreatedTime(LocalDateTimeUtils.format(LocalDateTime.now()));
                sPlantStockStores.setAccountDate(dateStart);
                if(!storesMap.containsKey(agentCode)){
                    List<SPlantStockStores> list = Lists.newArrayList();
                    storesMap.put(agentCode,list);
                }
                storesMap.get(agentCode).add(sPlantStockStores);
            }
            if (customerLevel == 2) {
                String supplyId = StringUtils.getFormatId(sPlantCustomerStockDto.getCustomerId(),"D","00000");
                SPlantStockSupply sPlantStockSupply = new SPlantStockSupply();
                sPlantStockSupply.setCompanyId(agentCode);
                sPlantStockSupply.setSupplyId(supplyId);
                sPlantStockSupply.setProductId(colorId);
                sPlantStockSupply.setCreatedTime(LocalDateTimeUtils.format(LocalDateTime.now()));
                sPlantStockSupply.setSumStock(0);
                sPlantStockSupply.setUseAbleStock(sPlantCustomerStockDto.getUseAbleStock());
                sPlantStockSupply.setBad(0);
                sPlantStockSupply.setAccountDate(dateStart);
                if(!supplyMap.containsKey(agentCode)){
                    List<SPlantStockSupply> list = Lists.newArrayList();
                    supplyMap.put(agentCode,list);
                }
                supplyMap.get(agentCode).add(sPlantStockSupply);
            }
            if (customerLevel == 3) {
                String dealerId = StringUtils.getFormatId(sPlantCustomerStockDto.getCustomerId(),"C","00000");
                SPlantStockDealer sPlantStockDealer = new SPlantStockDealer();
                sPlantStockDealer.setCompanyId(agentCode);
                sPlantStockDealer.setDealerId(dealerId);
                sPlantStockDealer.setProductId(colorId);
                sPlantStockDealer.setSumStock(0);
                sPlantStockDealer.setUseAbleStock(sPlantCustomerStockDto.getUseAbleStock());
                sPlantStockDealer.setBad(0);
                sPlantStockDealer.setCreatedTime(LocalDateTimeUtils.format(LocalDateTime.now()));
                sPlantStockDealer.setAccountDate(dateStart);
                if(!dealerMap.containsKey(agentCode)){
                    List<SPlantStockDealer> list = Lists.newArrayList();
                    dealerMap.put(agentCode,list);
                }
                dealerMap.get(agentCode).add(sPlantStockDealer);
            }
        }

        logger.info("IDVIVO:大库库存数据上抛开始" + LocalDateTime.now());
        for(String agentCode:storesMap.keySet()){
            List<SPlantStockStores> sPlantStockStoresList =storesMap.get(agentCode);
            sPlantStockStoresRepository.deleteIDvivoByAccountDate(dateStart,dateEnd,agentCode);
            sPlantStockStoresRepository.batchIDvivoSave(sPlantStockStoresList,agentCode);
        }
        logger.info("IDVIVO:代理商库存数据上抛开始" + LocalDateTime.now());
        for(String agentCode:supplyMap.keySet()){
            List<SPlantStockSupply> sPlantStockSupplyList =supplyMap.get(agentCode);
            sPlantStockSupplyRepository.deleteIDvivoByAccountDate(dateStart,dateEnd,agentCode);
            sPlantStockSupplyRepository.batchIDvivoSave(sPlantStockSupplyList,agentCode);
        }
        logger.info("IDVIVO:经销商库存数据上抛开始" + LocalDateTime.now());
        for(String agentCode:dealerMap.keySet()){
            List<SPlantStockDealer> sPlantStockDealerList =dealerMap.get(agentCode);
            sPlantStockDealerRepository.deleteIDvivoByAccountDate(dateStart,dateEnd,agentCode);
            sPlantStockDealerRepository.batchIDvivoSave(sPlantStockDealerList,agentCode);
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
        Map<String,List<SProductItemStocks>> stockMap=Maps.newHashMap();
        Map<String,List<SProductItem000>> itemMap=Maps.newHashMap();
        for (SPlantCustomerStockDetailDto sPlantCustomerStockDetailDto : sPlantCustomerStockDetailDtoList){
            int customerLevel = sPlantCustomerStockDetailDto.getCustomerLevel();
            String colorId = productColorMap.get(sPlantCustomerStockDetailDto.getProductId());
            String agentCode=sPlantCustomerStockDetailDto.getAgentCode();
            String productNo=sPlantCustomerStockDetailDto.getIme();
            if(StringUtils.isBlank(colorId)){
                continue;
            }
            if (customerLevel == 1){
                SProductItemStocks sProductItemStocks = new SProductItemStocks();
                sProductItemStocks.setCompanyId(agentCode);
                sProductItemStocks.setProductId(colorId);
                sProductItemStocks.setProductNo(productNo);
                sProductItemStocks.setStoreId(agentCode+"K0000");
                sProductItemStocks.setStatus("在渠道中");
                sProductItemStocks.setStatusInfo(agentCode+"K0000");
                sProductItemStocks.setAgentCode(agentCode);
                sProductItemStocks.setUpdateTime(date);
                if(!stockMap.containsKey(agentCode)){
                    List<SProductItemStocks> list=Lists.newArrayList();
                    stockMap.put(agentCode,list);
                }
                stockMap.get(agentCode).add(sProductItemStocks);
            } else {
                SProductItem000 sProductItem000 = new SProductItem000();
                sProductItem000.setCompanyId(agentCode);
                sProductItem000.setProductId(colorId);
                sProductItem000.setProductNo(productNo);
                sProductItem000.setAgentCode(agentCode);
                if(customerLevel == 2){
                    if("R250082".equals(agentCode)) {
                        sProductItem000.setStoreId(agentCode + "K0000");
                    }else{
                        sProductItem000.setStoreId(StringUtils.getFormatId(sPlantCustomerStockDetailDto.getStoreId(), agentCode + "D", "00000"));
                    }
                    sProductItem000.setStatusInfo(sProductItem000.getStoreId());
                }
                if(customerLevel==3){
                    if("R250082".equals(agentCode)) {
                        sProductItem000.setStoreId(agentCode + "K0000");
                    }else{
                        sProductItem000.setStoreId(StringUtils.getFormatId(sPlantCustomerStockDetailDto.getStoreId(), agentCode + "D", "00000"));
                    }
                    sProductItem000.setStatusInfo(StringUtils.getFormatId(sPlantCustomerStockDetailDto.getCustomerId(), agentCode + "C", "00000"));
                }
                sProductItem000.setStatus("在渠道中");
                sProductItem000.setUpdateTime(date);
                if(!itemMap.containsKey(agentCode)){
                    List<SProductItem000> list=Lists.newArrayList();
                    itemMap.put(agentCode,list);
                }
                itemMap.get(agentCode).add(sProductItem000);
            }
        }
        logger.info("IDVIVO:库存串码明细数据上抛开始" + LocalDateTime.now());
        for(String agentCode:stockMap.keySet()){
            List<SProductItemStocks> sProductItemStocksList =stockMap.get(agentCode);
//            sProductItemStocksRepository.deleteIDvivoByUpdateTime(date,LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1)),agentCode);
            sProductItemStocksRepository.batchIDvivoSave(sProductItemStocksList,agentCode);
        }
        logger.info("IDVIVO:库存串码明细数据上抛结束" + LocalDateTime.now());
        logger.info("IDVIVO:渠道串码明细数据上抛开始" + LocalDateTime.now());
        for(String agentCode:itemMap.keySet()){
            List<SProductItem000> sProductItem000List =itemMap.get(agentCode);
//            sProductItem000Repository.deleteIDvivoByUpdateTime(date,LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1)),agentCode);
            sProductItem000Repository.batchIDvivoSave(sProductItem000List,agentCode);
        }
        logger.info("IDVIVO:渠道串码明细数据上抛结束" + LocalDateTime.now());
    }

    //获取仓库数据
    @FutureDataSource
    public List<SStores> getCustomerStoreData(){
        List<SStores> sStoressList= sStoresRepository.findIDvivoStore();
        cacheUtils.initCacheInput(sStoressList);
        return sStoressList;
    }

    //上抛仓库数据
    @FactoryDataSource
    @Transactional
    public void pushCustomerStoresData(List<SStores> sStoressList){
        Map<String,List<SStores>> storeMap= Maps.newHashMap();
        if(CollectionUtil.isNotEmpty(sStoressList)){
            for(SStores stores:sStoressList){
                String agentCode=stores.getAgentCode();
                if(!storeMap.containsKey(agentCode)){
                    List<SStores> list = Lists.newArrayList();
                    storeMap.put(agentCode,list);
                }
                storeMap.get(agentCode).add(stores);
            }
            logger.info("IDVIVO:仓库数据上抛开始" + LocalDateTime.now());
            for(String agentCode:storeMap.keySet()){
                sStoresRepository.deleteIDvivoStores(agentCode);
                List<SStores> sStoresList=storeMap.get(agentCode);
                sStoresRepository.batchIDvivoSave(sStoresList,agentCode);
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
        return vivoCustomerSaleImeiDtos;
    }

    @FactoryDataSource
    @Transactional
    public void pushProductImeSaleData(List<VivoCustomerSaleImeiDto> vivoCustomerSaleImeiDtoList,Map<String,String> productColorMap,String date){
        if (StringUtils.isBlank(date)){
            date = LocalDateUtils.format(LocalDate.now());
        }
        Map<String,List<SPlantEndProductSale>> saleMap=Maps.newHashMap();
        String dateStart = date;
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        for (VivoCustomerSaleImeiDto vivoCustomerSaleImeiDto : vivoCustomerSaleImeiDtoList){
            if (StringUtils.isBlank(productColorMap.get(vivoCustomerSaleImeiDto.getProductId()))){
                continue;
            }
            String agentCode=vivoCustomerSaleImeiDto.getAgentCode();
            SPlantEndProductSale sPlantEndProductSale = new SPlantEndProductSale();
            sPlantEndProductSale.setCompanyID(agentCode);
            sPlantEndProductSale.setEndBillID(String.valueOf(System.currentTimeMillis()));
            sPlantEndProductSale.setProductID(productColorMap.get(vivoCustomerSaleImeiDto.getProductId()));
            sPlantEndProductSale.setSaleCount(1);
            sPlantEndProductSale.setImei(vivoCustomerSaleImeiDto.getImei());
            sPlantEndProductSale.setBillDate(vivoCustomerSaleImeiDto.getSaleTime());
            sPlantEndProductSale.setDealerID(StringUtils.getFormatId(vivoCustomerSaleImeiDto.getShopId(),agentCode+"C","00000"));
            sPlantEndProductSale.setCreatedTime(LocalDateTimeUtils.format(LocalDateTime.now()));
            if(!saleMap.containsKey(agentCode)){
                List<SPlantEndProductSale> list=Lists.newArrayList();
                saleMap.put(agentCode,list);
            }
            saleMap.get(agentCode).add(sPlantEndProductSale);
        }
        logger.info("IDVIVO:开始上抛核销记录数据" + LocalDateTime.now());
        for(String agentCode:saleMap.keySet()){
            List<SPlantEndProductSale> sPlantEndProduuctSaleImeiList=saleMap.get(agentCode);
            sPlantEndProductSaleRepository.deleteIDvivoByBillDate(dateStart,dateEnd,agentCode);
            sPlantEndProductSaleRepository.batchIDvivoSave(sPlantEndProduuctSaleImeiList,agentCode);
        }
        logger.info("IDVIVO:上抛核销记录结束" + LocalDateTime.now());
    }


    @LocalDataSource
    public Map<String,String> getProductColorMap(){
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
