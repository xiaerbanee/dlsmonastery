package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.DutyOvertime;
import net.myspring.basic.modules.hr.model.DutyModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface DutyOvertimeMapper extends MyMapper<DutyOvertime,String> {

    Page<DutyOvertime> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<DutyModel> findByAuditable(@Param("leaderId") String leaderId, @Param("status") String status, @Param("dateStart") LocalDateTime dateStart);

    List<DutyOvertime> findByDutyDate(@Param("employeeId") String employeeId, @Param("dutyDate") LocalDate dutyDate);

    List<DutyOvertime> findByDutyDateAndStatus(@Param("employeeId") String employeeId, @Param("dutyDateStart") LocalDate dutyDateStart, @Param("dutyDateEnd") LocalDate dutyDateEnd, @Param("status") String status);

    List<DutyOvertime> findByIdAndDate(@Param("employeeId") String employeeId, @Param("dateStart") LocalDateTime dateStart, @Param("dateEnd") LocalDateTime dateEnd, @Param("status") String status);

    List<DutyOvertime> findByDateAndStatusList(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("statusList") List<String> statusList);

    List<DutyOvertime> findByEmployeeAndDate(@Param("employeeId") String employeeId, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    List<DutyOvertime> findByAccountIdAndDutyDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("accountIds") List<Long> accountIds);

}
