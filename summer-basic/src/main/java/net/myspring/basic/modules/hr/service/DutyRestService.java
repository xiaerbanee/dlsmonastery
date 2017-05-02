package net.myspring.basic.modules.hr.service;

import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.DutyRest;
import net.myspring.basic.modules.hr.dto.DutyRestDto;
import net.myspring.basic.modules.hr.mapper.DutyRestMapper;
import net.myspring.basic.modules.hr.web.form.DutyRestForm;
import net.myspring.basic.modules.hr.web.query.DutyRestQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DutyRestService {

    @Autowired
    private DutyRestMapper dutyRestMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public Page<DutyRestDto> findPage(Pageable pageable,DutyRestQuery dutyRestQuery) {
        Page<DutyRestDto> page = dutyRestMapper.findPage(pageable, dutyRestQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DutyRest save(DutyRestForm dutyRestForm) {
        dutyRestForm.setStatus(AuditTypeEnum.APPLYING.toString());
        dutyRestForm.setEmployeeId(SecurityUtils.getEmployeeId());
        DutyRest dutyRest = BeanUtil.map(dutyRestForm, DutyRest.class);
        dutyRestMapper.save(dutyRest);
        return dutyRest;
    }

    public void logicDeleteOne(String id) {
        dutyRestMapper.logicDeleteOne(id);
    }

    public DutyRest findOne(String id) {
        DutyRest dutyRest = dutyRestMapper.findOne(id);
        return dutyRest;
    }

    public DutyRestForm findForm(DutyRestForm dutyRestForm) {
        if(!dutyRestForm.isCreate()){
            DutyRest dutyRest =findOne(dutyRestForm.getId());
            dutyRestForm= BeanUtil.map(dutyRest,DutyRestForm.class);
            cacheUtils.initCacheInput(dutyRestForm);
        }
        return dutyRestForm;
    }
}
