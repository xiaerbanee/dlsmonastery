package net.myspring.tool.modules.oppo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.tool.common.client.OfficeClient;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.domain.DistrictEntity;
import net.myspring.tool.common.domain.OfficeEntity;
import net.myspring.tool.common.client.CustomerClient;
import net.myspring.tool.common.client.DistrictClient;
import net.myspring.tool.modules.oppo.client.OppoClient;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.common.dto.CustomerDto;
import net.myspring.tool.modules.oppo.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by guolm on 2017/6/10.
 */
@Service
@LocalDataSource
@Transactional
public class OppoPushSerivce {

    @Autowired
    private CustomerClient customerClient;
    @Autowired
    private DistrictClient districtClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OppoClient oppoClient;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private OppoPlantAgentProductSelRepository oppoPlantAgentProductSelRepository;
    @Autowired
    private OppoCustomerRepository oppoCustomerRepository;
    @Autowired
    private OppoCustomerOperatortypeRepository oppoCustomerOperatortypeRepository;
    @Autowired
    private OppoCustomerAllotRepository oppoCustomerAllotRepository;
    @Autowired
    private OppoCustomerStockRepository oppoCustomerStockRepository;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static Map<String,String> areaDepotMap=Maps.newHashMap();
    private static Map<String,CustomerDto> customerDtoMap=Maps.newHashMap();


    //上抛oppo门店数据,只上抛二代和渠道门店
    @LocalDataSource
    public List<OppoCustomer> getOppoCustomers(String agentCode) {
        List<OppoCustomer> oppoCustomers = Lists.newArrayList();
        initAreaDepotMap();
        Map<String, OppoCustomer> oppoCustomersMap = Maps.newHashMap();
        List<CustomerDto> customerDtosList=customerClient.findCustomerDtoList();
        List<DistrictEntity>  districtList=districtClient.findDistrictList();
        Map<String,DistrictEntity>  districtMap=Maps.newHashMap();
        for(DistrictEntity districtEntity:districtList){
            districtMap.put(districtEntity.getId(),districtEntity);
        }
        List<OfficeEntity> offices=officeClient.findAll();
        Map<String,OfficeEntity>  officeMap=Maps.newHashMap();
        for(OfficeEntity officeEntity:offices){
            officeMap.put(officeEntity.getId(),officeEntity);
        }
        for(CustomerDto customerDto:customerDtosList){
            String depotId=getDepotId(customerDto);
            if(StringUtils.isNotBlank(customerDto.getAreaId())){
                customerDto.setAreaName(officeMap.get(customerDto.getAreaId()).getName());
            }
            if (!oppoCustomersMap.containsKey(depotId)) {
                OppoCustomer oppoCustomer = new OppoCustomer();
                oppoCustomer.setCustomerid(depotId);
                oppoCustomer.setCustomername(getAreaDepotName(customerDto));
                String agentId = "";
                if(StringUtils.isNotBlank(customerDto.getAreaId())&&!isArea(customerDto)){
                    agentId = StringUtils.isNotBlank(areaDepotMap.get(customerDto.getAreaId())) ? areaDepotMap.get(customerDto.getAreaId()) : "";
                }
                oppoCustomer.setAgentid(agentId);
                oppoCustomer.setCompanyid(agentCode);
                oppoCustomer.setDealertype(customerDto.getSalePointType());
                oppoCustomer.setDealergrade("");
                oppoCustomer.setDealertel(customerDto.getMobilePhone());
                oppoCustomer.setCitytype(customerDto.getAreaType());
                oppoCustomer.setBussinesscenter(customerDto.getBussinessCenterName());
                oppoCustomer.setChainname(customerDto.getChainType());
                oppoCustomer.setSaletype("");
                oppoCustomer.setDoorhead(StringUtils.isNotBlank(customerDto.getDoorHead())? "1" : "0");
                oppoCustomer.setEnabledate(customerDto.getEnableDate());
                if (isArea(customerDto)) {
                    oppoCustomer.setCustomertype("代理商");
                } else {
                    oppoCustomer.setCustomertype("经销商");
                }
                oppoCustomer.setKeydealer("");
                oppoCustomer.setIsenable((customerDto.getEnabled() && customerDto.getIsHidden() != null && !customerDto.getIsHidden()) ? "有效" : "失效");
                String province="";
                String city="";
                String county="";
                if(StringUtils.isNotBlank(customerDto.getDistrictId())&&districtMap.get(customerDto.getDistrictId())!=null){
                    province=districtMap.get(customerDto.getDistrictId()).getProvince();
                    city=districtMap.get(customerDto.getDistrictId()).getCity();
                    county=districtMap.get(customerDto.getDistrictId()).getCounty();
                }
                oppoCustomer.setProvince(province);
                oppoCustomer.setCity(city);
                oppoCustomer.setCounty(county);
                oppoCustomer.setVillage(customerDto.getTownName());
                oppoCustomer.setDealerarea(customerDto.getShopArea());
                oppoCustomer.setFramenum(StringUtils.isBlank(customerDto.getFrameNum())?"":customerDto.getFrameNum());
                oppoCustomer.setDeskdoublenum(StringUtils.isBlank(customerDto.getFrameNum())?"":customerDto.getFrameNum());
                oppoCustomer.setDesksinglenum(StringUtils.isBlank(customerDto.getDeskSingleNum())?"":customerDto.getDeskSingleNum());
                oppoCustomer.setCabinetnum(StringUtils.isBlank(customerDto.getCabinetNum())?"":customerDto.getCabinetNum());
                oppoCustomer.setCreatedDate(LocalDateTime.now());
                oppoCustomersMap.put(oppoCustomer.getCustomerid(), oppoCustomer);
            }
        }
        for (String key : oppoCustomersMap.keySet()) {
            oppoCustomers.add(oppoCustomersMap.get(key));
        }
        logger.info("同步经销商数据开始");
        logger.info("oppoCustomers="+oppoCustomers.toString());
        oppoCustomerRepository.save(oppoCustomers);
        logger.info("同步经销商数据结束");
        return oppoCustomers;
    }

