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
import net.myspring.tool.modules.vivo.repository.*;
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
public class VivoPushService {

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
    private CacheUtils cacheUtils;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @FactoryDataSource
    @Transactional
    public  List<SZonesM13e00> pushVivoZonesData(){
        logger.info(DbContextHolder.get().getCompanyName()+DbContextHolder.get().getDataSourceType());
        String mainCode = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).split(CharConstant.COMMA)[0].replace("\"","");
        List<OfficeEntity> officeEntityList = officeClient.findAll();
        List<SZonesM13e00> sZonesM13e00List = Lists.newArrayList();
        for(OfficeEntity officeEntity:officeEntityList){
            SZonesM13e00 sZonesM13e00 = new SZonesM13e00();
            sZonesM13e00.setZoneId(getZoneId(mainCode,officeEntity.getId()));
            if (officeEntity.getName().length() > 10){
                sZonesM13e00.setZoneName(officeEntity.getName().substring(0,10));
            } else {
                sZonesM13e00.setZoneName(officeEntity.getName());
            }
            sZonesM13e00.setShortcut(mainCode);
            String[] parentIds = officeEntity.getParentIds().split(CharConstant.COMMA);
            sZonesM13e00.setZoneDepth(parentIds.length);
            StringBuilder zonePath = new StringBuilder(CharConstant.VERTICAL_LINE);
            for(String parentId:parentIds){
                zonePath.append(getZoneId(mainCode,parentId)).append(CharConstant.VERTICAL_LINE);
            }
            zonePath.append(getZoneId(mainCode,officeEntity.getId())).append(CharConstant.VERTICAL_LINE);
            sZonesM13e00.setZonePath(zonePath.toString());
            sZonesM13e00.setFatherId(getZoneId(mainCode,officeEntity.getParentId()));
            sZonesM13e00.setSubCount(officeEntity.getChildCount());
            sZonesM13e00.setZoneTypes(CharConstant.EMPTY);
            sZonesM13e00List.add(sZonesM13e00);
        }
        logger.info("开始上抛机构数据"+ LocalDateTime.now());
        sZonesM13e00Repository.deleteAll();
        sZonesM13e00Repository.batchSave(sZonesM13e00List);
        logger.info("上抛机构数据完成"+LocalDateTime.now());
        return sZonesM13e00List;
    }

    @FutureDataSource
    public List<SCustomerDto> getVivoCustomersData(String date){
        List<SCustomerDto> sCustomerDtoList = sCustomersM13e00Repository.findVivoCustomers(LocalDateUtils.parse(date));
        cacheUtils.initCacheInput(sCustomerDtoList);
        return sCustomerDtoList;
    }

    @FactoryDataSource
    @Transactional
    public void saveVivoPushSCustomers(List<SCustomerDto> futureCustomerDtoList,String date){
        String mainCode = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).split(CharConstant.COMMA)[0].replace("\"","");
        List<SCustomersM13e00> sCustomersM13e00List = Lists.newArrayList();
        for(SCustomerDto futureCustomerDto :futureCustomerDtoList){
            SCustomersM13e00 sCustomersM13e00 = new SCustomersM13e00();
            String customerId = futureCustomerDto.getCustomerId();
            sCustomersM13e00.setCustomerLevel(futureCustomerDto.getCustomerLevel());
            if(futureCustomerDto.getCustomerLevel() == 1){
                sCustomersM13e00.setCustomerId(StringUtils.getFormatId(customerId,mainCode+"D","00000"));
                sCustomersM13e00.setCustomerName(futureCustomerDto.getAreaName());
            }else {
                sCustomersM13e00.setCustomerId(StringUtils.getFormatId(customerId,mainCode+"C","00000"));
                sCustomersM13e00.setCustomerName(futureCustomerDto.getCustomerName());
                sCustomersM13e00.setCustomerStr4(StringUtils.getFormatId(futureCustomerDto.getCustomerStr4(),mainCode+"D","00000"));
            }
            sCustomersM13e00.setZoneId(getZoneId(mainCode,futureCustomerDto.getZoneId()));
            sCustomersM13e00.setCompanyId(mainCode);
            sCustomersM13e00.setRecordDate(futureCustomerDto.getRecordDate());
            sCustomersM13e00.setCustomerStr1(futureCustomerDto.getCustomerStr1());
            sCustomersM13e00.setCustomerStr10(mainCode);
            sCustomersM13e00List.add(sCustomersM13e00);
        }
        logger.info("开始上抛客户数据"+LocalDateTime.now());
        sCustomersM13e00Repository.deleteAll();
        sCustomersM13e00Repository.batchSave(sCustomersM13e00List);
