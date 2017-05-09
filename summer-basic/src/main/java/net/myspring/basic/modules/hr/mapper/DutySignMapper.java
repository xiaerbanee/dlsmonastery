package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.DutySign;
import net.myspring.basic.modules.hr.dto.DutyDto;
import net.myspring.basic.modules.hr.dto.DutySignDto;
import net.myspring.basic.modules.hr.web.query.DutySignQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by liuj on 2016/11/18.
 */
@Mapper
public interface DutySignMapper extends MyMapper<DutySign,String> {

    Page<DutySignDto> findPage(Pageable pageable, @Param("p")DutySignQuery dutySignQuery);

    List<DutyDto> findByAuditable(@Param("leaderId") String leaderId, @Param("status") String status, @Param("dateStart") LocalDateTime dateStart);

    List<DutySign> findByFilter(@Param("p")DutySignQuery dutySignQuery);

    List<DutySign> findByAccountIdAndDutyDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("accountIds") List<Long> accountIds);

    List<DutySign> findByEmployeeAndDate(@Param("employeeId") String employeeId, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
}