    //上抛运营商属性
    @LocalDataSource
    public List<OppoCustomerOperatortype> getOppoCustomerOperatortype() {
        List<OppoCustomerOperatortype> oppoCustomerOperatortypeList = Lists.newArrayList();
        initAreaDepotMap();
        List<CustomerDto> customerDtosList=customerClient.findCustomerDtoList();
        for(CustomerDto customerDto:customerDtosList){
            if(isShop(customerDto)){
                if("运营商营业厅".equals(customerDto.getSalePointType())){
                    if(StringUtils.isNotBlank((customerDto.getCarrierType()))){
                        OppoCustomerOperatortype oppoCustomerOperatortype=new OppoCustomerOperatortype();
                        oppoCustomerOperatortype.setCustomerid(getDepotId(customerDto));
                        oppoCustomerOperatortype.setCustomername(getAreaDepotName(customerDto));
                        oppoCustomerOperatortype.setOperatortype(customerDto.getCarrierType());
                        oppoCustomerOperatortype.setCreatedDate(LocalDateTime.now());
                        oppoCustomerOperatortypeList.add(oppoCustomerOperatortype);
                    }
                }else{
                    if(StringUtils.isNotBlank(customerDto.getChannelType())){
                        List<String> channelNames=StringUtils.getSplitList(customerDto.getChannelType(),CharConstant.COMMA);
                        if(CollectionUtil.isNotEmpty(channelNames)){
                            for(String channelName:channelNames){
                                OppoCustomerOperatortype oppoCustomerOperatortype=new OppoCustomerOperatortype();
                                oppoCustomerOperatortype.setCustomerid(getDepotId(customerDto));
                                oppoCustomerOperatortype.setCustomername(getAreaDepotName(customerDto));
                                oppoCustomerOperatortype.setOperatortype(channelName);
                                oppoCustomerOperatortype.setCreatedDate(LocalDateTime.now());
                                oppoCustomerOperatortypeList.add(oppoCustomerOperatortype);
                            }
                        }
                    }
                }
            }
        }
        if(CollectionUtil.isNotEmpty(oppoCustomerOperatortypeList)){
            oppoCustomerOperatortypeRepository.save(oppoCustomerOperatortypeList);
        }
        return oppoCustomerOperatortypeList;
    }

