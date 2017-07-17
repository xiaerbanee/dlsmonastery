package net.myspring.tool.modules.oppo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.*;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.dataSource.annotation.FutureDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.domain.DistrictEntity;
import net.myspring.tool.common.domain.EmployeeEntity;
import net.myspring.tool.common.domain.OfficeEntity;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.common.dto.CustomerDto;
import net.myspring.tool.modules.oppo.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by guolm on 2017/6/10.
 */
@Service
@LocalDataSource
public class OppoPushSerivce {

    @Autowired
    private CustomerClient customerClient;
    @Autowired
    private DistrictClient districtClient;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private EmployeeClient employeeClient;
    @Autowired
    private CompanyConfigClient companyConfigClient;
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
    @Autowired
    private OppoCustomerImeiStockRepository oppoCustomerImeiStockRepository;
    @Autowired
    private OppoCustomerSaleRepository oppoCustomerSaleRepository;
    @Autowired
    private OppoCustomerSaleImeiRepository oppoCustomerSaleImeiRepository;
    @Autowired
    private OppoCustomerSaleCountRepository oppoCustomerSaleCountRepository;
    @Autowired
    private OppoCustomerAfterSaleImeiRepository oppoCustomerAfterSaleImeiRepository;
    @Autowired
    private OppoCustomerDemoPhoneRepository oppoCustomerDemoPhoneRepository;


    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static Map<String,String> areaDepotMap=Maps.newHashMap();
    private static Map<String,CustomerDto> customerDtoMap=Maps.newHashMap();

