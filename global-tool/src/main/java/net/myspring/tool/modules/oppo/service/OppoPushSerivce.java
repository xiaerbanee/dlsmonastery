package net.myspring.tool.modules.oppo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.tool.common.client.DistrictClient;
import net.myspring.tool.common.client.EmployeeClient;
import net.myspring.tool.common.client.OfficeClient;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.utils.CacheUtils;
import net.myspring.tool.modules.future.domain.Office;
import net.myspring.tool.modules.future.dto.AccountDepotDto;
import net.myspring.tool.modules.future.dto.CustomerDto;
import net.myspring.tool.modules.future.dto.DistrictDto;
import net.myspring.tool.modules.future.dto.EmployeeDto;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.modules.oppo.dto.OppoPushDto;
import net.myspring.tool.modules.oppo.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.MD5Utils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by guolm on 2017/6/10.
 */
@Service
@LocalDataSource
@Transactional(readOnly = true)
public class OppoPushSerivce {

    @Autowired
    private DistrictClient districtClient;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private EmployeeClient employeeClient;
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
    @Autowired
    private OppoPushEmployeeRepository oppoPushEmployeeRepository;
    @Autowired
    private OppoPushCustomerEmployeeRepository oppoPushCustomerEmployeeRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private RedisTemplate redisTemplate;


    protected Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String,String> areaDepotMap=Maps.newHashMap();
    private Map<String,CustomerDto> customerDtoMap=Maps.newHashMap();

    public String getCompanyName(String key,String ...args){
        StringBuffer params=new StringBuffer("");
        for(String arg:args){
            params=params.append(arg);
        }
        String jxoppoKey= MD5Utils.encode("M13AMB" + params);
        String wzoppoKey=MD5Utils.encode("M03FMB" + params);
        if(jxoppoKey.equals(key)){
            return CompanyNameEnum.JXOPPO.name();
        }else if(wzoppoKey.equals(key)){
            return CompanyNameEnum.WZOPPO.name();
        }else{
            return null;
        }
    }


    @LocalDataSource
    @Transactional
    public void pushToLocal(OppoPushDto oppoPushDto,String companyName) {
        areaDepotMap = oppoPushDto.getAreaDepotMap();
        customerDtoMap = oppoPushDto.getCustomerDtoMap();
        //上抛oppo门店数据,只上抛二代和渠道门店
        pushOppoCustomers(oppoPushDto.getCustomerDtos(),oppoPushDto.getDate(),companyName);
        //上抛运营商属性
        pushOppoCustomerOperatortype(oppoPushDto.getCustomerDtos(),oppoPushDto.getDate());
        //发货退货调拨数据上抛
        pushOppoCustomerAllot(oppoPushDto.getOppoCustomerAllots(),oppoPushDto.getDate());
        //上抛一代二代库存数据,不包括门店数据
        pushOppoCustomerStock(oppoPushDto.getOppoCustomerStocks(),oppoPushDto.getDate());
        //获取渠道串码收货数据
        pushOppoCustomerImeiStock(oppoPushDto.getOppoCustomerImeiStocks(),oppoPushDto.getDate());
        //获取店核销总数据
        pushOppoCustomerSales(oppoPushDto.getOppoCustomerSales(),oppoPushDto.getDate());
        //门店销售明细数据
        pushOppoCustomerSaleImes(oppoPushDto.getOppoCustomerSaleImeis(),oppoPushDto.getDate(),companyName);
        //门店销售数据汇总
        pushOppoCustomerSaleCounts(oppoPushDto.getOppoCustomerSaleCounts(),oppoPushDto.getDate());
        //门店售后退货汇总
        pushOppoCustomerAfterSaleImeis(oppoPushDto.getOppoCustomerAfterSaleImeis(),oppoPushDto.getDate());
        //门店演示机汇总数据
        pushOppoCustomerDemoPhone(oppoPushDto.getOppoCustomerDemoPhones(),oppoPushDto.getDate());
        //员工基本信息数据
        pushOppoEmployeeInfo(companyName);
        //员工绑定门店信息
        pushOppoCustomerEmployee(oppoPushDto.getAccountDepotDtos());
    }


