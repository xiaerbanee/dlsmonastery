package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.DutyFree;
import net.myspring.basic.modules.hr.dto.DutyFreeDto;
import net.myspring.basic.modules.hr.mapper.DutyFreeMapper;
import net.myspring.basic.modules.hr.web.form.DutyFreeForm;
import net.myspring.basic.modules.hr.web.query.DutyFreeQuery;
import net.myspring.util.mapper.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DutyFreeService {

    @Autowired
    private DutyFreeMapper dutyFreeMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private SecurityUtils securityUtils;

    public Page<DutyFreeDto> findPage(Pageable pageable, DutyFreeQuery dutyFreeQuery) {
        Page<DutyFree> page = dutyFreeMapper.findPage(pageable, dutyFreeQuery);
        Page<DutyFreeDto> dutyFreeDtoPage= BeanMapper.convertPage(page,DutyFreeDto.class);
        cacheUtils.initCacheInput(dutyFreeDtoPage.getContent());
        return dutyFreeDtoPage;
    }

    public DutyFreeForm save(DutyFreeForm dutyFreeForm) {
        dutyFreeForm.setEmployeeId(securityUtils.getEmployeeId());
        dutyFreeForm.setStatus(AuditTypeEnum.APPLY.getValue());
        dutyFreeMapper.saveForm(dutyFreeForm);
        return dutyFreeForm;
    }

    public DutyFree findOne(String id) {
        DutyFree dutyFree = dutyFreeMapper.findOne(id);
        return dutyFree;
    }

    public DutyFreeDto findDto(String id) {
        DutyFree dutyFree =findOne(id);
        DutyFreeDto dutyFreeDto = BeanMapper.convertDto(dutyFree,DutyFreeDto.class);
        cacheUtils.initCacheInput(dutyFreeDto);
        return dutyFreeDto;
    }

    public void logicDeleteOne(String id) {
        dutyFreeMapper.logicDeleteOne(id);
    }

    public List<DutyFree> findByDate(LocalDate freeDate, String employeeId) {
        return dutyFreeMapper.findByDate(freeDate, employeeId);
    }
}
