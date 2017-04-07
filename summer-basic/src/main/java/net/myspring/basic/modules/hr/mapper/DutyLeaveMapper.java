package net.myspring.basic.modules.hr.mapper;


import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.DutyLeave;
import net.myspring.basic.modules.hr.model.DutyModel;
import net.myspring.basic.modules.hr.web.query.DutyLeaveQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface DutyLeaveMapper extends MyMapper<DutyLeave,String> {

    Page<DutyLeave> findPage(Pageable pageable, @Param("p") DutyLeaveQuery dutyLeaveQuery);

    DutyLeave findByEmployeeAndDateAndDateType(@Param("employeeId") String employeeId, @Param("date") LocalDate date, @Param("dateType") String dateType);

    List<DutyModel> findByAuditable(@Param("leaderId") String leaderId, @Param("status") String status, @Param("dateStart") LocalDateTime dateStart);

    List<DutyLeave> findByDutyDateList(@Param("employeeId") String employeeId, @Param("dutyDateList") List<LocalDate> dutyDateList);

    List<DutyLeave> findByDutyDate(@Param("employeeId") String employeeId, @Param("dutyDate") LocalDate dutyDate);

    List<DutyLeave> findByDateAndStatusList(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("statusList") List<String> statusList);

    List<DutyLeave> findByEmployeeAndDate(@Param("employeeId") String employeeId, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    List<DutyLeave> findByAccountIdAndDutyDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("accountIds") List<Long> accountIds);

}
