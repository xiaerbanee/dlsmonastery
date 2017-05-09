package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.DutyTrip;
import net.myspring.basic.modules.hr.dto.DutyDto;
import net.myspring.basic.modules.hr.dto.DutyTripDto;
import net.myspring.basic.modules.hr.web.query.DutyTripQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface DutyTripMapper extends MyMapper<DutyTrip,String> {

    Page<DutyTripDto> findPage(Pageable pageable, @Param("p")DutyTripQuery dutyTripQuery);

    List<DutyDto> findByAuditable(@Param("leaderId") String leaderId, @Param("status") String status, @Param("createdDateStart") LocalDateTime createdDateStart);

    List<DutyTrip> findByEmployeeAndDate(@Param("employeeId") String employeeId, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    List<DutyTrip> findByDateAndStatusList(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("statusList") List<String> statusList);

    List<DutyTrip> findByAccountIdAndDutyDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("accountIds") List<Long> accountIds);

}
