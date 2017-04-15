package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.DutyWorktime;
import net.myspring.basic.modules.hr.dto.DutyWorktimeDto;
import net.myspring.basic.modules.hr.web.query.DutyWorktimeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface DutyWorktimeMapper extends MyMapper<DutyWorktime,String> {

    Page<DutyWorktimeDto> findPage(Pageable pageable, @Param("p")DutyWorktimeQuery dutyWorktimeQuery);

    List<DutyWorktime> findByDutyDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    List<DutyWorktime> findByEmployeeAndDate(@Param("employeeId") String employeeId, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    List<DutyWorktime> findByAccountIdAndDutyDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("accountIds") List<Long> accountIds);
 }