    //发货退货调拨数据上抛
    @LocalDataSource
    @Transactional
    public List<OppoCustomerAllot>  getOppoCustomerAllot(LocalDate dateStart, LocalDate dateEnd){
        initAreaDepotMap();
        String companyId="1";
        List<OppoCustomerAllot> oppoCustomerAllots=oppoClient.findOppoCustomerAllots(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        Map<String, String> productColorMap = getProductColorMap();
        Map<String, OppoCustomerAllot> oppoCustomerAllotMap = Maps.newHashMap();
        for(OppoCustomerAllot oppoCustomerAllot:oppoCustomerAllots){
            String fromDepotId=getDepotId(customerDtoMap.get(oppoCustomerAllot.getFromCustomerid()));
            String toDepotId=getDepotId(customerDtoMap.get(oppoCustomerAllot.getToCustomerid()));
            if(!fromDepotId.equals(toDepotId)){
                String colorId=productColorMap.get(oppoCustomerAllot.getProductcode());
                if(StringUtils.isNotEmpty(colorId)){
                    String key = fromDepotId +CharConstant.UNDER_LINE+ toDepotId + CharConstant.UNDER_LINE+ productColorMap.get(oppoCustomerAllot.getProductcode());
                    if (!oppoCustomerAllotMap.containsKey(key)) {
                        OppoCustomerAllot allot = new OppoCustomerAllot();
                        allot.setFromCustomerid(fromDepotId);
                        allot.setToCustomerid(toDepotId);
                        allot.setProductcode(productColorMap.get(oppoCustomerAllot.getProductcode()));
                        allot.setDate(oppoCustomerAllot.getDate());
                        allot.setQty(0);
                        oppoCustomerAllotMap.put(key, allot);
                    }
                    oppoCustomerAllotMap.get(key).setQty(oppoCustomerAllotMap.get(key).getQty() + oppoCustomerAllot.getQty());
                }
            }
        }
        List<OppoCustomerAllot>  oppoCustomerAllotList=new ArrayList<OppoCustomerAllot>(oppoCustomerAllotMap.values());
        oppoCustomerAllotRepository.save(oppoCustomerAllotList);
        return  oppoCustomerAllotList;
    }

    //上抛一代二代库存数据,不包括门店数据
    @LocalDataSource
    public List<OppoCustomerStock> getOppoCustomerStock(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap();
        Map<String, OppoCustomerStock> oppoCustomerStockHashMap = Maps.newHashMap();
        Map<String, String> productColorMap = getProductColorMap();
        List<OppoCustomerStock> oppoCustomerStocks=oppoClient.findOppoCustomerStocks(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        for(OppoCustomerStock oppoCustomerStock:oppoCustomerStocks){
            String customerId=getDepotId(customerDtoMap.get(oppoCustomerStock.getCustomerid()));
            String colorId=productColorMap.get(oppoCustomerStock.getProductcode());
            if(StringUtils.isNotEmpty(colorId)){
                String key=customerId+ CharConstant.UNDER_LINE+productColorMap.get(oppoCustomerStock.getProductcode());
                if(!oppoCustomerStockHashMap.containsKey(key)){
                    OppoCustomerStock customerStock = new OppoCustomerStock();
                    customerStock.setCustomerid(customerId);
                    customerStock.setDate(dateStart);
                    customerStock.setProductcode(productColorMap.get(oppoCustomerStock.getProductcode()));
                    customerStock.setQty(0);
                    oppoCustomerStockHashMap.put(key, customerStock);
                }
                oppoCustomerStockHashMap.get(key).setQty(oppoCustomerStockHashMap.get(key).getQty() + oppoCustomerStock.getQty());
            }
        }
        List<OppoCustomerStock>  oppoCustomerStockList=new ArrayList<OppoCustomerStock>(oppoCustomerStockHashMap.values());
        oppoCustomerStockRepository.save(oppoCustomerStockList);
        return oppoCustomerStockList;
    }

    //获取渠道串码收货数据
    @LocalDataSource
    public List<OppoCustomerImeiStock> getOppoCustomerImeiStock(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap();
        List<OppoCustomerImeiStock> oppoCustomerImeiStocks = Lists.newArrayList();
        Map<String, String> productColorMap = getProductColorMap();
        List<OppoCustomerImeiStock> oppoCustomerImeiStockList=oppoClient.findOppoCustomerImeiStocks(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        for(OppoCustomerImeiStock oppoCustomerImeiStock:oppoCustomerImeiStockList){
            oppoCustomerImeiStock.setCustomerid(oppoCustomerImeiStock.getCustomerid());
            oppoCustomerImeiStock.setProductcode(productColorMap.get(oppoCustomerImeiStock.getProductcode()));
            oppoCustomerImeiStock.setImei(oppoCustomerImeiStock.getImei());
            oppoCustomerImeiStock.setDate(dateStart);
            oppoCustomerImeiStock.setTransType(oppoCustomerImeiStock.getTransType());
        }
        return oppoCustomerImeiStocks;
    }

    //获取店核销总数据
    @LocalDataSource
    public List<OppoCustomerSale> getOppoCustomerSales(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap();
        List<OppoCustomerSale> oppoCustomerSales=oppoClient.findOppoCustomerSales(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        return oppoCustomerSales;
    }

    //	门店销售明细数据抛转
    @LocalDataSource
    public List<OppoCustomerSaleImei> getOppoCustomerSaleImes(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap();
        List<DistrictEntity>  districtList=districtClient.findDistrictList();
        Map<String,DistrictEntity>  districtMap=Maps.newHashMap();
        for(DistrictEntity districtEntity:districtList){
            districtMap.put(districtEntity.getId(),districtEntity);
        }
        List<OppoCustomerSaleImei>    oppoCustomerSaleImes=oppoClient.findOppoCustomerSaleImes(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        for(OppoCustomerSaleImei oppoCustomerSaleIme:oppoCustomerSaleImes){
            oppoCustomerSaleIme.setAgentcode("M13AMB");
            oppoCustomerSaleIme.setAgentname("JXOPPO");
        }
        return oppoCustomerSaleImes;
    }

    //门店销售数据汇总
    @LocalDataSource
    public List<OppoCustomerSaleCount> getOppoCustomerSaleCounts(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap();
        Map<String, String> productColorMap = getProductColorMap();
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoClient.findOppoCustomerSaleCounts(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        for(OppoCustomerSaleCount oppoCustomerSaleCount:oppoCustomerSaleCounts) {
            String colorId=productColorMap.get(oppoCustomerSaleCount.getProductCode());
            oppoCustomerSaleCount.setProductCode(colorId);
            oppoCustomerSaleCount.setAgentCode("M13AMB");
        }
        return oppoCustomerSaleCounts;
    }


    //门店售后退货汇总
    @LocalDataSource
    public List<OppoCustomerAfterSaleImei> getOppoCustomerAfterSaleImeis(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap();
        Map<String, String> productColorMap = getProductColorMap();
        List<OppoCustomerAfterSaleImei>  oppoCustomerAfterSaleImeis=oppoClient.findOppoCustomerAfterSaleImes(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        for(OppoCustomerAfterSaleImei oppoCustomerAfterSaleImei:oppoCustomerAfterSaleImeis){
            oppoCustomerAfterSaleImei.setProductCode(productColorMap.get(oppoCustomerAfterSaleImei.getProductCode()));
            oppoCustomerAfterSaleImei.setDate(oppoCustomerAfterSaleImei.getDate());
        }
        return oppoCustomerAfterSaleImeis;
    }

    //门店演示机汇总数据
    @LocalDataSource
    public List<OppoCustomerDemoPhone> getOppoCustomerDemoPhone(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap();
        Map<String, String> productColorMap = getProductColorMap();
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoClient.findOppoCustomerDemoPhones(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        for(OppoCustomerDemoPhone oppoCustomerDemoPhone:oppoCustomerDemoPhones){
            oppoCustomerDemoPhone.setProductCode(productColorMap.get(oppoCustomerDemoPhone.getProductCode()));
        }
        return oppoCustomerDemoPhones;
    }


    private void initAreaDepotMap(){
        List<CustomerDto> customerDtosList=customerClient.findCustomerDtoList();
        for(CustomerDto customerDto:customerDtosList){
            if(!customerDtoMap.containsKey(customerDto.getDepotId())){
                customerDtoMap.put(customerDto.getDepotId(),customerDto);
            }
            if(isArea(customerDto)){
                String areaId=customerDto.getAreaId();
                String depotId=customerDto.getDepotId();
                if(!areaDepotMap.containsKey(areaId)){
                    areaDepotMap.put(areaId,depotId);
                }
            }
        }
    }

    private  String  getDepotId(CustomerDto customerDto){
        String jointLeavel=customerDto.getJointLeavel();
        String storeId=customerDto.getStoreId();
        String  areaId=customerDto.getAreaId();
        if(StringUtils.isNotBlank(storeId)&&StringUtils.isNotBlank(areaId)&&"一级".equals(jointLeavel)){
            return "";
        }else{
            if(isArea(customerDto)){
                if(!areaDepotMap.containsKey(areaId)){
                    areaDepotMap.put(areaId,customerDto.getDepotId());
                }
                return areaDepotMap.get(areaId);
            }else{
                return  customerDto.getDepotId();
            }
        }
    }

    private  String  getAreaDepotName(CustomerDto customerDto ) {
        String jointLeavel=customerDto.getJointLeavel();
        String storeId=customerDto.getStoreId();
        String  areaId=customerDto.getAreaId();
        if(StringUtils.isNotBlank(storeId)&&StringUtils.isNotBlank(areaId)&&"一级".equals(jointLeavel)){
            return "";
        } else {
            if (isArea(customerDto)) {
                return customerDto.getAreaName();
            } else {
                return customerDto.getDepotName();
            }
        }
    }

    private Boolean isArea(CustomerDto customerDto){
        String jointLeavel=customerDto.getJointLeavel();
        String  areaId=customerDto.getAreaId();
        String storeId=customerDto.getStoreId();
        if(StringUtils.isNotBlank(storeId)&&StringUtils.isNotBlank(areaId)&&"二级".equals(jointLeavel)){
            return true;
        }else{
            return false;
        }
    }
    private Boolean isShop(CustomerDto customerDto){
        String jointLeavel=customerDto.getJointLeavel();
        String storeId=customerDto.getStoreId();
        String shopId=customerDto.getShopId();
        if(StringUtils.isBlank(storeId)&& StringUtils.isNotBlank(shopId)&&StringUtils.isBlank(jointLeavel)){
            return true;
        }else{
            return false;
        }
    }

    private Map<String,String> getProductColorMap(){
        List<OppoPlantAgentProductSel> oppoPlantAgentProductSels=oppoPlantAgentProductSelRepository.findAllByProductId();
        Map<String, String> productColorMap = Maps.newHashMap();
        for (OppoPlantAgentProductSel oppoPlantAgentProductSel : oppoPlantAgentProductSels) {
            String colorId = oppoPlantAgentProductSel.getColorId();
            String productId=oppoPlantAgentProductSel.getProductId();
            String lxProductId=oppoPlantAgentProductSel.getLxProductId();
            if (StringUtils.isNotBlank(colorId) && StringUtils.isNotBlank(productId)) {
                productColorMap.put(productId, colorId);
            }
            if (StringUtils.isNotBlank(colorId)&& StringUtils.isNotBlank(lxProductId)) {
                productColorMap.put(lxProductId, colorId);
            }
        }
        return productColorMap;
    }

}
