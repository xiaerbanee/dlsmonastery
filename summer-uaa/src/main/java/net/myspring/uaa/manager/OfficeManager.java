package net.myspring.uaa.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.uaa.dto.OfficeDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.uaa.dto.OfficeBusinessDto;
import net.myspring.uaa.enums.OfficeTypeEnum;
import net.myspring.uaa.repository.OfficeBusinessRepository;
import net.myspring.uaa.repository.OfficeRepository;
import net.myspring.uaa.repository.OfficeRuleRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfficeManager {

    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private OfficeRuleRepository officeRuleRepository;
    @Autowired
    private OfficeBusinessRepository officeBusinessRepository;
    @Value("${setting.adminIdList}")
    private String adminIdList;

    public List<String> officeFilter(String accountId,String officeId) {
        List<String> officeIdList = Lists.newArrayList();
        if (!StringUtils.getSplitList(adminIdList, CharConstant.COMMA).contains(accountId)) {
            OfficeDto office = officeRepository.findDtoById(officeId);
            officeIdList.add(office.getId());
            if (OfficeTypeEnum.职能部门.name().equalsIgnoreCase(office.getType())) {
                officeIdList.addAll(CollectionUtil.extractToList(officeRepository.findByParentIdsLike("%,"+office.getParentId()+",%"), "id"));
            } else {
                List<OfficeBusinessDto> businessList = officeBusinessRepository.findBusinessIdByOfficeId(office.getId());
                if (CollectionUtil.isNotEmpty(businessList)) {
                    List<String> officeIds = CollectionUtil.extractToList(businessList, "id");
                    officeIdList.addAll(officeIds);
                    List<OfficeDto> childOfficeList = officeRepository.findByParentIdsListLike(officeIds);
                    officeIdList.addAll(CollectionUtil.extractToList(childOfficeList, "id"));
                }
            }
            if (CollectionUtil.isNotEmpty(officeIdList)) {
                officeIdList.add("0");
            }
        }
        officeIdList = Lists.newArrayList(Sets.newHashSet(officeIdList));
        return officeIdList;
    }
}
