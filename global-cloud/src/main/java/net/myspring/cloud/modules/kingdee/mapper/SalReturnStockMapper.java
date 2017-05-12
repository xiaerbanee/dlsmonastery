package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 标准销售退货单/现销退货单
 * Created by lihx on 2017/5/12.
 */
@Mapper
public interface SalReturnStockMapper {

    //标准销售退货单
    List<CustomerReceiveDetailDto> findXSTHDByPeriodForEntryF(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);

    List<CustomerReceiveDetailDto> findXSTHDByPeriodForEntryFSum(@Param("dateStart")LocalDate dateStart,@Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);

    List<CustomerReceiveDto> findXSTHDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("primaryGroup")String primaryGroup);

    //现销退货单
    List<CustomerReceiveDetailDto> findXXTHDByPeriodForEntryF(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);

    List<CustomerReceiveDetailDto> findXXTHDByPeriodForEntryFSum(@Param("dateStart")LocalDate dateStart,@Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);

    List<CustomerReceiveDto>   findXXTHDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("primaryGroup")String primaryGroup);
}
