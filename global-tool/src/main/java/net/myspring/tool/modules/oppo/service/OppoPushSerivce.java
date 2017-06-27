package net.myspring.tool.modules.oppo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.common.client.CustomerClient;
import net.myspring.tool.common.client.DistrictClient;
import net.myspring.tool.modules.oppo.client.OppoClient;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.common.dto.CustomerDto;
import net.myspring.tool.common.dto.DistrictDto;
import net.myspring.tool.modules.oppo.repository.OppoPlantAgentProductSelRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
    private RedisTemplate redisTemplate;
    @Autowired
    private OppoClient oppoClient;
    @Autowired
    private OppoPlantAgentProductSelRepository oppoPlantAgentProductSelRepository;

    private static Map<String,String> areaDepotMap=Maps.newHashMap();
    private static Map<String,CustomerDto> customerDtoMap=Maps.newHashMap();


    //上抛oppo门店数据,只上抛二代和渠道门店
    public List<OppoCustomer> getOppoCustomers() {
        List<OppoCustomer> oppoCustomers = Lists.newArrayList();
        String companyId=("1");
        initAreaDepotMap(companyId);
        Map<String, OppoCustomer> oppoCustomersMap = Maps.newHashMap();
        String customerStr=customerClient.findCustomerDtoList();
        List<CustomerDto> customerDtosList= ObjectMapperUtils.readValue(customerStr, ArrayList.class);
        String districtStr=districtClient.findDistrictList();
        List<DistrictDto>  districtDtoList=ObjectMapperUtils.readValue(districtStr,ArrayList.class);
        Map<String,DistrictDto>  districtDtoMap=Maps.newHashMap();
        for(DistrictDto districtDto:districtDtoList){
            districtDtoMap.put(districtDto.getId(),districtDto);
        }
        for(CustomerDto customerDto:customerDtosList){
            String depotId=getDepotId(customerDto);
            if (!oppoCustomersMap.containsKey(depotId)) {
                OppoCustomer oppoCustomer = new OppoCustomer();
                oppoCustomer.setCustomerid(depotId);
                oppoCustomer.setCustomername(getAreaDepotName(customerDto));
                String agentId = "";
                if(StringUtils.isNotBlank(customerDto.getAreaId())&&!isArea(customerDto)){
                    agentId = StringUtils.isNotBlank(areaDepotMap.get(customerDto.getAreaId())) ? areaDepotMap.get(customerDto.getAreaId()) : "";
                }
                oppoCustomer.setAgentid(agentId);
                List<String> agentCodes = StringUtils.getSplitList(CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(), CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue(), CharConstant.COMMA);
                oppoCustomer.setCompanyid(agentCodes.get(0));
                oppoCustomer.setDealertype(customerDto.getSalePointType());
                oppoCustomer.setDealergrade("");
                oppoCustomer.setDealertel(customerDto.getMobilePhone());
                oppoCustomer.setCitytype(customerDto.getAreaType());
                oppoCustomer.setBussinesscenter(customerDto.getBussinessCenterName());
                oppoCustomer.setChainName(customerDto.getChainType());
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
                if(StringUtils.isNotBlank(customerDto.getDistrictId())&&districtDtoMap.get(customerDto.getDistrictId())!=null){
                    province=districtDtoMap.get(customerDto.getDistrictId()).getProvince();
                    city=districtDtoMap.get(customerDto.getDistrictId()).getCity();
                    county=districtDtoMap.get(customerDto.getDistrictId()).getCounty();
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
                oppoCustomersMap.put(oppoCustomer.getCustomerid(), oppoCustomer);
            }
        }
        for (String key : oppoCustomersMap.keySet()) {
            oppoCustomers.add(oppoCustomersMap.get(key));
        }
        oppoCustomersMap=null;
        return oppoCustomers;
    }

    //上抛运营商属性
    public List<OppoCustomerOperatortype> getOppoCustomerOperatortype() {
        List<OppoCustomerOperatortype> oppoCustomerOperatortypeList = Lists.newArrayList();
        initAreaDepotMap("1");
        String customerStr=customerClient.findCustomerDtoList();
        List<CustomerDto> customerDtosList= ObjectMapperUtils.readValue(customerStr, ArrayList.class);
        for(CustomerDto customerDto:customerDtosList){
            if(isShop(customerDto)){
                if("运营商营业厅".equals(customerDto.getSalePointType())){
                    if(StringUtils.isNotBlank((customerDto.getCarrierType()))){
                        OppoCustomerOperatortype oppoCustomerOperatortype=new OppoCustomerOperatortype();
                        oppoCustomerOperatortype.setCustomerid(getDepotId(customerDto));
                        oppoCustomerOperatortype.setCustomername(getAreaDepotName(customerDto));
                        oppoCustomerOperatortype.setOperatortype(customerDto.getCarrierType());
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
                                oppoCustomerOperatortypeList.add(oppoCustomerOperatortype);
                            }
                        }
                    }
                }
            }
        }
        return oppoCustomerOperatortypeList;
    }

    //发货退货调拨数据上抛
    public List<OppoCustomerAllot>  getOppoCustomerAllot(LocalDate dateStart, LocalDate dateEnd){
        String companyId=("1");
        initAreaDepotMap(companyId);
        String oppoCustomerAllotStr=oppoClient.findOppoCustomerAllots(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        List<OppoCustomerAllot> oppoCustomerAllots=ObjectMapperUtils.readValue(oppoCustomerAllotStr, ArrayList.class);
        Map<String, String> productColorMap = getProductColorMap();
        Map<String, OppoCustomerAllot> oppoCustomerAllotMap = Maps.newHashMap();
        for(OppoCustomerAllot oppoCustomerAllot:oppoCustomerAllots){
            String fromDepotId=getDepotId(customerDtoMap.get(oppoCustomerAllot.getFromCustomerid()));
            String toDepotId=getDepotId(customerDtoMap.get(oppoCustomerAllot.getToCustomerid()));
            if(!fromDepotId.equals(toDepotId)){
                String key = fromDepotId +CharConstant.UNDER_LINE+ toDepotId + CharConstant.UNDER_LINE+ productColorMap.get(oppoCustomerAllot.getProductcode());
                if (!oppoCustomerAllotMap.containsKey(key)) {
                    OppoCustomerAllot allot = new OppoCustomerAllot();
                    allot.setFromCustomerid(fromDepotId);
                    allot.setToCustomerid(toDepotId);
                    allot.setProductcode(productColorMap.get(oppoCustomerAllot.getProductcode()));
                    allot.setDate(dateStart);
                    allot.setQty(0);
                    oppoCustomerAllotMap.put(key, allot);
                }
                oppoCustomerAllotMap.get(key).setQty(oppoCustomerAllotMap.get(key).getQty() + oppoCustomerAllot.getQty());
            }
        }
        List<OppoCustomerAllot>  oppoCustomerAllotList=new ArrayList<OppoCustomerAllot>(oppoCustomerAllotMap.values());
        return  oppoCustomerAllotList;
    }

    //上抛一代二代库存数据,不包括门店数据
    public List<OppoCustomerStock> getOppoCustomerStock(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap(companyId);
        Map<String, OppoCustomerStock> oppoCustomerStockHashMap = Maps.newHashMap();
        Map<String, String> productColorMap = getProductColorMap();
       String oppoCustomerStockStr=oppoClient.findOppoCustomerStocks(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        List<OppoCustomerStock> oppoCustomerStocks= ObjectMapperUtils.readValue(oppoCustomerStockStr, ArrayList.class);
        for(OppoCustomerStock oppoCustomerStock:oppoCustomerStocks){
            String customerId=getDepotId(customerDtoMap.get(oppoCustomerStock.getCustomerid()));
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
        List<OppoCustomerStock>  oppoCustomerStockList=new ArrayList<OppoCustomerStock>(oppoCustomerStockHashMap.values());
        return oppoCustomerStockList;
    }

    //获取渠道串码收货数据
    public List<OppoCustomerImeiStock> getOppoCustomerImeiStock(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap(companyId);
        List<OppoCustomerImeiStock> oppoCustomerImeiStocks = Lists.newArrayList();
        Map<String, String> productColorMap = getProductColorMap();
        String oppoCustomerImeiStockStr=oppoClient.findOppoCustomerImeiStocks(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        List<OppoCustomerImeiStock> oppoCustomerImeiStockList= ObjectMapperUtils.readValue(oppoCustomerImeiStockStr, ArrayList.class);
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
    public List<OppoCustomerSale> getOppoCustomerSales(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap(companyId);
        String oppoCustomerSaleStr=oppoClient.findOppoCustomerSales(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        List<OppoCustomerSale> oppoCustomerSales=ObjectMapperUtils.readValue(oppoCustomerSaleStr,ArrayList.class);
        return oppoCustomerSales;
    }

    //	门店销售明细数据抛转
    public List<OppoCustomerSaleImei> getOppoCustomerSaleImes(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap(companyId);
        String districtStr=districtClient.findDistrictList();
        List<DistrictDto>  districtDtoList=ObjectMapperUtils.readValue(districtStr,ArrayList.class);
        Map<String,DistrictDto>  districtDtoMap=Maps.newHashMap();
        for(DistrictDto districtDto:districtDtoList){
            districtDtoMap.put(districtDto.getId(),districtDto);
        }
        String oppoCustomerSaleImeStr=oppoClient.findOppoCustomerSaleImes(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        List<OppoCustomerSaleImei>    oppoCustomerSaleImes= ObjectMapperUtils.readValue(oppoCustomerSaleImeStr,ArrayList.class);
        for(OppoCustomerSaleImei oppoCustomerSaleIme:oppoCustomerSaleImes){
            oppoCustomerSaleIme.setAgentcode("M13AMB");
            oppoCustomerSaleIme.setAgentname("JXOPPO");
        }
        return oppoCustomerSaleImes;
    }

    //门店销售数据汇总
    public List<OppoCustomerSaleCount> getOppoCustomerSaleCounts(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap(companyId);
        Map<String, String> productColorMap = getProductColorMap();
        String oppoCustomerSaleCountStr=oppoClient.findOppoCustomerSaleCounts(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts= ObjectMapperUtils.readValue(oppoCustomerSaleCountStr,ArrayList.class);
        for(OppoCustomerSaleCount oppoCustomerSaleCount:oppoCustomerSaleCounts) {
            String colorId=productColorMap.get(oppoCustomerSaleCount.getProductCode());
            oppoCustomerSaleCount.setProductCode(colorId);
            oppoCustomerSaleCount.setAgentCode("M13AMB");
        }
        return oppoCustomerSaleCounts;
    }


    //门店售后退货汇总
    public List<OppoCustomerAfterSaleImei> getOppoCustomerAfterSaleImeis(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap(companyId);
        Map<String, String> productColorMap = getProductColorMap();
        String oppoCustomerAfterSaleImeiStr=oppoClient.findOppoCustomerAfterSaleImes(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        List<OppoCustomerAfterSaleImei>  oppoCustomerAfterSaleImeis= ObjectMapperUtils.readValue(oppoCustomerAfterSaleImeiStr,ArrayList.class);
        for(OppoCustomerAfterSaleImei oppoCustomerAfterSaleImei:oppoCustomerAfterSaleImeis){
            oppoCustomerAfterSaleImei.setProductCode(productColorMap.get(oppoCustomerAfterSaleImei.getProductCode()));
            oppoCustomerAfterSaleImei.setDate(oppoCustomerAfterSaleImei.getDate());
        }
        return oppoCustomerAfterSaleImeis;
    }

    //门店演示机汇总数据
    public List<OppoCustomerDemoPhone> getOppoCustomerDemoPhone(LocalDate dateStart, LocalDate dateEnd) {
        String companyId=("1");
        initAreaDepotMap(companyId);
        Map<String, String> productColorMap = getProductColorMap();
        String oppoCustomerDeomoPhones=oppoClient.findOppoCustomerDemoPhones(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones= ObjectMapperUtils.readValue(oppoCustomerDeomoPhones,ArrayList.class);
        for(OppoCustomerDemoPhone oppoCustomerDemoPhone:oppoCustomerDemoPhones){
            oppoCustomerDemoPhone.setProductCode(productColorMap.get(oppoCustomerDemoPhone.getProductCode()));
        }
        return oppoCustomerDemoPhones;
    }


    private void initAreaDepotMap(String companyId){
        String customerStr=customerClient.findCustomerDtoList();
        List<CustomerDto> customerDtosList= ObjectMapperUtils.readValue(customerStr, ArrayList.class);
        for(CustomerDto customerDto:customerDtosList){
            if(customerDtoMap.containsKey(customerDto.getDepotId())){
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