//        sCustomersM13e00Repository.batchSave(sCustomersM13e00List);
        logger.info("上抛客户数据完成"+LocalDateTime.now());
    }

    @FutureDataSource
    public List<SPlantCustomerStockDto> getCustomerStockData(String date){
        LocalDate dateStart = LocalDateUtils.parse(date).minusYears(1);
        LocalDate dateEnd = LocalDateUtils.parse(date).plusDays(1);
        List<SPlantCustomerStockDto> sPlantCustomerStockDtoList = sPlantCustomerStockRepository.findCustomerStockData(dateStart,dateEnd);
        return sPlantCustomerStockDtoList;
    }

    @FactoryDataSource
    @Transactional
    public void pushCustomerStockData(List<SPlantCustomerStockDto> sPlantCustomerStockDtoList,Map<String,String> productColorMap,String date){

        String dateStart = LocalDateUtils.format(LocalDateUtils.parse(date));
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(dateStart));
        String mainCode = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).split(CharConstant.COMMA)[0].replace("\"","");

        List<SPlantStockStoresM13e00> sPlantStockStoresM13e00List = Lists.newArrayList();
        List<SPlantStockSupplyM13e00> sPlantStockSupplyM13e00List = Lists.newArrayList();
        List<SPlantStockDealerM13e00> sPlantStockDealerM13e00List = Lists.newArrayList();

        for (SPlantCustomerStockDto sPlantCustomerStockDto : sPlantCustomerStockDtoList) {
            int customerLevel = sPlantCustomerStockDto.getCustomerLevel();
            String colorId = productColorMap.get(sPlantCustomerStockDto.getProductId());
            if (StringUtils.isBlank(colorId)){
                continue;
            }
            if (customerLevel == 1) {
                SPlantStockStoresM13e00 sPlantStockStoresM13e00 = new SPlantStockStoresM13e00();
                sPlantStockStoresM13e00.setCompanyId(mainCode);
                sPlantStockStoresM13e00.setStoreId(mainCode+"K0000");
                sPlantStockStoresM13e00.setProductId(colorId);
                sPlantStockStoresM13e00.setSumStock(0);
                sPlantStockStoresM13e00.setUseAbleStock(sPlantCustomerStockDto.getUseAbleStock());
                sPlantStockStoresM13e00.setBad(0);
                sPlantStockStoresM13e00.setCreatedTime(LocalDateTimeUtils.format(LocalDateTime.now()));
                sPlantStockStoresM13e00.setAccountDate(dateStart);
                sPlantStockStoresM13e00List.add(sPlantStockStoresM13e00);
            }
            if (customerLevel == 2) {
                String supplyId = StringUtils.getFormatId(sPlantCustomerStockDto.getCustomerId(),"D","00000");
                SPlantStockSupplyM13e00 sPlantStockSupplyM13e00 = new SPlantStockSupplyM13e00();
                sPlantStockSupplyM13e00.setCompanyId(mainCode);
                sPlantStockSupplyM13e00.setSupplyId(supplyId);
                sPlantStockSupplyM13e00.setProductId(colorId);
                sPlantStockSupplyM13e00.setCreatedTime(LocalDateTimeUtils.format(LocalDateTime.now()));
                sPlantStockSupplyM13e00.setSumStock(0);
                sPlantStockSupplyM13e00.setUseAbleStock(sPlantCustomerStockDto.getUseAbleStock());
                sPlantStockSupplyM13e00.setBad(0);
                sPlantStockSupplyM13e00.setAccountDate(dateStart);
                sPlantStockSupplyM13e00List.add(sPlantStockSupplyM13e00);
            }
            if (customerLevel == 3) {
                String dealerId = StringUtils.getFormatId(sPlantCustomerStockDto.getCustomerId(),"C","00000");
                SPlantStockDealerM13e00 sPlantStockDealerM13e00 = new SPlantStockDealerM13e00();
                sPlantStockDealerM13e00.setCompanyId(mainCode);
                sPlantStockDealerM13e00.setDealerId(dealerId);
                sPlantStockDealerM13e00.setProductId(colorId);
                sPlantStockDealerM13e00.setSumStock(0);
                sPlantStockDealerM13e00.setUseAbleStock(sPlantCustomerStockDto.getUseAbleStock());
                sPlantStockDealerM13e00.setBad(0);
                sPlantStockDealerM13e00.setCreatedTime(LocalDateTimeUtils.format(LocalDateTime.now()));
                sPlantStockDealerM13e00.setAccountDate(dateStart);
                sPlantStockDealerM13e00List.add(sPlantStockDealerM13e00);
            }
        }

        logger.info("开始上抛一代库库存数据"+LocalDateTime.now());
        sPlantStockStoresM13e00Repository.deleteByAccountDate(dateStart,dateEnd);
        sPlantStockStoresM13e00Repository.batchSave(sPlantStockStoresM13e00List);
        logger.info("上抛一代库库存数据成功"+LocalDateTime.now());

        logger.info("开始上抛二代库库存数据"+LocalDateTime.now());
        sPlantStockSupplyM13e00Repository.deleteByAccountDate(dateStart,dateEnd);
        sPlantStockSupplyM13e00Repository.batchSave(sPlantStockSupplyM13e00List);
        logger.info("上抛二代库库存数据成功"+LocalDateTime.now());

        logger.info("开始上抛经销商库存数据"+LocalDateTime.now());
        sPlantStockDealerM13e00Repository.deleteByAccountDate(dateStart,dateEnd);
        sPlantStockDealerM13e00Repository.batchSave(sPlantStockDealerM13e00List);
        logger.info("上抛经销商库存数据成功"+LocalDateTime.now());
    }

    @FutureDataSource
    public List<SPlantCustomerStockDetailDto> getCustomerStockDetailData(String date){
        LocalDate dateStart = LocalDateUtils.parse(date).minusYears(1);
        LocalDate dateEnd = LocalDateUtils.parse(date).plusDays(1);
        List<SPlantCustomerStockDetailDto> sPlantCustomerStockDetailDtoList = sPlantCustomerStockDetailRepository.findCustomerStockDetailData(dateStart,dateEnd);
        return sPlantCustomerStockDetailDtoList;
    }

    @FactoryDataSource
    @Transactional
    public void pushCustomerStockDetailData(List<SPlantCustomerStockDetailDto> sPlantCustomerStockDetailDtoList,Map<String,String> productColorMap,String date){

        LocalDate dateStart = LocalDateUtils.parse(date).minusYears(1);
        LocalDate dateEnd = LocalDateUtils.parse(date).plusDays(1);
        String mainCode = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).split(CharConstant.COMMA)[0].replace("\"","");

        List<SProductItemStocksM13e00> sProductItemStocksM13e00List = Lists.newArrayList();
        List<SProductItem000M13e00> sProductItem000M13e00List = Lists.newArrayList();

        for (SPlantCustomerStockDetailDto sPlantCustomerStockDetailDto : sPlantCustomerStockDetailDtoList){
            int customerLevel = sPlantCustomerStockDetailDto.getCustomerLevel();
            String colorId = productColorMap.get(sPlantCustomerStockDetailDto.getProductId());
            if(StringUtils.isBlank(colorId)){
                continue;
            }
            String productNo = sPlantCustomerStockDetailDto.getIme();
            if (customerLevel == 1){
                SProductItemStocksM13e00 sProductItemStocksM13e00 = new SProductItemStocksM13e00();
                sProductItemStocksM13e00.setCompanyId(mainCode);
                sProductItemStocksM13e00.setProductId(colorId);
                sProductItemStocksM13e00.setProductNo(productNo);
                sProductItemStocksM13e00.setStoreId(mainCode+"K0000");
                sProductItemStocksM13e00.setStatus("在渠道中");
                sProductItemStocksM13e00.setStatusInfo(mainCode+"K0000");
                sProductItemStocksM13e00.setUpdateTime(date);
                sProductItemStocksM13e00List.add(sProductItemStocksM13e00);
            } else {
                SProductItem000M13e00 sProductItem000M13e00 = new SProductItem000M13e00();
                sProductItem000M13e00.setCompanyId(mainCode);
                sProductItem000M13e00.setProductId(colorId);
                sProductItem000M13e00.setProductNo(productNo);
                sProductItem000M13e00.setStoreId(StringUtils.getFormatId(sPlantCustomerStockDetailDto.getStoreId(),mainCode+"D","00000"));
                if(customerLevel == 2){
                    sProductItem000M13e00.setStatusInfo(sProductItem000M13e00.getStoreId());
                } else {
                    sProductItem000M13e00.setStatusInfo(StringUtils.getFormatId(sPlantCustomerStockDetailDto.getCustomerId(),mainCode+"C","00000"));
                }
                sProductItem000M13e00.setStatus("在渠道中");
                sProductItem000M13e00.setUpdateTime(date);
                sProductItem000M13e00List.add(sProductItem000M13e00);
            }
        }

        logger.info("开始同步库存串码明细数据:"+LocalDateTime.now());
        sProductItemStocksM13e00Repository.deleteByUpdateTime(date,LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1)));
        sProductItemStocksM13e00Repository.batchSave(sProductItemStocksM13e00List);
        sProductItem000M13e00Repository.deleteByUpdateTime(date,LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1)));
        sProductItem000M13e00Repository.batchSave(sProductItem000M13e00List);
        logger.info("同步库存串码明细数据完成:"+LocalDateTime.now());
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