    //上抛oppo门店数据,只上抛二代和渠道门店
    @LocalDataSource
    @Transactional
    public List<OppoCustomer> pushOppoCustomers(List<CustomerDto> customerDtoList,String date,String companyName) {
        String agentCode= CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue().split(CharConstant.COMMA)[0];
        List<OppoCustomer> oppoCustomers = Lists.newArrayList();
        Map<String, OppoCustomer> oppoCustomersMap = Maps.newHashMap();
        List<DistrictDto>  districtList=districtClient.findDistrictList();
        Map<String,DistrictDto>  districtMap=Maps.newHashMap();
        for(DistrictDto districtDto:districtList){
            districtMap.put(districtDto.getId(),districtDto);
        }
        List<Office> offices=officeClient.findAll(companyName);
        Map<String,Office>  officeMap=Maps.newHashMap();
        for(Office office:offices){
            officeMap.put(office.getId(),office);
        }
        for(CustomerDto customerDto:customerDtoList){
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
                oppoCustomer.setCompanyName(companyName);
                oppoCustomersMap.put(oppoCustomer.getCustomerid(), oppoCustomer);
            }
        }
        for (String key : oppoCustomersMap.keySet()) {
            oppoCustomers.add(oppoCustomersMap.get(key));
        }
        logger.info("同步经销商数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        oppoCustomerRepository.deleteByCompanyNameAndDate(companyName,date,dateEnd);
        oppoCustomerRepository.save(oppoCustomers);
        logger.info("同步经销商数据结束");
        return oppoCustomers;
    }

