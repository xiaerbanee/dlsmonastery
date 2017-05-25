package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.*;
import net.myspring.basic.modules.hr.dto.DutyWorktimeDto;
import net.myspring.basic.modules.hr.repository.*;
import net.myspring.basic.modules.hr.web.query.DutyWorktimeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class DutyWorktimeService {
    @Autowired
    private DutyWorktimeRepository dutyWorktimeRepository;
    @Autowired
    private DutyLeaveRepository dutyLeaveRepository;
    @Autowired
    private DutyOvertimeRepository dutyOvertimeRepository;
    @Autowired
    private DutyRestRepository dutyRestRepository;
    @Autowired
    private DutySignRepository dutySignRepository;
    @Autowired
    private DutyFreeRepository dutyFreeRepository;
    @Autowired
    private DutyPublicFreeRepository dutyPublicFreeRepository;
    @Autowired
    private DutyTripRepository dutyTripRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DutyWorktimeDto> findPage(Pageable pageable, DutyWorktimeQuery dutyWorktimeQuery){
        Page<DutyWorktimeDto> page = dutyWorktimeRepository.findPage(pageable, dutyWorktimeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public Map<String, DutyWorktime> getWorktimeMap(Long accountId,LocalDate dateStart, LocalDate dateEnd){
        List<Long> accountIds=Lists.newArrayList();
        if(accountId!=null){
            accountIds.add(accountId);
        }
        List<DutyWorktime>  dutyWorktimeList=dutyWorktimeRepository.findByAccountIdAndDutyDate(dateStart,dateEnd,accountIds);
        List<DutySign> dutySignList=dutySignRepository.findByAccountIdAndDutyDate(dateStart,dateEnd,accountIds);
        List<DutyOvertime> dutyOvertimeList=dutyOvertimeRepository.findByAccountIdAndDutyDate(dateStart,dateEnd,accountIds);
        List<DutyLeave> dutyLeaveList=dutyLeaveRepository.findByAccountIdAndDutyDate(dateStart,dateEnd,accountIds);
        List<DutyRest> dutyRestList=dutyRestRepository.findByAccountIdAndDutyDate(dateStart,dateEnd,accountIds);
        List<DutyFree> dutyFreeList=dutyFreeRepository.findByAccountIdAndDutyDate(dateStart,dateEnd,accountIds);
        return null;
    }
}
