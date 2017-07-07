package net.myspring.tool.modules.vivo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.client.OfficeClient;
import net.myspring.tool.common.domain.OfficeEntity;
import net.myspring.tool.modules.vivo.domain.VivoPushZones;
import net.myspring.tool.modules.vivo.repository.VivoPushZoneRepository;
import net.myspring.util.text.StringUtils;
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

    @Transactional
    public void vivoPush(){
        String mainCode = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).split(CharConstant.COMMA)[0].replace("\"","");
        LocalDate dateStart = LocalDate.now();
        LocalDate dateEnd = dateStart.plusDays(1);
        List<OfficeEntity> officeEntityList = officeClient.findAll();
        List<VivoPushZones> vivoPushZonesList = Lists.newArrayList();
        Map<String,Integer> map = getOfficeChildCountMap(officeEntityList);
        for(OfficeEntity officeEntity:officeEntityList){
            VivoPushZones vivoPushZones = new VivoPushZones();
            vivoPushZones.setZoneId(getZoneId(mainCode,officeEntity.getId()));
            vivoPushZones.setZoneName(officeEntity.getName());
            vivoPushZones.setShortCut(mainCode);
            String[] parentIds = officeEntity.getParentIds().split(CharConstant.COMMA);
            vivoPushZones.setZoneDepth(parentIds.length);
            StringBuilder zonePath = new StringBuilder(CharConstant.VERTICAL_LINE);
            for(String parentId:parentIds){
                zonePath.append(getZoneId(mainCode,parentId)).append(CharConstant.VERTICAL_LINE);
            }
            zonePath.append(getZoneId(mainCode,officeEntity.getId())).append(CharConstant.VERTICAL_LINE);
            vivoPushZones.setZonePath(zonePath.toString());
            vivoPushZones.setFatherId(getZoneId(mainCode,officeEntity.getParentId()));
            vivoPushZones.setSubCount(map.get(officeEntity.getId()));
            vivoPushZones.setZoneTypes("");
            vivoPushZones.setCreatedDate(LocalDateTime.now());
            vivoPushZonesList.add(vivoPushZones);
        }
        vivoPushZoneRepository.save(vivoPushZonesList);
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