    //上抛oppo门店数据,只上抛二代和渠道门店
    @LocalDataSource
    @Transactional(readOnly = false)
    public List<OppoCustomer> getOppoCustomers(String date) {
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","").split(CharConstant.COMMA)[0];
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
            //一代库不上抛
            if(StringUtils.isBlank(depotId)){
                continue;
            }
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
                oppoCustomer.setDealertype(StringUtils.isEmpty(customerDto.getSalePointType())?"":customerDto.getSalePointType());
                oppoCustomer.setDealergrade("");
                oppoCustomer.setDealertel(StringUtils.isEmpty(customerDto.getMobilePhone())?"":customerDto.getMobilePhone());
                oppoCustomer.setCitytype(StringUtils.isEmpty(customerDto.getAreaType())?"":customerDto.getAreaType());
                oppoCustomer.setBussinesscenter(StringUtils.isEmpty(customerDto.getBussinessCenterName())?"":customerDto.getBussinessCenterName());
                oppoCustomer.setChainname(StringUtils.isEmpty(customerDto.getChainType())?"":customerDto.getChainType());
                oppoCustomer.setSaletype("");
                oppoCustomer.setDoorhead(StringUtils.isNotBlank(customerDto.getDoorHead())? "1" : "0");
                oppoCustomer.setEnabledate(StringUtils.isEmpty(customerDto.getEnableDate())?"":customerDto.getEnableDate());
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
                oppoCustomer.setVillage(StringUtils.isEmpty(customerDto.getTownName())?"":customerDto.getTownName());
                oppoCustomer.setDealerarea(StringUtils.isEmpty(customerDto.getShopArea())?"":customerDto.getShopArea());
                oppoCustomer.setFramenum(StringUtils.isBlank(customerDto.getFrameNum())?"":customerDto.getFrameNum());
                oppoCustomer.setDeskdoublenum(StringUtils.isBlank(customerDto.getFrameNum())?"":customerDto.getFrameNum());
                oppoCustomer.setDesksinglenum(StringUtils.isBlank(customerDto.getDeskSingleNum())?"":customerDto.getDeskSingleNum());
                oppoCustomer.setCabinetnum(StringUtils.isBlank(customerDto.getCabinetNum())?"":customerDto.getCabinetNum());
                oppoCustomer.setCreatedDate(LocalDateUtils.parse(date));
                oppoCustomersMap.put(oppoCustomer.getCustomerid(), oppoCustomer);
            }
        }
        for (String key : oppoCustomersMap.keySet()) {
            oppoCustomers.add(oppoCustomersMap.get(key));
        }
        logger.info("同步经销商数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomer> oppoCustomerList=oppoCustomerRepository.findByDate(date,dateEnd);
        oppoCustomerRepository.delete(oppoCustomerList);
        oppoCustomerRepository.save(oppoCustomers);
        logger.info("同步经销商数据结束");
        return oppoCustomers;
    }

    //上抛运营商属性
    @LocalDataSource
    @Transactional(readOnly = false)
    public List<OppoCustomerOperatortype> getOppoCustomerOperatortype(String date) {
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
                        oppoCustomerOperatortype.setCreatedDate(LocalDateUtils.parse(date));
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
                                oppoCustomerOperatortype.setCreatedDate(LocalDateUtils.parse(date));
                                oppoCustomerOperatortypeList.add(oppoCustomerOperatortype);
                            }
                        }
                    }
                }
            }
        }
        logger.info("同步运营商数据开始");
        if(CollectionUtil.isNotEmpty(oppoCustomerOperatortypeList)){
            String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
            List<OppoCustomerOperatortype> customerOperatortypes=oppoCustomerOperatortypeRepository.findByDate(date,dateEnd);
            oppoCustomerOperatortypeRepository.delete(customerOperatortypes);
            oppoCustomerOperatortypeRepository.save(oppoCustomerOperatortypeList);
        }
        logger.info("同步运营商数据结束");
        return oppoCustomerOperatortypeList;
    }

    //发货退货调拨数据上抛
    @LocalDataSource
    @Transactional(readOnly = false)
    public List<OppoCustomerAllot>  getOppoCustomerAllot(List<OppoCustomerAllot> oppoCustomerAllots,String date){
        initAreaDepotMap();
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
                        allot.setCreatedDate(LocalDate.now());
                        allot.setQty(0);
                        oppoCustomerAllotMap.put(key, allot);
                    }
                    oppoCustomerAllotMap.get(key).setQty(oppoCustomerAllotMap.get(key).getQty() + oppoCustomerAllot.getQty());
                }
            }
        }
        List<OppoCustomerAllot>  oppoCustomerAllotList=new ArrayList<OppoCustomerAllot>(oppoCustomerAllotMap.values());
        logger.info("同步发货退货数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerAllot> customerAllots=oppoCustomerAllotRepository.findByDate(date,dateEnd);
        oppoCustomerAllotRepository.delete(customerAllots);
        oppoCustomerAllotRepository.save(oppoCustomerAllotList);
        logger.info("同步发货退货数据结束");
        return  oppoCustomerAllotList;
    }

    //上抛一代二代库存数据,不包括门店数据
    @LocalDataSource
    @Transactional(readOnly = false)
    public List<OppoCustomerStock> getOppoCustomerStock(List<OppoCustomerStock> oppoCustomerStocks,String date) {
        initAreaDepotMap();
        Map<String, OppoCustomerStock> oppoCustomerStockHashMap = Maps.newHashMap();
        Map<String, String> productColorMap = getProductColorMap();
        for(OppoCustomerStock oppoCustomerStock:oppoCustomerStocks){
            String customerId=getDepotId(customerDtoMap.get(oppoCustomerStock.getCustomerid()));
            String colorId=productColorMap.get(oppoCustomerStock.getProductcode());
            String jointLevel=customerDtoMap.get(oppoCustomerStock.getCustomerid()).getJointLeavel();
            if(StringUtils.isNotEmpty(jointLevel)&&StringUtils.isNotEmpty(colorId)){
                String key=customerId+ CharConstant.UNDER_LINE+productColorMap.get(oppoCustomerStock.getProductcode());
                if(!oppoCustomerStockHashMap.containsKey(key)){
                    OppoCustomerStock customerStock = new OppoCustomerStock();
                    customerStock.setCustomerid(customerId);
                    customerStock.setDate(oppoCustomerStock.getDate());
                    customerStock.setProductcode(productColorMap.get(oppoCustomerStock.getProductcode()));
                    customerStock.setQty(0);
                    customerStock.setCreatedDate(LocalDate.now());
                    oppoCustomerStockHashMap.put(key, customerStock);
                }
                oppoCustomerStockHashMap.get(key).setQty(oppoCustomerStockHashMap.get(key).getQty() + oppoCustomerStock.getQty());
            }
        }
        List<OppoCustomerStock>  oppoCustomerStockList=new ArrayList<OppoCustomerStock>(oppoCustomerStockHashMap.values());
        logger.info("同步库存数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerStock> customerStocks=oppoCustomerStockRepository.findByDate(date,dateEnd);
        oppoCustomerStockRepository.delete(customerStocks);
        oppoCustomerStockRepository.save(oppoCustomerStockList);
        logger.info("同步库存数据结束");
        return oppoCustomerStockList;
    }

    //获取渠道串码收货数据
    @LocalDataSource
    @Transactional(readOnly = false)
    public List<OppoCustomerImeiStock> getOppoCustomerImeiStock(List<OppoCustomerImeiStock> oppoCustomerImeiStockList,String date) {
        initAreaDepotMap();
        Map<String, String> productColorMap = getProductColorMap();
        for(OppoCustomerImeiStock oppoCustomerImeiStock:oppoCustomerImeiStockList){
            oppoCustomerImeiStock.setCustomerid(oppoCustomerImeiStock.getCustomerid());
            oppoCustomerImeiStock.setProductcode(productColorMap.get(oppoCustomerImeiStock.getProductcode()));
            oppoCustomerImeiStock.setImei(oppoCustomerImeiStock.getImei());
            oppoCustomerImeiStock.setDate(oppoCustomerImeiStock.getDate());
            oppoCustomerImeiStock.setTransType(oppoCustomerImeiStock.getTransType());
            oppoCustomerImeiStock.setCreatedDate(LocalDate.now());
        }
        logger.info("同步渠道串码收货数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerImeiStock> oppoCustomerImeiStocks=oppoCustomerImeiStockRepository.findByDate(date,dateEnd);
        oppoCustomerImeiStockRepository.delete(oppoCustomerImeiStocks);
        oppoCustomerImeiStockRepository.save(oppoCustomerImeiStockList);
        logger.info("同步渠道串码收货数据结束");
        return oppoCustomerImeiStockList;
    }

    //获取店核销总数据
    @LocalDataSource
    @Transactional(readOnly = false)
    public List<OppoCustomerSale> getOppoCustomerSales(List<OppoCustomerSale> oppoCustomerSales,String date) {
        initAreaDepotMap();
        for(OppoCustomerSale oppoCustomerSale:oppoCustomerSales){
            oppoCustomerSale.setCreatedDate(LocalDate.now());
        }
        logger.info("同步店核销总数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerSale> customerSales=oppoCustomerSaleRepository.findByDate(date,dateEnd);
        oppoCustomerSaleRepository.delete(customerSales);
        oppoCustomerSaleRepository.save(oppoCustomerSales);
        logger.info("同步店核销总数据结束");
        return oppoCustomerSales;
    }

    //	门店销售明细数据
    @LocalDataSource
    @Transactional(readOnly = false)
    public List<OppoCustomerSaleImei> getOppoCustomerSaleImes(List<OppoCustomerSaleImei> oppoCustomerSaleImes,String date) {
        initAreaDepotMap();
        List<DistrictEntity>  districtList=districtClient.findDistrictList();
        Map<String,DistrictEntity>  districtMap=Maps.newHashMap();
        for(DistrictEntity districtEntity:districtList){
            districtMap.put(districtEntity.getId(),districtEntity);
        }
        Map<String,EmployeeEntity>  employeeMap=Maps.newHashMap();
        List<EmployeeEntity> employeeList=employeeClient.findAll();
        for(EmployeeEntity employeeEntity:employeeList){
            employeeMap.put(employeeEntity.getId(),employeeEntity);
        }
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","").split(CharConstant.COMMA)[0];
        String agentName=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"","");
        for(OppoCustomerSaleImei oppoCustomerSaleIme:oppoCustomerSaleImes){
            if(StringUtils.isNotBlank(oppoCustomerSaleIme.getSalepromoter())&&employeeMap.get(oppoCustomerSaleIme.getSalepromoter())!=null){
                oppoCustomerSaleIme.setSalepromoter(employeeMap.get(oppoCustomerSaleIme.getSalepromoter()).getName());
            }
            if(StringUtils.isBlank(oppoCustomerSaleIme.getCustname())){
                oppoCustomerSaleIme.setCustname("");
            }
            if(StringUtils.isBlank(oppoCustomerSaleIme.getCustmobile())){
                oppoCustomerSaleIme.setCustmobile("");
            }
            if(StringUtils.isBlank(oppoCustomerSaleIme.getCustsex())){
                oppoCustomerSaleIme.setCustsex("");
            }
            oppoCustomerSaleIme.setAgentcode(agentCode);
            oppoCustomerSaleIme.setAgentname(agentName);
            String districtId=oppoCustomerSaleIme.getProvince();
            String province="";
            String city="";
            if(StringUtils.isNotBlank(districtId)){
                province=districtMap.get(districtId).getProvince();
                city=districtMap.get(districtId).getCity();
            }
            oppoCustomerSaleIme.setProvince(province);
            oppoCustomerSaleIme.setCity(city);
            oppoCustomerSaleIme.setCreatedDate(LocalDate.now());
        }
        logger.info("同步销售明细数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerSaleImei> oppoCustomerSaleImeis=oppoCustomerSaleImeiRepository.findByDate(date,dateEnd);
        oppoCustomerSaleImeiRepository.delete(oppoCustomerSaleImeis);
        oppoCustomerSaleImeiRepository.save(oppoCustomerSaleImes);
        logger.info("同步销售明细数据结束");
        return oppoCustomerSaleImes;
    }

    //门店销售数据汇总
    @LocalDataSource
    @Transactional(readOnly = false)
    public List<OppoCustomerSaleCount> getOppoCustomerSaleCounts(List<OppoCustomerSaleCount> oppoCustomerSaleCounts,String date) {
        initAreaDepotMap();
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","").split(CharConstant.COMMA)[0];
        Map<String, String> productColorMap = getProductColorMap();
        for(OppoCustomerSaleCount oppoCustomerSaleCount:oppoCustomerSaleCounts) {
            String colorId=productColorMap.get(oppoCustomerSaleCount.getProductCode());
            oppoCustomerSaleCount.setAgentCode(agentCode);
            oppoCustomerSaleCount.setSaleTime(oppoCustomerSaleCount.getSaleTime());
            oppoCustomerSaleCount.setProductCode(colorId);
            oppoCustomerSaleCount.setCreatedDate(LocalDate.now());
        }
        logger.info("同步销售数据汇总开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerSaleCount> customerSaleCounts=oppoCustomerSaleCountRepository.findByDate(date,dateEnd);
        oppoCustomerSaleCountRepository.delete(customerSaleCounts);
        oppoCustomerSaleCountRepository.save(oppoCustomerSaleCounts);
        logger.info("同步销售数据汇总结束");
        return oppoCustomerSaleCounts;
    }


    //门店售后退货汇总
    @LocalDataSource
    @Transactional(readOnly = false)
    public List<OppoCustomerAfterSaleImei> getOppoCustomerAfterSaleImeis(List<OppoCustomerAfterSaleImei>  oppoCustomerAfterSaleImeis,String date) {
        initAreaDepotMap();
        Map<String, String> productColorMap = getProductColorMap();
        for(OppoCustomerAfterSaleImei oppoCustomerAfterSaleImei:oppoCustomerAfterSaleImeis){
            oppoCustomerAfterSaleImei.setProductCode(productColorMap.get(oppoCustomerAfterSaleImei.getProductCode()));
            oppoCustomerAfterSaleImei.setDate(oppoCustomerAfterSaleImei.getDate());
            oppoCustomerAfterSaleImei.setCreatedDate(LocalDate.now());
        }
        logger.info("同步门店售后退货汇总开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerAfterSaleImei> customerAfterSaleImeis=oppoCustomerAfterSaleImeiRepository.findByDate(date,dateEnd);
        oppoCustomerAfterSaleImeiRepository.delete(customerAfterSaleImeis);
        oppoCustomerAfterSaleImeiRepository.save(oppoCustomerAfterSaleImeis);
        logger.info("同步门店售后退货汇总结束");
        return oppoCustomerAfterSaleImeis;
    }

    //门店演示机汇总数据
    @LocalDataSource
    @Transactional(readOnly = false)
    public List<OppoCustomerDemoPhone> getOppoCustomerDemoPhone(List<OppoCustomerDemoPhone> oppoCustomerDemoPhones,String date) {
        initAreaDepotMap();
        Map<String, String> productColorMap = getProductColorMap();
        for(OppoCustomerDemoPhone oppoCustomerDemoPhone:oppoCustomerDemoPhones){
            oppoCustomerDemoPhone.setCustomerid(oppoCustomerDemoPhone.getCustomerid());
            oppoCustomerDemoPhone.setDate(oppoCustomerDemoPhone.getDate());
            oppoCustomerDemoPhone.setImei(oppoCustomerDemoPhone.getImei());
            oppoCustomerDemoPhone.setProductCode(productColorMap.get(oppoCustomerDemoPhone.getProductCode()));
            oppoCustomerDemoPhone.setCreatedDate(LocalDate.now());
        }
        logger.info("同步门店演示机汇总数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerDemoPhone> customerDemoPhones=oppoCustomerDemoPhoneRepository.findByDate(date,dateEnd);
        oppoCustomerDemoPhoneRepository.delete(customerDemoPhones);
        oppoCustomerDemoPhoneRepository.save(oppoCustomerDemoPhones);
        logger.info("同步门店演示机汇总数据结束");
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


    @FutureDataSource
    @Transactional(readOnly = true)
    public List<OppoCustomerAllot> getFutureOppoCustomerAllot(String date){
        String companyName=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"","");
        DbContextHolder.get().setCompanyName(companyName);
        if(StringUtils.isEmpty(date)){
            date=LocalDateUtils.format(LocalDate.now());
        }
        String dateStart= date;
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerAllot> oppoCustomerAllots=oppoCustomerAllotRepository.findAll(dateStart,dateEnd);
        return oppoCustomerAllots;
    }

    @FutureDataSource
    @Transactional(readOnly = true)
    public List<OppoCustomerStock> getFutureOppoCustomerStock(String date){
        String companyName=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"","");
        DbContextHolder.get().setCompanyName(companyName);
        if(StringUtils.isEmpty(date)){
            date=LocalDateUtils.format(LocalDate.now());
        }
        String dateStart= LocalDateUtils.format(LocalDateUtils.parse(date).plusMonths(-12));
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerStock> oppoCustomerStocks=oppoCustomerStockRepository.findAll(dateStart,dateEnd,date);
        return oppoCustomerStocks;
    }

    @FutureDataSource
    @Transactional(readOnly = true)
    public List<OppoCustomerImeiStock>  getFutureOppoCustomerImeiStock(String date){
        String companyName=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"","");
        DbContextHolder.get().setCompanyName(companyName);
        if(StringUtils.isEmpty(date)){
            date=LocalDateUtils.format(LocalDate.now());
        }
        String dateStart= date;
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerImeiStock>  oppoCustomerImeiStocks=oppoCustomerImeiStockRepository.findAll(dateStart,dateEnd);
        return oppoCustomerImeiStocks;
    }

    @FutureDataSource
    @Transactional(readOnly = true)
    public List<OppoCustomerSale> getFutureOppoCustomerSale(String date){
        String companyName=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"","");
        DbContextHolder.get().setCompanyName(companyName);
        if(StringUtils.isEmpty(date)){
            date=LocalDateUtils.format(LocalDate.now());
        }
        String dateStart= date;
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerSale>  oppoCustomerSales=oppoCustomerSaleRepository.findAll(dateStart,dateEnd);
        return oppoCustomerSales;
    }

    @FutureDataSource
    @Transactional(readOnly = true)
    public List<OppoCustomerSaleImei> getFutureOppoCustomerSaleImeis(String date){
        String companyName=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"","");
        DbContextHolder.get().setCompanyName(companyName);
        if(StringUtils.isEmpty(date)){
            date=LocalDateUtils.format(LocalDate.now());
        }
        String dateStart= date;
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerSaleImei> oppoCustomerSaleImeis=oppoCustomerSaleImeiRepository.findAll(dateStart,dateEnd);
        return oppoCustomerSaleImeis;
    }

    @FutureDataSource
    @Transactional(readOnly = true)
    public List<OppoCustomerSaleCount> getFutureOppoCustomerSaleCounts(String date){
        String companyName=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"","");
        DbContextHolder.get().setCompanyName(companyName);
        if(StringUtils.isEmpty(date)){
            date=LocalDateUtils.format(LocalDate.now());
        }
        String dateStart= date;
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoCustomerSaleCountRepository.findAll(dateStart,dateEnd);
        return oppoCustomerSaleCounts;
    }

    @FutureDataSource
    @Transactional(readOnly = true)
    public List<OppoCustomerAfterSaleImei> getFutureOppoCustomerAfterSaleImeis(String date){
        String companyName=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"","");
        DbContextHolder.get().setCompanyName(companyName);
        if(StringUtils.isEmpty(date)){
            date=LocalDateUtils.format(LocalDate.now());
        }
        String dateStart= date;
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=oppoCustomerAfterSaleImeiRepository.findAll(dateStart,dateEnd);
        return oppoCustomerAfterSaleImeis;
    }

    @FutureDataSource
    @Transactional(readOnly = true)
    public  List<OppoCustomerDemoPhone> getFutureOppoCustomerDemoPhone(String date){
        String companyName=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"","");
        DbContextHolder.get().setCompanyName(companyName);
        if(StringUtils.isEmpty(date)){
            date=LocalDateUtils.format(LocalDate.now());
        }
        String dateStart= date;
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoCustomerDemoPhoneRepository.findAll(dateStart,dateEnd);
        return oppoCustomerDemoPhones;
    }

    @LocalDataSource
    public List<OppoCustomer>  getOppoCustomersByDate(String createdDate){
        String dateStart=LocalDateUtils.format(LocalDateUtils.parse(createdDate));
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(createdDate).plusDays(1));
        List<OppoCustomer> oppoCustomers=oppoCustomerRepository.findByDate(dateStart,dateEnd);
        return oppoCustomers;
    }

    @LocalDataSource
    public List<OppoCustomerOperatortype>  getOppoCustomerOperatortypesByDate(String createdDate){
        String dateStart=LocalDateUtils.format(LocalDateUtils.parse(createdDate));
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(createdDate).plusDays(1));
        List<OppoCustomerOperatortype> oppoCustomerOperatortypes=oppoCustomerOperatortypeRepository.findByDate(dateStart,dateEnd);
        return oppoCustomerOperatortypes;
    }

    @LocalDataSource
    public List<OppoCustomerAllot>  getOppoCustomerAllotsByDate(String dateStart,String dateEnd){
        List<OppoCustomerAllot> oppoCustomerAllots=oppoCustomerAllotRepository.findByDate(dateStart,dateEnd);
        return oppoCustomerAllots;
    }


    @LocalDataSource
    public List<OppoCustomerStock>  getOppoCustomerStocksByDate(String dateStart,String dateEnd){
        List<OppoCustomerStock> oppoCustomerStocks=oppoCustomerStockRepository.findByDate(dateStart,dateEnd);
        return oppoCustomerStocks;
    }

    @LocalDataSource
    public List<OppoCustomerImeiStock>  getOppoCustomerImeiStocksByDate(String dateStart,String dateEnd){
        List<OppoCustomerImeiStock> oppoCustomerImeiStocks=oppoCustomerImeiStockRepository.findByDate(dateStart,dateEnd);
        return oppoCustomerImeiStocks;
    }

    @LocalDataSource
    public List<OppoCustomerSale>  getOppoCustomerSalesByDate(String dateStart,String dateEnd){
        List<OppoCustomerSale> oppoCustomerSales=oppoCustomerSaleRepository.findByDate(dateStart,dateEnd);
        return oppoCustomerSales;
    }

    @LocalDataSource
    public List<OppoCustomerSaleImei>  getOppoCustomerSaleImeisByDate(String dateStart,String dateEnd){
        List<OppoCustomerSaleImei> oppoCustomerSaleImeis=oppoCustomerSaleImeiRepository.findByDate(dateStart,dateEnd);
        return oppoCustomerSaleImeis;
    }

    @LocalDataSource
    public List<OppoCustomerSaleCount>  getOppoCustomerSaleCountsByDate(String dateStart,String dateEnd){
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoCustomerSaleCountRepository.findByDate(dateStart,dateEnd);
        return oppoCustomerSaleCounts;
    }

    @LocalDataSource
    public List<OppoCustomerAfterSaleImei>  getOppoCustomerAfterSaleImeisByDate(String dateStart,String dateEnd){
        List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=oppoCustomerAfterSaleImeiRepository.findByDate(dateStart,dateEnd);
        return oppoCustomerAfterSaleImeis;
    }

    @LocalDataSource
    public List<OppoCustomerDemoPhone>  getOppoCustomerDemoPhonesByDate(String dateStart,String dateEnd){
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoCustomerDemoPhoneRepository.findByDate(dateStart,dateEnd);
        return oppoCustomerDemoPhones;
    }
}
