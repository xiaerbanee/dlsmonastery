package net.myspring.tool.modules.vivo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.client.OfficeClient;
import net.myspring.tool.common.dataSource.annotation.FutureDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.domain.OfficeEntity;
import net.myspring.tool.modules.vivo.domain.VivoPushSCustomers;
import net.myspring.tool.modules.vivo.domain.VivoPushZones;
import net.myspring.tool.modules.vivo.dto.FutureCustomerDto;
import net.myspring.tool.modules.vivo.repository.VivoPushSCustomersRepository;
import net.myspring.tool.modules.vivo.repository.VivoPushZoneRepository;
import net.myspring.util.text.StringUtils;
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
    private VivoPushZoneRepository vivoPushZoneRepository;
    @Autowired
    private VivoPushSCustomersRepository vivoPushSCustomersRepository;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @LocalDataSource
    @Transactional
    public  List<VivoPushZones> pushVivoZones(String date){
        LocalDate dateStart = LocalDateUtils.parse(date);
        LocalDate dateEnd = dateStart.plusDays(1);
        String mainCode = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).split(CharConstant.COMMA)[0].replace("\"","");
        List<OfficeEntity> officeEntityList = officeClient.findAll();
        List<VivoPushZones> vivoPushZonesList = Lists.newArrayList();
        Map<String,Integer> map = getOfficeChildCountMap(officeEntityList);
        for(OfficeEntity officeEntity:officeEntityList){
            VivoPushZones vivoPushZone = new VivoPushZones();
            vivoPushZone.setZoneId(getZoneId(mainCode,officeEntity.getId()));
            vivoPushZone.setZoneName(officeEntity.getName());
            vivoPushZone.setShortCut(mainCode);
            String[] parentIds = officeEntity.getParentIds().split(CharConstant.COMMA);
            vivoPushZone.setZoneDepth(parentIds.length);
            StringBuilder zonePath = new StringBuilder(CharConstant.VERTICAL_LINE);
            for(String parentId:parentIds){
                zonePath.append(getZoneId(mainCode,parentId)).append(CharConstant.VERTICAL_LINE);
            }
            zonePath.append(getZoneId(mainCode,officeEntity.getId())).append(CharConstant.VERTICAL_LINE);
            vivoPushZone.setZonePath(zonePath.toString());
            vivoPushZone.setFatherId(getZoneId(mainCode,officeEntity.getParentId()));
            vivoPushZone.setSubCount(map.get(officeEntity.getId()));
            vivoPushZone.setZoneTypes(CharConstant.EMPTY);
            vivoPushZone.setCreatedDate(LocalDateUtils.parse(date));
            vivoPushZonesList.add(vivoPushZone);
        }
        logger.info("开始上抛机构数据"+ LocalDateTime.now());
        List<VivoPushZones> vivoPushZones = vivoPushZoneRepository.findBydate(dateStart,dateEnd);
        vivoPushZoneRepository.delete(vivoPushZones);
        vivoPushZoneRepository.batchSave(vivoPushZonesList);
        logger.info("上抛机构数据完成"+LocalDateTime.now());
        return vivoPushZonesList;
    }

    @FutureDataSource
    public List<FutureCustomerDto> getFutureVivoCustomers(String date){
        List<FutureCustomerDto> futureCustomerDtoList =  vivoPushSCustomersRepository.findFutureVivoCustomers(LocalDateUtils.parse(date));
        return futureCustomerDtoList;
    }

    @LocalDataSource
    @Transactional
    public void saveVivoPushSCustomers(List<FutureCustomerDto> futureCustomerDtoList,String date){
        LocalDate dateStart = LocalDateUtils.parse(date);
        LocalDate dateEnd = dateStart.plusDays(1);
        String mainCode = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).split(CharConstant.COMMA)[0].replace("\"","");
        List<VivoPushSCustomers> vivoPushSCustomersList = Lists.newArrayList();
        for(FutureCustomerDto futureCustomerDto :futureCustomerDtoList){
            VivoPushSCustomers vivoPushSCustomer = new VivoPushSCustomers();
            String customerId = futureCustomerDto.getCustomerId();
            vivoPushSCustomer.setCustomerLevel(futureCustomerDto.getCustomerLevel());
            if(futureCustomerDto.getCustomerLevel() == 1){
                vivoPushSCustomer.setCustomerId(StringUtils.getFormatId(customerId,mainCode+"D","00000"));
            }else {
                vivoPushSCustomer.setCustomerId(StringUtils.getFormatId(customerId,mainCode+"C","00000"));
                vivoPushSCustomer.setCustomerstr4(StringUtils.getFormatId(futureCustomerDto.getCustomerStr4(),mainCode+"D","00000"));
            }
            vivoPushSCustomer.setCustomerName(futureCustomerDto.getCustomerName());
            vivoPushSCustomer.setZoneId(getZoneId(mainCode,futureCustomerDto.getZoneId()));
            vivoPushSCustomer.setCompanyId(mainCode);
            vivoPushSCustomer.setRecordDate(futureCustomerDto.getRecordDate());
            vivoPushSCustomer.setCustomerstr1(futureCustomerDto.getCustomerStr1());
            vivoPushSCustomer.setCustomerstr10(mainCode);
            vivoPushSCustomer.setCreatedDate(dateStart);
            vivoPushSCustomersList.add(vivoPushSCustomer);
        }
        logger.info("开始上抛客户数据"+LocalDateTime.now());
        List<VivoPushSCustomers> vivoPushSCustomers = vivoPushSCustomersRepository.findByDate(dateStart,dateEnd);
        vivoPushSCustomersRepository.delete(vivoPushSCustomers);
        vivoPushSCustomersRepository.batchSave(vivoPushSCustomersList);
        logger.info("上抛客户数据完成"+LocalDateTime.now());
    }


    //上抛组织机构数据
    private List<Object> getOffice(){


        return null;
    }

    private String getZoneId(String mainCode,String id){
        return StringUtils.getFormatId(id,mainCode,"0000");
    }

    private Map<String,Integer> getOfficeChildCountMap(List<OfficeEntity> officeEntityList){
        Map<String,Integer> childCountMap = Maps.newHashMap();
        for(OfficeEntity officeEntity : officeEntityList){
            String id = officeEntity.getId();
            int subCount = 0 ;
            for(OfficeEntity officeEntity1:officeEntityList){
                if (officeEntity1.getParentIds().contains(CharConstant.COMMA+id+CharConstant.COMMA)){
                    subCount++;
                }
            }
            childCountMap.put(officeEntity.getId(),subCount);
        }
        return childCountMap;
    }


}
