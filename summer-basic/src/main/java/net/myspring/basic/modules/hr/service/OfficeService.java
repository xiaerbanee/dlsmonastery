package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Office;
import net.myspring.basic.modules.hr.dto.OfficeDto;
import net.myspring.basic.modules.hr.mapper.OfficeMapper;
import net.myspring.basic.modules.hr.web.form.OfficeForm;
import net.myspring.basic.modules.hr.web.query.OfficeQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OfficeService {

    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private SecurityUtils securityUtils;

    public List<Office> findByType(String type) {
        return officeMapper.findByType(type);
    }

    public Page<OfficeDto> findPage(Pageable pageable, OfficeQuery officeQuery) {
        Page<Office> page = officeMapper.findPage(pageable, officeQuery);
        Page<OfficeDto> officeDtoPage= BeanUtil.map(page,OfficeDto.class);
        cacheUtils.initCacheInput(officeDtoPage.getContent());
        return officeDtoPage;
    }

    public List<Office> findByParentIdsLike(String parentId) {
        List<Office> officeList = officeMapper.findByParentIdsLike(parentId);
        return officeList;
    }


    public List<Office> findAll() {
        return officeMapper.findAll();
    }

    public Office findOne(String id) {
        Office office = officeMapper.findOne(id);
        return office;
    }

    public OfficeDto findDto(String id) {
        Office office = findOne(id);
        OfficeDto officeDto= BeanUtil.map(office,OfficeDto.class);
        cacheUtils.initCacheInput(officeDto);
        return officeDto;
    }


    public OfficeForm save(OfficeForm officeForm) {
        boolean isCreate= StringUtils.isBlank(officeForm.getId());
        if (isCreate) {
            officeMapper.saveForm(officeForm);
        } else {
            officeMapper.updateForm(officeForm);
        }
        return officeForm;
    }

    public void logicDeleteOne(Office office) {
        officeMapper.logicDeleteOne(office.getId());
    }

    public List<OfficeDto> findByFilter(Map<String, Object> map) {
        List<Office> officeList = officeMapper.findByFilter(map);
        List<OfficeDto> officeDtoList= BeanUtil.map(officeList,OfficeDto.class);
        cacheUtils.initCacheInput(officeDtoList);
        return officeDtoList;
    }

//    public List<Office> findSortList() {
//        List<Office> officeList = Lists.newLinkedList();
//        List<String> officeIds = FilterUtils.getOfficeIds(AccountUtils.getAccountId());
//        List<Office> offices =Lists.newArrayList();
//        if(CollectionUtil.isNotEmpty(officeIds)){
//            List<String> areaIds = Lists.newArrayList();
//            for (String officeId : officeIds) {
//                areaIds.add(OfficeUtils.getOfficeIdByOfficeType(officeId, Const.OFFICE_TYPE_AREA));
//            }
//            offices = officeMapper.findByAreaIds(areaIds);
//        }else {
//            offices=officeMapper.findAll();
//        }
//        for (Office office : offices) {
//           if(Const.OFFICE_TYPE_AREA.equals(office.getType())){
//               office.setName(office.getName().trim());
//               officeList.add(office);
//               sortOffice(officeList,offices,office.getId(),Const.CHAR_SPACE_FULL);
//           }
//        }
//        return officeList;
//    }
//
//    private void sortOffice(List<Office> officeList, List<Office> offices,String parentId,String prefix) {
//        for (Office office : offices) {
//            if (parentId.equals(office.getParentId())) {
//                office.setName(prefix+office.getName().trim());
//                officeList.add(office);
//                sortOffice(officeList, offices,office.getId(),prefix+Const.CHAR_SPACE_FULL);
//            }
//        }
//    }

}
