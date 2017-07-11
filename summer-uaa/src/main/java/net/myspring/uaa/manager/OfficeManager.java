package net.myspring.uaa.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.uaa.dto.OfficeDto;
import net.myspring.uaa.dto.OfficeBusinessDto;
import net.myspring.uaa.enums.OfficeTypeEnum;
import net.myspring.uaa.repository.OfficeBusinessRepository;
import net.myspring.uaa.repository.OfficeRepository;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfficeManager {

    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private OfficeBusinessRepository officeBusinessRepository;

    public List<String> getOfficeIdList(String officeId) {
        List<String> officeIdList = Lists.newLinkedList();
        OfficeDto officeDto = officeRepository.findOne(officeId);
        officeIdList.add(officeDto.getId());
        if (OfficeTypeEnum.业务部门.name().equalsIgnoreCase(officeDto.getType())) {
            officeIdList.addAll(CollectionUtil.extractToList(officeRepository.findByParentIdsLike("%,"+officeDto.getId()+",%"), "id"));
        } else if (OfficeTypeEnum.职能部门.name().equalsIgnoreCase(officeDto.getType())) {
            List<OfficeBusinessDto> businessList = officeBusinessRepository.findByOfficeId(officeDto.getId());
            if (CollectionUtil.isNotEmpty(businessList)) {
                List<String> officeIds = CollectionUtil.extractToList(businessList, "businessOfficeId");
                officeIdList.addAll(officeIds);
                List<OfficeDto> childOfficeList = officeRepository.findByParentIdsListLike(officeIds);
                officeIdList.addAll(CollectionUtil.extractToList(childOfficeList, "id"));
            }
        }
        officeIdList.add("0");
        officeIdList = Lists.newArrayList(Sets.newHashSet(officeIdList));
        return officeIdList;
    }

    public OfficeDto findOne(String id) {
        return officeRepository.findOne(id);
    }
}
