package net.myspring.cloud.modules.report.mapper;


import net.myspring.cloud.modules.report.domain.Retail;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.sys.domain.DynamicSubject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lihx on 2017/2/8.
 */
@Mapper
public interface GlcxViewMapper {
    //针对有数据的门店
    List<NameNumberDto> findDepartment();

    List<Retail> findEntityByPeriod(@Param("year") Integer year, @Param("month") Integer month, @Param("accName") String accName, @Param("fyNum") String fyNum);
    List<Retail> findEntityBySumPeriod(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd, @Param("accName") String accName, @Param("fyNum") String fyNum);

    //合计门店
    List<Retail> findEntityByPeriodForTotalDepartment(@Param("year") Integer year, @Param("month") Integer month, @Param("accName") String accName, @Param("fyNum") String fyNum);
    List<Retail> findEntityBySumPeriodForTotalDepartment(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd, @Param("accName") String accName, @Param("fyNum") String fyNum);

    //管理科目
    List<DynamicSubject> findByAccName(@Param("accName") String accName);
}
