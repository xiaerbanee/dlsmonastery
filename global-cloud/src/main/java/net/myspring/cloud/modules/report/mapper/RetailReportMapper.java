package net.myspring.cloud.modules.report.mapper;

import net.myspring.cloud.modules.report.dto.RetailForUnitDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lihx on 2017/4/27.
 */
@Mapper
public interface RetailReportMapper {
    //只针对手机产品（CHLB10）
    //销售出库单
    List<RetailForUnitDto> findXSCKDByPeriod(@Param("year") Integer year, @Param("month") Integer month);
    //销售退货单
    List<RetailForUnitDto> findXSTHDByPeriod(@Param("year") Integer year, @Param("month") Integer month);
    //销售出库单
    List<RetailForUnitDto> findXSCKDBySumPeriod(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd);
    //销售退货单
    List<RetailForUnitDto> findXSTHDBySumPeriod(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd);

    //合计
    //销售出库单
    List<RetailForUnitDto> findXSCKDByPeriodForTotalDepartment(@Param("year") Integer year, @Param("month") Integer month);
    //销售退货单
    List<RetailForUnitDto> findXSTHDByPeriodForTotalDepartment(@Param("year") Integer year, @Param("month") Integer month);
    //销售出库单
    List<RetailForUnitDto> findXSCKDBySumPeriodForTotalDepartment(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd);
    //销售退货单
    List<RetailForUnitDto> findXSTHDBySumPeriodForTotalDepartment(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd);
}
