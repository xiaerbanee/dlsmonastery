package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.DutyPublicFree;
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
public interface DutyPublicFreeMapper extends MyMapper<DutyPublicFree, String> {

    Page<DutyPublicFree> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<DutyModel> findByAuditable(@Param("leaderId") String leaderId, @Param("status") String status, @Param("dateStart") LocalDateTime dateStart);

    List<DutyPublicFree> findByDateAndStatusList(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("statusList") List<String> statusList);

    List<DutyPublicFree> findByEmployeeAndDate(@Param("employeeId") String employeeId, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

}
