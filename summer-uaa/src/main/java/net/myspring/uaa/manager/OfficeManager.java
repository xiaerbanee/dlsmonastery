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

    public List<String> getOfficeIdList(List<String> officeIds) {
        List<String> officeIdList = Lists.newLinkedList();
        officeIdList.addAll(officeIds);
        officeIdList.addAll(CollectionUtil.extractToList(officeRepository.findByParentIdsListLike(officeIds),"id"));
        officeIdList.add("0");
        officeIdList = Lists.newArrayList(Sets.newHashSet(officeIdList));
        return officeIdList;
    }

    public OfficeDto findOne(String id) {
        return officeRepository.findOne(id);
    }

    public List<OfficeDto> findByIds(List<String> idList) {
        return officeRepository.findByIds(idList);
    }
}
