package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.*;
import net.myspring.basic.modules.hr.dto.DutyWorktimeDto;
import net.myspring.basic.modules.hr.mapper.*;
import net.myspring.basic.modules.hr.web.query.DutyWorktimeQuery;
import net.myspring.util.mapper.BeanUtil;
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
    private DutyWorktimeMapper dutyWorktimeMapper;
    @Autowired
    private DutyLeaveMapper dutyLeaveMapper;
    @Autowired
    private DutyOvertimeMapper dutyOvertimeMapper;
    @Autowired
    private DutyRestMapper dutyRestMapper;
    @Autowired
    private DutySignMapper dutySignMapper;
    @Autowired
    private DutyFreeMapper dutyFreeMapper;
    @Autowired
    private DutyPublicFreeMapper dutyPublicFreeMapper;
    @Autowired
    private DutyTripMapper dutyTripMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private SecurityUtils securityUtils;

    public Page<DutyWorktimeDto> findPage(Pageable pageable, DutyWorktimeQuery dutyWorktimeQuery){
        Page<DutyWorktime> page = dutyWorktimeMapper.findPage(pageable, dutyWorktimeQuery);
        Page<DutyWorktimeDto> dutyWorktimeDtoPage= BeanUtil.map(page,DutyWorktimeDto.class);
        cacheUtils.initCacheInput(dutyWorktimeDtoPage.getContent());
        return dutyWorktimeDtoPage;
    }

    public Map<String, DutyWorktime> getWorktimeMap(Long accountId,LocalDate dateStart, LocalDate dateEnd){
        List<Long> accountIds=Lists.newArrayList();
        if(accountId!=null){
            accountIds.add(accountId);
        }
        List<DutyWorktime>  dutyWorktimeList=dutyWorktimeMapper.findByAccountIdAndDutyDate(dateStart,dateEnd,accountIds);
        List<DutySign> dutySignList=dutySignMapper.findByAccountIdAndDutyDate(dateStart,dateEnd,accountIds);
        List<DutyOvertime> dutyOvertimeList=dutyOvertimeMapper.findByAccountIdAndDutyDate(dateStart,dateEnd,accountIds);
        List<DutyLeave> dutyLeaveList=dutyLeaveMapper.findByAccountIdAndDutyDate(dateStart,dateEnd,accountIds);
        List<DutyRest> dutyRestList=dutyRestMapper.findByAccountIdAndDutyDate(dateStart,dateEnd,accountIds);
        List<DutyFree> dutyFreeList=dutyFreeMapper.findByAccountIdAndDutyDate(dateStart,dateEnd,accountIds);
        return null;
    }
}
