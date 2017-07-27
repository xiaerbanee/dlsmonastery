package net.myspring.tool.modules.vivo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.tool.common.client.OfficeClient;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.modules.future.dto.OfficeDto;
import net.myspring.tool.common.utils.CacheUtils;
import net.myspring.tool.modules.vivo.domain.*;
import net.myspring.tool.modules.vivo.dto.*;
import net.myspring.tool.modules.vivo.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@LocalDataSource
public class VivoPushService {

    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private SZonesRepository sZonesRepository;
    @Autowired
    private SCustomersRepository sCustomersRepository;
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
    private SProductItemLendRepository sProductItemLendRepository;
    @Autowired
    private SPlantEndProductSaleRepository sPlantEndProductSaleRepository;
    @Autowired
    private SStoresRepository sStoresRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private RedisTemplate redisTemplate;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Transactional
    public void pushToLocal(VivoPushDto vivoPushDto){
        //同步机构数据
        pushVivoZonesData();
        //客户数据
        pushVivoPushSCustomersData(vivoPushDto.getsCustomerDtoList(),vivoPushDto.getDate());
        //库存汇总数据
        pushCustomerStockData(vivoPushDto.getsPlantCustomerStockDtoList(),vivoPushDto.getProductColorMap(),vivoPushDto.getDate());
        //库存串码明细
        pushCustomerStockDetailData(vivoPushDto.getsPlantCustomerStockDetailDtoList(),vivoPushDto.getProductColorMap(),vivoPushDto.getDate());
        if (CompanyNameEnum.JXVIVO.name().equals(DbContextHolder.get().getCompanyName())){
            //演示机数据
            pushDemoPhonesData(vivoPushDto.getsProductItemLendList(),vivoPushDto.getProductColorMap(),vivoPushDto.getDate());
        }
        //核销记录数据
        pushProductImeSaleData(vivoPushDto.getVivoCustomerSaleImeiDtoList(),vivoPushDto.getProductColorMap(),vivoPushDto.getDate());
        //一代仓库上抛
        pushSStoreData(vivoPushDto.getsStoresList());
    }



    @Transactional
    public  List<SZones> pushVivoZonesData(){
        logger.info(DbContextHolder.get().getCompanyName()+DbContextHolder.get().getDataSourceType());
        String mainCode = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue().split(CharConstant.COMMA)[0];
        List<OfficeDto> officeDtoList = officeClient.findAll();
        List<SZones> sZonesList = Lists.newArrayList();
        for(OfficeDto officeDto:officeDtoList){
            SZones sZones = new SZones();
            sZones.setZoneId(getZoneId(officeDto.getAgentCode(),officeDto.getId()));
            sZones.setZoneName(officeDto.getName());
            sZones.setShortcut(officeDto.getAgentCode());
            String[] parentIds = officeDto.getParentIds().split(CharConstant.COMMA);
            sZones.setZoneDepth(parentIds.length);
            StringBuilder zonePath = new StringBuilder(CharConstant.VERTICAL_LINE);
            for(String parentId:parentIds){
                zonePath.append(getZoneId(officeDto.getAgentCode(),parentId)).append(CharConstant.VERTICAL_LINE);
            }
            zonePath.append(getZoneId(officeDto.getAgentCode(),officeDto.getId())).append(CharConstant.VERTICAL_LINE);
            sZones.setZonePath(zonePath.toString());
            sZones.setFatherId(getZoneId(officeDto.getAgentCode(),officeDto.getParentId()));
            sZones.setSubCount(officeDto.getChildCount());
            sZones.setZoneTypes(CharConstant.EMPTY);
            sZones.setAgentCode(officeDto.getAgentCode());
            sZonesList.add(sZones);
        }
        logger.info("开始上抛机构数据"+ LocalDateTime.now());
        sZonesRepository.deleteAll();
        sZonesRepository.batchSave(sZonesList);
        logger.info("上抛机构数据完成"+LocalDateTime.now());
        return sZonesList;
    }


