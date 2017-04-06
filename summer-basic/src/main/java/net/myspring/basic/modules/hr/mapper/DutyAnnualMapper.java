package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.DutyAnnual;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface DutyAnnualMapper extends MyMapper<DutyAnnual,String> {

    DutyAnnual findByEmployee(@Param("employeeId") String employeeId);

    Page<DutyAnnual> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

}
