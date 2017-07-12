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
import net.myspring.tool.modules.vivo.domain.SZonesM13e00;
import net.myspring.tool.modules.vivo.domain.SCustomersM13e00;
import net.myspring.tool.modules.vivo.dto.SCustomerDto;
import net.myspring.tool.modules.vivo.repository.SCustomersM13e00Repository;
import net.myspring.tool.modules.vivo.repository.SZonesM13e00Repository;
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
    private SZonesM13e00Repository sZonesM13e00Repository;
    @Autowired
    private SCustomersM13e00Repository sCustomersM13e00Repository;
    @Autowired
    private CacheUtils cacheUtils;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @FactoryDataSource
    @Transactional
    public  List<SZonesM13e00> pushVivoZonesData(){
        String mainCode = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).split(CharConstant.COMMA)[0].replace("\"","");
        List<OfficeEntity> officeEntityList = officeClient.findAll();
        List<SZonesM13e00> sZonesM13e00List = Lists.newArrayList();
        Map<String,Integer> map = getOfficeChildCountMap(officeEntityList);
        for(OfficeEntity officeEntity:officeEntityList){
            SZonesM13e00 sZonesM13e00 = new SZonesM13e00();
            sZonesM13e00.setZoneid(getZoneId(mainCode,officeEntity.getId()));
            sZonesM13e00.setZonename(officeEntity.getName());
            if(sZonesM13e00.getZonename().toString().length()>10){
                logger.info(sZonesM13e00.getZonename());
                sZonesM13e00.setZonename("测试");
            }
            sZonesM13e00.setShortcut(mainCode);
            String[] parentIds = officeEntity.getParentIds().split(CharConstant.COMMA);
            sZonesM13e00.setZonedepth(parentIds.length);
            StringBuilder zonePath = new StringBuilder(CharConstant.VERTICAL_LINE);
            for(String parentId:parentIds){
                zonePath.append(getZoneId(mainCode,parentId)).append(CharConstant.VERTICAL_LINE);
            }
            zonePath.append(getZoneId(mainCode,officeEntity.getId())).append(CharConstant.VERTICAL_LINE);
            sZonesM13e00.setZonepath(zonePath.toString());
            sZonesM13e00.setFatherid(getZoneId(mainCode,officeEntity.getParentId()));
            sZonesM13e00.setSubcount(map.get(officeEntity.getId()));
            sZonesM13e00.setZonetypes(CharConstant.EMPTY);
            sZonesM13e00List.add(sZonesM13e00);
        }
        logger.info("开始上抛机构数据"+ LocalDateTime.now());
        sZonesM13e00Repository.deleteAll();
        sZonesM13e00Repository.save(sZonesM13e00List);
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
            sCustomersM13e00.setCustomerlevel(futureCustomerDto.getCustomerLevel());
            if(futureCustomerDto.getCustomerLevel() == 1){
                sCustomersM13e00.setCustomerid(StringUtils.getFormatId(customerId,mainCode+"D","00000"));
                sCustomersM13e00.setCustomername(futureCustomerDto.getAreaName());
            }else {
                sCustomersM13e00.setCustomerid(StringUtils.getFormatId(customerId,mainCode+"C","00000"));
                sCustomersM13e00.setCustomername(futureCustomerDto.getCustomerName());
                sCustomersM13e00.setCustomerstr4(StringUtils.getFormatId(futureCustomerDto.getCustomerStr4(),mainCode+"D","00000"));
            }
            sCustomersM13e00.setZoneid(getZoneId(mainCode,futureCustomerDto.getZoneId()));
            sCustomersM13e00.setCompanyid(mainCode);
            sCustomersM13e00.setRecorddate(futureCustomerDto.getRecordDate());
            sCustomersM13e00.setCustomerstr1(futureCustomerDto.getCustomerStr1());
            sCustomersM13e00.setCustomerstr10(mainCode);
            sCustomersM13e00List.add(sCustomersM13e00);
        }
        logger.info("开始上抛客户数据"+LocalDateTime.now());
        sCustomersM13e00Repository.deleteAll();
        sCustomersM13e00Repository.save(sCustomersM13e00List);
//        sCustomersM13e00Repository.batchSave(sCustomersM13e00List);
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