    @Transactional
    public void pushVivoPushSCustomersData(List<SCustomerDto> futureCustomerDtoList,String date){
        String mainCode = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue().split(CharConstant.COMMA)[0];
        List<SCustomers> sCustomersList = Lists.newArrayList();
        for(SCustomerDto futureCustomerDto :futureCustomerDtoList){
            SCustomers sCustomers = new SCustomers();
            String customerId = futureCustomerDto.getCustomerId();
            String agentCode = futureCustomerDto.getAgentCode();
            sCustomers.setCustomerLevel(futureCustomerDto.getCustomerLevel());
            if(futureCustomerDto.getCustomerLevel() == 1){
                sCustomers.setCustomerId(StringUtils.getFormatId(customerId,agentCode+"D","00000"));
                sCustomers.setCustomerName(futureCustomerDto.getAreaName());
            }else {
                sCustomers.setCustomerId(StringUtils.getFormatId(customerId,agentCode+"C","00000"));
                sCustomers.setCustomerName(futureCustomerDto.getCustomerName());
                sCustomers.setCustomerStr4(StringUtils.getFormatId(futureCustomerDto.getCustomerStr4(),futureCustomerDto.getAgentCode()+"D","00000"));
            }
            if("R250082".equals(agentCode)){
                sCustomers.setCustomerStr4(agentCode + "K0000");
            }
            sCustomers.setZoneId(getZoneId(agentCode,futureCustomerDto.getZoneId()));
            if (StringUtils.isBlank(agentCode)){
                System.err.println(futureCustomerDto.getCustomerId());
            }
            sCustomers.setCompanyId(agentCode);
            sCustomers.setRecordDate(futureCustomerDto.getRecordDate());
            sCustomers.setCustomerStr1(futureCustomerDto.getCustomerStr1());
            sCustomers.setCustomerStr10(agentCode);
            sCustomers.setAgentCode(agentCode);
            sCustomersList.add(sCustomers);
        }
        logger.info("开始上抛客户数据"+LocalDateTime.now());
        sCustomersRepository.deleteAll();
        sCustomersRepository.batchSave(sCustomersList);
        logger.info("上抛客户数据完成"+LocalDateTime.now());
    }


    @Transactional
    public void pushCustomerStockData(List<SPlantCustomerStockDto> sPlantCustomerStockDtoList,Map<String,String> productColorMap,String date){

        String dateStart = LocalDateUtils.format(LocalDateUtils.parse(date));
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(dateStart));
        String mainCode = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue().split(CharConstant.COMMA)[0];

        List<SPlantStockStores> sPlantStockStoresList = Lists.newArrayList();
        List<SPlantStockSupply> sPlantStockSupplyList = Lists.newArrayList();
        List<SPlantStockDealer> sPlantStockDealerList = Lists.newArrayList();