    //上抛运营商属性
    @LocalDataSource
    @Transactional
    public List<OppoCustomerOperatortype> pushOppoCustomerOperatortype(List<CustomerDto> customerDtosList,String date) {
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoCustomerOperatortype> oppoCustomerOperatortypeList = Lists.newArrayList();
        cacheUtils.initCacheInput(customerDtosList);
        for(CustomerDto customerDto:customerDtosList){
            if(isShop(customerDto)){
                if("运营商营业厅".equals(customerDto.getSalePointType())){
                    if(StringUtils.isNotBlank((customerDto.getCarrierType()))){
                        OppoCustomerOperatortype oppoCustomerOperatortype=new OppoCustomerOperatortype();
                        oppoCustomerOperatortype.setCustomerid(getDepotId(customerDto));
                        oppoCustomerOperatortype.setCustomername(getAreaDepotName(customerDto));
                        oppoCustomerOperatortype.setOperatortype(customerDto.getCarrierType());
                        oppoCustomerOperatortype.setCreatedDate(LocalDateUtils.parse(date));
                        oppoCustomerOperatortype.setCompanyName(companyName);
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
                                oppoCustomerOperatortype.setCompanyName(companyName);
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
            oppoCustomerOperatortypeRepository.deleteByCompanyNameAndDate(companyName,date,dateEnd);
            oppoCustomerOperatortypeRepository.save(oppoCustomerOperatortypeList);
        }
        logger.info("同步运营商数据结束");
        return oppoCustomerOperatortypeList;
    }

    //发货退货调拨数据上抛
    @LocalDataSource
    @Transactional
    public List<OppoCustomerAllot>  pushOppoCustomerAllot(List<OppoCustomerAllot> oppoCustomerAllots,String date){
        String companyName = DbContextHolder.get().getCompanyName();
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
                        allot.setCompanyName(companyName);
                        oppoCustomerAllotMap.put(key, allot);
                    }
                    oppoCustomerAllotMap.get(key).setQty(oppoCustomerAllotMap.get(key).getQty() + oppoCustomerAllot.getQty());
                }
            }
        }
        List<OppoCustomerAllot>  oppoCustomerAllotList=new ArrayList<OppoCustomerAllot>(oppoCustomerAllotMap.values());
        logger.info("同步发货退货数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        if (CollectionUtil.isNotEmpty(oppoCustomerAllotList)){
            oppoCustomerAllotRepository.deleteByCompanyNameAndDate(companyName,date,dateEnd);
            oppoCustomerAllotRepository.save(oppoCustomerAllotList);
        }
        logger.info("同步发货退货数据结束");
        return  oppoCustomerAllotList;
    }

    //上抛一代二代库存数据,不包括门店数据
    @LocalDataSource
    @Transactional
    public List<OppoCustomerStock> pushOppoCustomerStock(List<OppoCustomerStock> oppoCustomerStocks,String date) {
        String companyName = DbContextHolder.get().getCompanyName();
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
                    customerStock.setCompanyName(companyName);
                    customerStock.setCreatedDate(LocalDate.now());
                    oppoCustomerStockHashMap.put(key, customerStock);
                }
                oppoCustomerStockHashMap.get(key).setQty(oppoCustomerStockHashMap.get(key).getQty() + oppoCustomerStock.getQty());
            }
        }
        List<OppoCustomerStock>  oppoCustomerStockList=new ArrayList<OppoCustomerStock>(oppoCustomerStockHashMap.values());
        logger.info("同步库存数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        if (CollectionUtil.isNotEmpty(oppoCustomerStockList)){
            oppoCustomerStockRepository.deleteByCompanyNameAndDate(companyName,date,dateEnd);
            oppoCustomerStockRepository.save(oppoCustomerStockList);
        }
        logger.info("同步库存数据结束");
        return oppoCustomerStockList;
    }

    //获取渠道串码收货数据
    @LocalDataSource
    @Transactional
    public List<OppoCustomerImeiStock> pushOppoCustomerImeiStock(List<OppoCustomerImeiStock> oppoCustomerImeiStockList,String date) {
        String companyName = DbContextHolder.get().getCompanyName();
        Map<String, String> productColorMap = getProductColorMap();
        List<OppoCustomerImeiStock> oppoCustomerImeiStocks = Lists.newArrayList();
        for(OppoCustomerImeiStock oppoCustomerImeiStock:oppoCustomerImeiStockList){
            oppoCustomerImeiStock.setProductcode(productColorMap.get(oppoCustomerImeiStock.getProductcode()));
            oppoCustomerImeiStock.setCreatedDate(LocalDate.now());
            oppoCustomerImeiStock.setCompanyName(companyName);
            oppoCustomerImeiStocks.add(oppoCustomerImeiStock);
        }
        logger.info("同步渠道串码收货数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        if (CollectionUtil.isNotEmpty(oppoCustomerImeiStocks)){
            oppoCustomerImeiStockRepository.deleteByCompanyNameAndDate(companyName,date,dateEnd);
            oppoCustomerImeiStockRepository.save(oppoCustomerImeiStocks);
        }
        logger.info("同步渠道串码收货数据结束");
        return oppoCustomerImeiStocks;
    }

    //获取店核销总数据
    @LocalDataSource
    @Transactional
    public List<OppoCustomerSale> pushOppoCustomerSales(List<OppoCustomerSale> oppoCustomerSales,String date) {
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoCustomerSale> oppoCustomerSaleList = Lists.newArrayList();
        for(OppoCustomerSale oppoCustomerSale:oppoCustomerSales){
            oppoCustomerSale.setCreatedDate(LocalDate.now());
            oppoCustomerSale.setCompanyName(companyName);
            oppoCustomerSaleList.add(oppoCustomerSale);
        }
        logger.info("同步店核销总数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        if (CollectionUtil.isNotEmpty(oppoCustomerSaleList)){
            oppoCustomerSaleRepository.deleteByCompanyNameAndDate(companyName,date,dateEnd);
            oppoCustomerSaleRepository.save(oppoCustomerSaleList);
        }
        logger.info("同步店核销总数据结束");
        return oppoCustomerSaleList;
    }

    //	门店销售明细数据
    @LocalDataSource
    @Transactional
    public List<OppoCustomerSaleImei> pushOppoCustomerSaleImes(List<OppoCustomerSaleImei> oppoCustomerSaleImes,String date,String companyName) {
        List<DistrictDto>  districtList=districtClient.findDistrictList();
        Map<String,DistrictDto>  districtMap=Maps.newHashMap();
        for(DistrictDto districtDto:districtList){
            districtMap.put(districtDto.getId(),districtDto);
        }
        Map<String,EmployeeDto>  employeeMap=Maps.newHashMap();
        List<EmployeeDto> employeeList=employeeClient.findAll(companyName);
        for(EmployeeDto employeeDto:employeeList){
            employeeMap.put(employeeDto.getId(),employeeDto);
        }
        String agentCode=CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue().split(CharConstant.COMMA)[0];
        String agentName=CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.COMPANY_NAME.name()).getValue();
        List<OppoCustomerSaleImei> oppoCustomerSaleImeiList = Lists.newArrayList();
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
            oppoCustomerSaleIme.setCompanyName(companyName);
            oppoCustomerSaleIme.setCreatedDate(LocalDate.now());
            oppoCustomerSaleImeiList.add(oppoCustomerSaleIme);
        }
        logger.info("同步销售明细数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        if (CollectionUtil.isNotEmpty(oppoCustomerSaleImeiList)){
            oppoCustomerSaleImeiRepository.deleteByCompanyNameAndDate(companyName,date,dateEnd);
            oppoCustomerSaleImeiRepository.save(oppoCustomerSaleImeiList);
        }
        logger.info("同步销售明细数据结束");
        return oppoCustomerSaleImeiList;
    }

    //门店销售数据汇总
    @LocalDataSource
    @Transactional
    public List<OppoCustomerSaleCount> pushOppoCustomerSaleCounts(List<OppoCustomerSaleCount> oppoCustomerSaleCounts,String date) {
        String companyName = DbContextHolder.get().getCompanyName();
        String agentCode=CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue().split(CharConstant.COMMA)[0];
        Map<String, String> productColorMap = getProductColorMap();
        List<OppoCustomerSaleCount> oppoCustomerSaleCountList = Lists.newArrayList();
        for(OppoCustomerSaleCount oppoCustomerSaleCount:oppoCustomerSaleCounts) {
            String colorId=productColorMap.get(oppoCustomerSaleCount.getProductCode());
            oppoCustomerSaleCount.setAgentCode(agentCode);
            oppoCustomerSaleCount.setSaleTime(oppoCustomerSaleCount.getSaleTime());
            oppoCustomerSaleCount.setProductCode(colorId);
            oppoCustomerSaleCount.setCreatedDate(LocalDate.now());
            oppoCustomerSaleCount.setCompanyName(companyName);
            oppoCustomerSaleCountList.add(oppoCustomerSaleCount);
        }
        logger.info("同步销售数据汇总开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        if (CollectionUtil.isNotEmpty(oppoCustomerSaleCountList)){
            oppoCustomerSaleCountRepository.deleteByCompanyNameAndDate(companyName,date,dateEnd);
            oppoCustomerSaleCountRepository.save(oppoCustomerSaleCountList);
        }
        logger.info("同步销售数据汇总结束");
        return oppoCustomerSaleCountList;
    }


    //门店售后退货汇总
    @LocalDataSource
    @Transactional
    public List<OppoCustomerAfterSaleImei> pushOppoCustomerAfterSaleImeis(List<OppoCustomerAfterSaleImei>  oppoCustomerAfterSaleImeis,String date) {
        String companyName = DbContextHolder.get().getCompanyName();
        Map<String, String> productColorMap = getProductColorMap();
        List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeiList = Lists.newArrayList();
        for(OppoCustomerAfterSaleImei oppoCustomerAfterSaleImei:oppoCustomerAfterSaleImeis){
            oppoCustomerAfterSaleImei.setProductCode(productColorMap.get(oppoCustomerAfterSaleImei.getProductCode()));
            oppoCustomerAfterSaleImei.setCreatedDate(LocalDate.now());
            oppoCustomerAfterSaleImei.setCompanyName(companyName);
            oppoCustomerAfterSaleImeiList.add(oppoCustomerAfterSaleImei);
        }
        logger.info("同步门店售后退货汇总开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        if (CollectionUtil.isNotEmpty(oppoCustomerAfterSaleImeiList)){
            oppoCustomerAfterSaleImeiRepository.deleteByCompanyNameAndDate(companyName,date,dateEnd);
            oppoCustomerAfterSaleImeiRepository.save(oppoCustomerAfterSaleImeiList);
        }
        logger.info("同步门店售后退货汇总结束");
        return oppoCustomerAfterSaleImeiList;
    }

    //门店演示机汇总数据
    @LocalDataSource
    @Transactional
    public List<OppoCustomerDemoPhone> pushOppoCustomerDemoPhone(List<OppoCustomerDemoPhone> oppoCustomerDemoPhones,String date) {
        String companyName = DbContextHolder.get().getCompanyName();
        Map<String, String> productColorMap = getProductColorMap();
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhoneList = Lists.newArrayList();
        for(OppoCustomerDemoPhone oppoCustomerDemoPhone:oppoCustomerDemoPhones){
            oppoCustomerDemoPhone.setProductCode(productColorMap.get(oppoCustomerDemoPhone.getProductCode()));
            oppoCustomerDemoPhone.setCompanyName(companyName);
            oppoCustomerDemoPhone.setCreatedDate(LocalDate.now());
            oppoCustomerDemoPhoneList.add(oppoCustomerDemoPhone);
        }
        logger.info("同步门店演示机汇总数据开始");
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        if (CollectionUtil.isNotEmpty(oppoCustomerDemoPhoneList)){
            oppoCustomerDemoPhoneRepository.deleteByCompanyNameAndDate(companyName,date,dateEnd);
            oppoCustomerDemoPhoneRepository.save(oppoCustomerDemoPhoneList);
        }
        logger.info("同步门店演示机汇总数据结束");
        return oppoCustomerDemoPhoneList;
    }

    @LocalDataSource
    @Transactional
    public void pushOppoEmployeeInfo(String companyName){
        List<EmployeeDto> employeeDtoList = employeeClient.findEmployeeInfo(companyName);
        List<OppoPushEmployee> oppoPushEmployeeList = Lists.newArrayList();
        for (EmployeeDto employeeDto : employeeDtoList){
            OppoPushEmployee oppoPushEmployee = new OppoPushEmployee();
            oppoPushEmployee.setUserId(employeeDto.getId());
            oppoPushEmployee.setUserName(employeeDto.getName());
            oppoPushEmployee.setEntryTime(employeeDto.getEntryDate());
            oppoPushEmployee.setSex(employeeDto.getSex());
            oppoPushEmployee.setStatus(employeeDto.getStatus());
            oppoPushEmployee.setMobile(employeeDto.getMobilePhone());
            oppoPushEmployee.setIdcard(employeeDto.getIdcard());
            oppoPushEmployee.setUserPost(employeeDto.getPositionName());
            oppoPushEmployee.setCompanyName(companyName);
            oppoPushEmployeeList.add(oppoPushEmployee);
        }
        logger.info("同步员工信息数据开始:"+ LocalDateTime.now());
        oppoPushEmployeeRepository.deleteAll();
        oppoPushEmployeeRepository.save(oppoPushEmployeeList);
        logger.info("同步员工信息数据结束:"+ LocalDateTime.now());
    }

    @LocalDataSource
    @Transactional
    public void pushOppoCustomerEmployee(List<AccountDepotDto> accountDepotDtoList){
        List<OppoPushCustomerEmployee> oppoPushCustomerEmployeeList = Lists.newArrayList();
        for (AccountDepotDto accountDepotDto : accountDepotDtoList){
            OppoPushCustomerEmployee oppoPushCustomerEmployee = new OppoPushCustomerEmployee();
            oppoPushCustomerEmployee.setCustomerId(accountDepotDto.getDepotId());
            oppoPushCustomerEmployee.setUserId(accountDepotDto.getEmployeeId());
            oppoPushCustomerEmployee.setCompanyName(DbContextHolder.get().getCompanyName());
            oppoPushCustomerEmployeeList.add(oppoPushCustomerEmployee);
        }
        logger.info("员工绑定门店数据上抛开始:"+LocalDateTime.now());
        oppoPushCustomerEmployeeRepository.deleteAll();
        oppoPushCustomerEmployeeRepository.save(oppoPushCustomerEmployeeList);
        logger.info("员工绑定门店数据上抛结束:"+LocalDateTime.now());
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


    @LocalDataSource
    public List<OppoCustomer>  getOppoCustomersByDate(String createdDate,String companyName){
        String dateStart=LocalDateUtils.format(LocalDateUtils.parse(createdDate));
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(createdDate).plusDays(1));
        List<OppoCustomer> oppoCustomers=oppoCustomerRepository.findByDate(companyName,dateStart,dateEnd);
        return oppoCustomers;
    }

    @LocalDataSource
    public List<OppoCustomerOperatortype>  getOppoCustomerOperatortypesByDate(String createdDate){
        String dateStart=LocalDateUtils.format(LocalDateUtils.parse(createdDate));
        String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(createdDate).plusDays(1));
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoCustomerOperatortype> oppoCustomerOperatortypes=oppoCustomerOperatortypeRepository.findByDate(companyName,dateStart,dateEnd);
        return oppoCustomerOperatortypes;
    }

    @LocalDataSource
    public List<OppoCustomerAllot>  getOppoCustomerAllotsByDate(String dateStart,String dateEnd){
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoCustomerAllot> oppoCustomerAllots=oppoCustomerAllotRepository.findByDate(companyName,dateStart,dateEnd);
        return oppoCustomerAllots;
    }


    @LocalDataSource
    public List<OppoCustomerStock>  getOppoCustomerStocksByDate(String dateStart,String dateEnd){
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoCustomerStock> oppoCustomerStocks=oppoCustomerStockRepository.findByDate(companyName,dateStart,dateEnd);
        return oppoCustomerStocks;
    }

    @LocalDataSource
    public List<OppoCustomerImeiStock>  getOppoCustomerImeiStocksByDate(String dateStart,String dateEnd){
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoCustomerImeiStock> oppoCustomerImeiStocks=oppoCustomerImeiStockRepository.findByDate(companyName,dateStart,dateEnd);
        return oppoCustomerImeiStocks;
    }

    @LocalDataSource
    public List<OppoCustomerSale>  getOppoCustomerSalesByDate(String dateStart,String dateEnd){
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoCustomerSale> oppoCustomerSales=oppoCustomerSaleRepository.findByDate(companyName,dateStart,dateEnd);
        return oppoCustomerSales;
    }

    @LocalDataSource
    public List<OppoCustomerSaleImei>  getOppoCustomerSaleImeisByDate(String dateStart,String dateEnd){
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoCustomerSaleImei> oppoCustomerSaleImeis=oppoCustomerSaleImeiRepository.findByDate(companyName,dateStart,dateEnd);
        return oppoCustomerSaleImeis;
    }

    @LocalDataSource
    public List<OppoCustomerSaleCount>  getOppoCustomerSaleCountsByDate(String dateStart,String dateEnd){
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoCustomerSaleCountRepository.findByDate(companyName,dateStart,dateEnd);
        return oppoCustomerSaleCounts;
    }

    @LocalDataSource
    public List<OppoCustomerAfterSaleImei>  getOppoCustomerAfterSaleImeisByDate(String dateStart,String dateEnd){
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=oppoCustomerAfterSaleImeiRepository.findByDate(companyName,dateStart,dateEnd);
        return oppoCustomerAfterSaleImeis;
    }

    @LocalDataSource
    public List<OppoCustomerDemoPhone>  getOppoCustomerDemoPhonesByDate(String dateStart,String dateEnd){
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoCustomerDemoPhoneRepository.findByDate(companyName,dateStart,dateEnd);
        return oppoCustomerDemoPhones;
    }

    @LocalDataSource
    public List<OppoPushEmployee> getOppoPushEmployee(){
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoPushEmployee> oppoPushEmployees = oppoPushEmployeeRepository.findByCompanyName(companyName);
        return oppoPushEmployees;
    }

    @LocalDataSource
    public List<OppoPushCustomerEmployee> getOppoPushCustomerEmployee(){
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoPushCustomerEmployee> oppoPushCustomerEmployees = oppoPushCustomerEmployeeRepository.findByCompanyName(companyName);
        return oppoPushCustomerEmployees;
    }
}
