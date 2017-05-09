package net.myspring.basic.modules.hr.service;

import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.DutyFree;
import net.myspring.basic.modules.hr.dto.DutyFreeDto;
import net.myspring.basic.modules.hr.mapper.DutyFreeMapper;
import net.myspring.basic.modules.hr.web.form.DutyFreeForm;
import net.myspring.basic.modules.hr.web.query.DutyFreeQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DutyFreeService {

    @Autowired
    private DutyFreeMapper dutyFreeMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DutyFreeDto> findPage(Pageable pageable, DutyFreeQuery dutyFreeQuery) {
        Page<DutyFreeDto> page = dutyFreeMapper.findPage(pageable, dutyFreeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DutyFree save(DutyFreeForm dutyFreeForm) {
        dutyFreeForm.setEmployeeId(RequestUtils.getEmployeeId());
        dutyFreeForm.setStatus(AuditTypeEnum.APPLYING.toString());
        DutyFree dutyFree = BeanUtil.map(dutyFreeForm, DutyFree.class);
        dutyFreeMapper.save(dutyFree);
        return dutyFree;
    }

    public DutyFree findOne(String id) {
        DutyFree dutyFree = dutyFreeMapper.findOne(id);
        return dutyFree;
    }

    public DutyFreeForm findForm(DutyFreeForm dutyFreeForm) {
        if(!dutyFreeForm.isCreate()){
            DutyFree dutyFree =dutyFreeMapper.findOne(dutyFreeForm.getId());
            dutyFreeForm = BeanUtil.map(dutyFree,DutyFreeForm.class);
            cacheUtils.initCacheInput(dutyFreeForm);
        }
        return dutyFreeForm;
    }

    public void logicDeleteOne(String id) {
        dutyFreeMapper.logicDeleteOne(id);
    }

    public List<DutyFree> findByDate(LocalDate freeDate, String employeeId) {
        return dutyFreeMapper.findByDate(freeDate, employeeId);
    }
}
