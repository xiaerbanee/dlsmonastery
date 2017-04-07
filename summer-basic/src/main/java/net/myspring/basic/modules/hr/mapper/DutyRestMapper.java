package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.DutyRest;
import net.myspring.basic.modules.hr.model.DutyModel;
import net.myspring.basic.modules.hr.web.query.DutyRestQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface DutyRestMapper  extends MyMapper<DutyRest,String > {

    Page<DutyRest> findPage(Pageable pageable, @Param("p")DutyRestQuery dutyRestQuery);

    List<DutyModel> findByAuditable(@Param("leaderId") String leaderId, @Param("status") String status, @Param("dateStart") LocalDateTime dateStart);

    List<DutyRest> findByTypeAndDutyDate(@Param("type") String type, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("statusList") List<String> statusList);

    List<DutyRest> findByAccountIdAndDutyDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("accountIds") List<Long> accountIds);

    List<DutyRest> findByEmployeeAndDate(@Param("employeeId") String employeeId, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
}
