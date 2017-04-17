package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Office;
import net.myspring.basic.modules.hr.dto.OfficeDto;
import net.myspring.basic.modules.hr.manager.OfficeManager;
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
    private OfficeManager officeManager;
    @Autowired
    private CacheUtils cacheUtils;


    public List<Office> findByType(String type) {
        return officeMapper.findByType(type);
    }

    public Page<OfficeDto> findPage(Pageable pageable, OfficeQuery officeQuery) {
        Page<OfficeDto> page = officeMapper.findPage(pageable, officeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
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

    public OfficeForm findForm(OfficeForm officeForm) {
        if(!officeForm.isCreate()){
            Office office = officeMapper.findOne(officeForm.getId());
            officeForm= BeanUtil.map(office,OfficeForm.class);
            cacheUtils.initCacheInput(officeForm);
        }
        return officeForm;
    }


    public Office save(OfficeForm officeForm) {
        Office office;
        if (officeForm.isCreate()) {
            office=BeanUtil.map(officeForm,Office.class);
            office=officeManager.save(office);
        } else {
            office=officeManager.updateForm(officeForm);
        }
        return office;
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
}
