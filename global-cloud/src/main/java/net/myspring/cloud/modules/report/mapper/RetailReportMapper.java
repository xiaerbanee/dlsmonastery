package net.myspring.cloud.modules.report.mapper;

import net.myspring.cloud.modules.report.domain.Retail;
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
    List<Retail> findXSCKDByPeriod(@Param("year") Integer year, @Param("month") Integer month);
    //销售退货单
    List<Retail> findXSTHDByPeriod(@Param("year") Integer year, @Param("month") Integer month);
    //销售出库单
    List<Retail> findXSCKDBySumPeriod(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd);
    //销售退货单
    List<Retail> findXSTHDBySumPeriod(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd);
    //销售出库单
    List<Retail> findXSCKDByPeriodAndDeptNumList(@Param("year") Integer year, @Param("month") Integer month,@Param("deptNumList")List<String> deptNumList);
    //销售退货单
    List<Retail> findXSTHDByPeriodAndDeptNumList(@Param("year") Integer year, @Param("month") Integer month,@Param("deptNumList")List<String> deptNumList);
    //销售出库单
    List<Retail> findXSCKDBySumPeriodAndDeptNumList(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd,@Param("deptNumList")List<String> deptNumList);
    //销售退货单
    List<Retail> findXSTHDBySumPeriodAndDeptNumList(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd,@Param("deptNumList")List<String> deptNumList);

    //合计
    //销售出库单
    List<Retail> findXSCKDByPeriodForTotalDepartment(@Param("year") Integer year, @Param("month") Integer month);
    //销售退货单
    List<Retail> findXSTHDByPeriodForTotalDepartment(@Param("year") Integer year, @Param("month") Integer month);
    //销售出库单
    List<Retail> findXSCKDBySumPeriodForTotalDepartment(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd);
    //销售退货单
    List<Retail> findXSTHDBySumPeriodForTotalDepartment(@Param("dateStart") Integer dateStart, @Param("dateEnd") Integer dateEnd);
}
