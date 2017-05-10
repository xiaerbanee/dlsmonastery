package net.myspring.cloud.modules.report.mapper;


import net.myspring.cloud.modules.report.domain.Retail;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.report.dto.AccountSubjectDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;

/**
 * Created by lihx on 2017/2/8.
 */
@Mapper
public interface GlcxViewMapper {
    //针对有数据的门店
    List<NameNumberDto> findDepartment();
    List<NameNumberDto> findDepartmentByDeptNumList(@Param("deptNumList")List<String> deptNumList);
    List<String> findTowDepartmentNumber();

    List<Retail> findEntityByPeriod(@Param("year") Integer year, @Param("month") Integer month, @Param("accName") String accName, @Param("fyNum") String fyNum);
    List<Retail> findEntityBySumPeriod(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd, @Param("accName") String accName, @Param("fyNum") String fyNum);
    List<Retail> findEntityByPeriodAndDeptNumList(@Param("year") Integer year, @Param("month") Integer month, @Param("accName") String accName, @Param("fyNum") String fyNum,@Param("deptNumList")List<String> deptNumList);
    List<Retail> findEntityBySumPeriodAndDeptNumList(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd, @Param("accName") String accName, @Param("fyNum") String fyNum,@Param("deptNumList")List<String> deptNumList);

    //合计门店
    List<Retail> findEntityByPeriodForTotalDepartment(@Param("year") Integer year, @Param("month") Integer month, @Param("accName") String accName, @Param("fyNum") String fyNum);
    List<Retail> findEntityBySumPeriodForTotalDepartment(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd, @Param("accName") String accName, @Param("fyNum") String fyNum);

    //管理科目
    List<AccountSubjectDto> findByAccName(@Param("accName") String accName);
}
