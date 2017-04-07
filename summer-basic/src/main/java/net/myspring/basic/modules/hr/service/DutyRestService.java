package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.DutyRest;
import net.myspring.basic.modules.hr.dto.DutyRestDto;
import net.myspring.basic.modules.hr.mapper.DutyRestMapper;
import net.myspring.basic.modules.hr.web.form.DutyRestForm;
import net.myspring.basic.modules.hr.web.query.DutyRestQuery;
import net.myspring.util.mapper.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DutyRestService {

    @Autowired
    private DutyRestMapper dutyRestMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private SecurityUtils securityUtils;

    public Page<DutyRestDto> findPage(Pageable pageable,DutyRestQuery dutyRestQuery) {
        Page<DutyRest> page = dutyRestMapper.findPage(pageable, dutyRestQuery);
        Page<DutyRestDto> dutyRestDtoPage=BeanMapper.convertPage(page,DutyRestDto.class);
        cacheUtils.initCacheInput(dutyRestDtoPage.getContent());
        return dutyRestDtoPage;
    }

    public DutyRest save(DutyRestForm dutyRestForm) {
        dutyRestForm.setStatus(AuditTypeEnum.APPLY.getValue());
        dutyRestForm.setEmployeeId(securityUtils.getEmployeeId());
        dutyRestMapper.save(dutyRestForm);
        return dutyRestForm;
    }

    public void logicDeleteOne(String id) {
        dutyRestMapper.logicDeleteOne(id);
    }

    public DutyRest findOne(String id) {
        DutyRest dutyRest = dutyRestMapper.findOne(id);
        return dutyRest;
    }

    public DutyRestDto findDto(String id) {
        DutyRest dutyRest =findOne(id);
        DutyRestDto dutyRestDto= BeanMapper.convertDto(dutyRest,DutyRestDto.class);
        cacheUtils.initCacheInput(dutyRestDto);
        return dutyRestDto;
    }
}
