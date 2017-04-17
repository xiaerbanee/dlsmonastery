package net.myspring.basic.modules.hr.service;


import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.AuditTypeEnum;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.DutyLeave;
import net.myspring.basic.modules.hr.dto.DutyLeaveDto;
import net.myspring.basic.modules.hr.mapper.DutyLeaveMapper;
import net.myspring.basic.modules.hr.web.form.DutyLeaveForm;
import net.myspring.basic.modules.hr.web.query.DutyLeaveQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DutyLeaveService {

    @Autowired
    private DutyLeaveMapper dutyLeaveMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public Page<DutyLeaveDto> findPage(Pageable pageable, DutyLeaveQuery dutyLeaveQuery) {
        Page<DutyLeaveDto> page = dutyLeaveMapper.findPage(pageable, dutyLeaveQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<DutyLeave> save(DutyLeaveForm dutyLeaveForm) {
        List<DutyLeave> dutyLeaveList= Lists.newArrayList();
        if (dutyLeaveForm.getDutyDateStart().equals(dutyLeaveForm.getDutyDateEnd())) {
            LocalDate date = LocalDate.parse(dutyLeaveForm.getDutyDateStart());
            if (dutyLeaveMapper.findByEmployeeAndDateAndDateType(dutyLeaveForm.getEmployeeId(), date, dutyLeaveForm.getDateType())  == null) {
                dutyLeaveForm.setDutyDate(date);
                dutyLeaveForm.setStatus(AuditTypeEnum.APPLY.getValue());
                dutyLeaveForm.setEmployeeId(SecurityUtils.getEmployeeId());
                DutyLeave dutyLeave=BeanUtil.map(dutyLeaveForm,DutyLeave.class);
                dutyLeaveMapper.save(dutyLeave);
                dutyLeave=dutyLeaveMapper.findOne(dutyLeave.getId());
                dutyLeaveList.add(dutyLeave);
            }
        }else {
            LocalDate dateStart = LocalDate.parse(dutyLeaveForm.getDutyDateStart());
            LocalDate dateEnd = LocalDate.parse(dutyLeaveForm.getDutyDateEnd());
            List<LocalDate> dateList = LocalDateUtils.getDateList(dateStart, dateEnd);
            for (LocalDate date : dateList) {
                if (dutyLeaveMapper.findByEmployeeAndDateAndDateType(dutyLeaveForm.getEmployeeId(), date, DutyDateTypeEnum.全天.toString()) == null) {
                    DutyLeave item = new DutyLeave();
                    item.setDateType(DutyDateTypeEnum.全天.toString());
                    item.setDutyDate(date);
                    item.setStatus(AuditTypeEnum.APPLY.getValue());
                    item.setLeaveType(dutyLeaveForm.getLeaveType());
                    item.setAttachment(dutyLeaveForm.getAttachment());
                    item.setRemarks(dutyLeaveForm.getRemarks());
                    item.setEmployeeId(SecurityUtils.getEmployeeId());
                    dutyLeaveMapper.save(item);
                    dutyLeaveList.add(item);
                }
            }
        }
        return dutyLeaveList;
    }

    public void logicDeleteOne(String id) {
        dutyLeaveMapper.logicDeleteOne(id);
    }

    public DutyLeave findOne(String id) {
        DutyLeave dutyLeave=dutyLeaveMapper.findOne(id);
        return dutyLeave;
    }

    public DutyLeaveForm findForm(DutyLeaveForm dutyLeaveForm) {
        if(!dutyLeaveForm.isCreate()){
            DutyLeave dutyLeave=dutyLeaveMapper.findOne(dutyLeaveForm.getId());
            dutyLeaveForm= BeanUtil.map(dutyLeave,DutyLeaveForm.class);
            cacheUtils.initCacheInput(dutyLeaveForm);
        }
        return dutyLeaveForm;
    }

    public List<DutyLeave> findByDutyDateList(String employeeId,List<LocalDate> dutyDateList){
        return dutyLeaveMapper.findByDutyDateList(employeeId,dutyDateList);
    }

    public List<DutyLeave>  findByDutyDate(String employeeId,LocalDate dutyDate){
        return dutyLeaveMapper.findByDutyDate(employeeId,dutyDate);
    }
}
