package net.myspring.basic.modules.hr.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.DutyAnnual;
import net.myspring.basic.modules.hr.dto.DutyAnnualDto;
import net.myspring.basic.modules.hr.web.query.DutyAnnualQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface DutyAnnualMapper extends MyMapper<DutyAnnual,String> {

    DutyAnnual findByEmployee(@Param("employeeId") String employeeId);

    Page<DutyAnnualDto> findPage(Pageable pageable, @Param("p")DutyAnnualQuery dutyAnnualQuery);

}
