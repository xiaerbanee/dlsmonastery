package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.DutyPublicFree;
import net.myspring.basic.modules.hr.dto.DutyPublicFreeDto;
import net.myspring.basic.modules.hr.mapper.DutyPublicFreeMapper;
import net.myspring.basic.modules.hr.web.form.DutyPublicFreeForm;
import net.myspring.basic.modules.hr.web.query.DutyPublicFreeQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DutyPublicFreeService {

    @Autowired
    private DutyPublicFreeMapper dutyPublicFreeMapper;
    @Autowired
    private CacheUtils cacheUtils;



    public DutyPublicFree save(DutyPublicFreeForm dutyPublicFreeForm) {
        dutyPublicFreeForm.setEmployeeId(SecurityUtils.getEmployeeId());
        dutyPublicFreeForm.setStatus(AuditTypeEnum.APPLYING.toString());
        DutyPublicFree dutyPublicFree = BeanUtil.map(dutyPublicFreeForm, DutyPublicFree.class);
        dutyPublicFreeMapper.save(dutyPublicFree);
        return dutyPublicFree;
    }

    public Page<DutyPublicFreeDto> findPage(Pageable pageable, DutyPublicFreeQuery dutyPublicFreeQuery) {
        Page<DutyPublicFreeDto> page = dutyPublicFreeMapper.findPage(pageable,dutyPublicFreeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void logicDeleteOne(String id) {
        dutyPublicFreeMapper.logicDeleteOne(id);
    }

    public DutyPublicFree findOne(String id) {
        DutyPublicFree dutyPublicFree = dutyPublicFreeMapper.findOne(id);
        return dutyPublicFree;
    }

    public DutyPublicFreeForm findForm(DutyPublicFreeForm dutyPublicFreeForm) {
        if(!dutyPublicFreeForm.isCreate()){
            DutyPublicFree dutyPublicFree =dutyPublicFreeMapper.findOne(dutyPublicFreeForm.getId());
            dutyPublicFreeForm= BeanUtil.map(dutyPublicFree,DutyPublicFreeForm.class);
            cacheUtils.initCacheInput(dutyPublicFree);
        }
        return dutyPublicFreeForm;
    }
}