        for (SPlantCustomerStockDto sPlantCustomerStockDto : sPlantCustomerStockDtoList) {
            String colorId = productColorMap.get(sPlantCustomerStockDto.getProductId());
            if (StringUtils.isBlank(colorId)){
                continue;
            }
            int customerLevel = sPlantCustomerStockDto.getCustomerLevel();
            String agentCode=sPlantCustomerStockDto.getAgentCode();
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
                sPlantStockStores.setAgentCode(agentCode);
                sPlantStockStoresList.add(sPlantStockStores);
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
                sPlantStockSupply.setAgentCode(agentCode);
                sPlantStockSupplyList.add(sPlantStockSupply);
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
                sPlantStockDealer.setAgentCode(agentCode);
                sPlantStockDealerList.add(sPlantStockDealer);
            }
        }

        logger.info("开始上抛一代库库存数据"+LocalDateTime.now());
        sPlantStockStoresRepository.deleteByAccountDate(dateStart,dateEnd);
        sPlantStockStoresRepository.batchSave(sPlantStockStoresList);
        logger.info("上抛一代库库存数据成功"+LocalDateTime.now());

        logger.info("开始上抛二代库库存数据"+LocalDateTime.now());
        sPlantStockSupplyRepository.deleteByAccountDate(dateStart,dateEnd);
        sPlantStockSupplyRepository.batchSave(sPlantStockSupplyList);
        logger.info("上抛二代库库存数据成功"+LocalDateTime.now());

        logger.info("开始上抛经销商库存数据"+LocalDateTime.now());
        sPlantStockDealerRepository.deleteByAccountDate(dateStart,dateEnd);
        sPlantStockDealerRepository.batchSave(sPlantStockDealerList);
        logger.info("上抛经销商库存数据成功"+LocalDateTime.now());
    }

    @Transactional
    public void pushCustomerStockDetailData(List<SPlantCustomerStockDetailDto> sPlantCustomerStockDetailDtoList,Map<String,String> productColorMap,String date){

        LocalDate dateStart = LocalDateUtils.parse(date).minusYears(1);
        LocalDate dateEnd = LocalDateUtils.parse(date).plusDays(1);
        String mainCode = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue().split(CharConstant.COMMA)[0];

        List<SProductItemStocks> sProductItemStocksList = Lists.newArrayList();
        List<SProductItem000> sProductItem000List = Lists.newArrayList();

        for (SPlantCustomerStockDetailDto sPlantCustomerStockDetailDto : sPlantCustomerStockDetailDtoList){
            int customerLevel = sPlantCustomerStockDetailDto.getCustomerLevel();
            String colorId = productColorMap.get(sPlantCustomerStockDetailDto.getProductId());
            if(StringUtils.isBlank(colorId)){
                continue;
            }
            String agentCode = sPlantCustomerStockDetailDto.getAgentCode();
            String productNo = sPlantCustomerStockDetailDto.getIme();
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
                sProductItemStocksList.add(sProductItemStocks);
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
                sProductItem000List.add(sProductItem000);
            }
        }

        logger.info("开始同步库存串码明细数据:"+LocalDateTime.now());
        sProductItemStocksRepository.deleteByUpdateTime(date,LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1)));
        sProductItemStocksRepository.batchSave(sProductItemStocksList);
        sProductItem000Repository.deleteByUpdateTime(date,LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1)));
        sProductItem000Repository.batchSave(sProductItem000List);
        logger.info("同步库存串码明细数据完成:"+LocalDateTime.now());
    }

    @Transactional
    public void pushDemoPhonesData(List<SProductItemLend> sProductItemLendList, Map<String,String> productColorMap, String date){
        String mainCode = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue().split(CharConstant.COMMA)[0];
        if (StringUtils.isBlank(date)){
            date = LocalDateUtils.format(LocalDate.now());
        }
        String dateStart = date;
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<SProductItemLend> sProductItemLends = Lists.newArrayList();

        for (SProductItemLend sProductItemLend : sProductItemLendList){
            String productId=sProductItemLend.getProductID();
            if (StringUtils.isBlank(productColorMap.get(productId))){
                continue;
            }
            sProductItemLend.setStatusInfo(StringUtils.getFormatId(sProductItemLend.getCompanyID(),"C","00000"));
            sProductItemLend.setCompanyID(mainCode);
            sProductItemLend.setProductID(productColorMap.get(productId));
            sProductItemLend.setStoreID(mainCode+"K0000");
            sProductItemLend.setStatus("借机出库");
            sProductItemLends.add(sProductItemLend);
        }

        logger.info("开始上抛借机数据:"+LocalDateTime.now());
        sProductItemLendRepository.deleteByUpdateTime(dateStart,dateEnd);
        sProductItemLendRepository.batchSave(sProductItemLends);
        logger.info("上抛借机数据结束:"+LocalDateTime.now());
    }

    @Transactional
    public void pushProductImeSaleData(List<VivoCustomerSaleImeiDto> vivoCustomerSaleImeiDtoList,Map<String,String> productColorMap,String date){
        String mainCode = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue().split(CharConstant.COMMA)[0];
        if (StringUtils.isBlank(date)){
            date = LocalDateUtils.format(LocalDate.now());
        }
        String dateStart = date;
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<SPlantEndProductSale> sPlantEndProductSaleList = Lists.newArrayList();
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
            sPlantEndProductSale.setAgentCode(agentCode);
            sPlantEndProductSaleList.add(sPlantEndProductSale);
        }
        logger.info("开始上抛核销记录数据:"+LocalDateTime.now());
        sPlantEndProductSaleRepository.deleteByBillDate(dateStart,dateEnd);
        sPlantEndProductSaleRepository.batchSave(sPlantEndProductSaleList);
        logger.info("上抛核销记录数据结束:"+LocalDateTime.now());
    }

    @Transactional
    public void pushSStoreData(List<SStores> sStoresList){
        String mainCode = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue().split(CharConstant.COMMA)[0];
        if (CompanyNameEnum.JXVIVO.name().equals(DbContextHolder.get().getCompanyName())){
            SStores sStores = new SStores();
            sStores.setStoreID(mainCode + "K0000");
            sStores.setStoreName(mainCode + "大库");
            sStores.setAgentCode(mainCode);
            sStoresList.clear();
            sStoresList.add(sStores);
        }
        logger.info("上抛一代仓库数据开始:"+LocalDateTime.now());
        sStoresRepository.deleteAll();
        sStoresRepository.batchSave(sStoresList);
        logger.info("上抛一代仓库数据结束:"+LocalDateTime.now());
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
