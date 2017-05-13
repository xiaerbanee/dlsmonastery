package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

/**
 * 标准销售退货单/现销退货单
 * Created by lihx on 2017/5/12.
 */
@Mapper
public interface SalReturnStockMapper {

    //标准销售退货单
    List<CustomerReceiveDto> findXSTHDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerIdList") HashSet<String> customerIdList);

    //现销退货单
    List<CustomerReceiveDto> findXXTHDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerIdList") HashSet<String> customerIdList);
}
