package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

/**
 * 标准销售出库单/现销出库单
 * Created by lihx on 2017/5/12.
 */
@Mapper
public interface SalOutStockMapper {

    //标准销售出库单
    List<CustomerReceiveDto> findXSCKDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerIdList")HashSet<String> customerIdList);

    //现销出库单
    List<CustomerReceiveDto> findXXCKDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerIdList")HashSet<String> customerIdList);
}
