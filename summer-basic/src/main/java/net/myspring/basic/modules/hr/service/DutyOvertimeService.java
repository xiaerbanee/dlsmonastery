package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.DutyOvertime;
import net.myspring.basic.modules.hr.dto.DutyOvertimeDto;
import net.myspring.basic.modules.hr.repository.DutyOvertimeRepository;
import net.myspring.basic.modules.hr.web.form.DutyOvertimeForm;
import net.myspring.basic.modules.hr.web.query.DutyOvertimeQuery;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DutyOvertimeService {

    @Autowired
    private DutyOvertimeRepository dutyOvertimeRepository;
    @Autowired
    private CacheUtils cacheUtils;



    public Page<DutyOvertimeDto> findPage(Pageable pageable, DutyOvertimeQuery dutyOvertimeQuery) {
        Page<DutyOvertimeDto> page = dutyOvertimeRepository.findPage(pageable, dutyOvertimeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DutyOvertime save(DutyOvertimeForm dutyOvertimeForm) {
        dutyOvertimeForm.setLeftHour(dutyOvertimeForm.getHour());
        dutyOvertimeForm.setStatus(AuditTypeEnum.APPLYING.toString());
        dutyOvertimeForm.setEmployeeId(RequestUtils.getRequestEntity().getEmployeeId());
        DutyOvertime dutyOvertime = BeanUtil.map(dutyOvertimeForm, DutyOvertime.class);
        dutyOvertimeRepository.save(dutyOvertime);
        return dutyOvertime;
    }

    public List<DutyOvertime> findByDutyDate(String employeeId, LocalDate dutyDate) {
        List<DutyOvertime> dutyOvertimes = dutyOvertimeRepository.findByDutyDate(employeeId, dutyDate);
        return dutyOvertimes;
    }

    public List<DutyOvertime> findByDutyDateAndStatus(String employeeId, LocalDate dutyDateStart, LocalDate dutyDateEnd, String status) {
        List<DutyOvertime> dutyOvertimes = dutyOvertimeRepository.findByDutyDateAndStatus(employeeId, dutyDateStart, dutyDateEnd, status);
        return dutyOvertimes;
    }

    public void logicDeleteOne(String id) {
        dutyOvertimeRepository.logicDeleteOne(id);
    }


    public DutyOvertime findOne(String id) {
        DutyOvertime dutyOvertime = dutyOvertimeRepository.findOne(id);
        return dutyOvertime;
    }

    public DutyOvertimeForm getForm(DutyOvertimeForm dutyOvertimeForm) {
        if(!dutyOvertimeForm.isCreate()){
            DutyOvertime dutyOvertime =dutyOvertimeRepository.findOne(dutyOvertimeForm.getId());
            dutyOvertimeForm= BeanUtil.map(dutyOvertime,DutyOvertimeForm.class);
            cacheUtils.initCacheInput(dutyOvertimeForm);
        }
        return dutyOvertimeForm;
    }

    public Double getAvailableHour(String employeeId, LocalDate currentDate) {
        Double overtimeHour = 0.0;
        if (currentDate == null) {
            return overtimeHour;
        }
        LocalDate dateStart = currentDate.minusMonths(3);
        LocalDate dateEnd = currentDate;
        List<DutyOvertime> overtimeList = dutyOvertimeRepository.findByIdAndDate(employeeId, dateStart, dateEnd, AuditTypeEnum.PASSED.toString());
        if (CollectionUtil.isNotEmpty(overtimeList)) {
            for (DutyOvertime dutyOvertime : overtimeList) {
                overtimeHour = overtimeHour + dutyOvertime.getLeftHour();
            }
        }
        return overtimeHour;
    }

    public Double getExpiredHour(String employeeId, LocalDate currentDate){
        Double overtimeHour = 0.0;
        if (currentDate == null) {
            return overtimeHour;
        }
        LocalDate dateStart = currentDate.minusMonths(3);
        LocalDate dateEnd =  currentDate.minusMonths(3);
        List<DutyOvertime> overtimeList = dutyOvertimeRepository.findByIdAndDate(employeeId, dateStart, dateEnd, AuditTypeEnum.PASSED.toString());
        if (CollectionUtil.isNotEmpty(overtimeList)) {
            for (DutyOvertime dutyOvertime : overtimeList) {
                overtimeHour = overtimeHour + dutyOvertime.getLeftHour();
            }
        }
        return overtimeHour;
    }
}
