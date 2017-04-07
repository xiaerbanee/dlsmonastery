package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.DutyFree;
import net.myspring.basic.modules.hr.model.DutyModel;
import net.myspring.basic.modules.hr.web.query.DutyFreeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface DutyFreeMapper extends MyMapper<DutyFree,String> {

    Page<DutyFree> findPage(@Param("pageable") Pageable pageable, @Param("p")DutyFreeQuery dutyFreeQuery);

    List<DutyFree> findByDate(@Param("freeDate") LocalDate freeDate, @Param("employeeId") String employeeId);

    List<DutyModel> findByAuditable(@Param("leaderId") String leaderId, @Param("status") String status, @Param("dateStart") LocalDateTime dateStart);

    List<DutyFree> findByDateAndStatusList(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("statusList") List<String> statusList);

    List<DutyFree> findByEmployeeAndDate(@Param("employeeId") String employeeId, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);


    List<DutyFree> findByAccountIdAndDutyDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("accountIds") List<Long> accountIds);

}
