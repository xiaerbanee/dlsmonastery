package net.myspring.tool.modules.oppo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.oppo.client.CustomerClient;
import net.myspring.tool.modules.oppo.client.DistrictClient;
import net.myspring.tool.modules.oppo.client.OppoCustomerAllotClient;
import net.myspring.tool.modules.oppo.domain.OppoCustomerAllot;
import net.myspring.tool.modules.oppo.domain.OppoCustomerOperatortype;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.dto.CustomerDto;
import net.myspring.tool.modules.oppo.dto.DistrictDto;
import net.myspring.tool.modules.oppo.domain.OppoCustomer;
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
    private OppoCustomerAllotClient oppoCustomerAllotClient;
    @Autowired
    private OppoPlantAgentProductSelRepository oppoPlantAgentProductSelRepository;

    private static Map<String,String> areaDepotMap=Maps.newHashMap();
    private static Map<String,CustomerDto> customerDtoMap=Maps.newHashMap();


    //上抛oppo门店数据,只上抛二代和渠道门店
    public List<OppoCustomer> getOppoCustomers() {
        List<OppoCustomer> oppoCustomers = Lists.newArrayList();
        String companyId=RequestUtils.getRequestEntity().getCompanyId();
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
            String depotId=getAreaDepotId(customerDto);
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
        String companyId=RequestUtils.getRequestEntity().getCompanyId();
        initAreaDepotMap(companyId);
        String customerStr=customerClient.findCustomerDtoList();
        List<CustomerDto> customerDtosList= ObjectMapperUtils.readValue(customerStr, ArrayList.class);
        for(CustomerDto customerDto:customerDtosList){
            if(isShop(customerDto)){
                if("运营商营业厅".equals(customerDto.getSalePointType())){
                    if(StringUtils.isNotBlank((customerDto.getCarrierType()))){
                        OppoCustomerOperatortype oppoCustomerOperatortype=new OppoCustomerOperatortype();
                        oppoCustomerOperatortype.setCustomerid(getAreaDepotId(customerDto));
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
                                oppoCustomerOperatortype.setCustomerid(getAreaDepotId(customerDto));
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

    public List<OppoCustomerAllot>  getOppoCustomerAllot(LocalDate dateStart, LocalDate dateEnd){
        String companyId=RequestUtils.getRequestEntity().getCompanyId();
        String oppoCustomerAllotStr=oppoCustomerAllotClient.findOppoCustomerAllots(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateStart),companyId);
        List<OppoCustomerAllot> oppoCustomerAllots=ObjectMapperUtils.readValue(oppoCustomerAllotStr, ArrayList.class);
        List<OppoPlantAgentProductSel> oppoPlantAgentProductSels=oppoPlantAgentProductSelRepository.findAllByProductId();
        Map<String, String> productColorMap = Maps.newHashMap();
        Map<String, OppoCustomerAllot> oppoCustomerAllotMap = Maps.newHashMap();
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
        for(OppoCustomerAllot oppoCustomerAllot:oppoCustomerAllots){
            String fromDepotId=getAreaDepotId(customerDtoMap.get(oppoCustomerAllot.getFromCustomerid()));
            String toDepotId=getAreaDepotId(customerDtoMap.get(oppoCustomerAllot.getToCustomerid()));
            if(!fromDepotId.equals(toDepotId)){
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
        List<OppoCustomerAllot>  oppoCustomerAllotList=new ArrayList<OppoCustomerAllot>(oppoCustomerAllotMap.values());
        return  oppoCustomerAllotList;
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

    private  String  getAreaDepotId(CustomerDto customerDto){
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



}
