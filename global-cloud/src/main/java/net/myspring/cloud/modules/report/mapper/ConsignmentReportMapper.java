package net.myspring.cloud.modules.report.mapper;

import net.myspring.cloud.modules.report.dto.ConsignmentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ConsignmentReportMapper {
    List<ConsignmentDto> findInitialization();
    List<ConsignmentDto> findTransferInByPeriod(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    List<ConsignmentDto> findTransferOutByPeriod(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    List<ConsignmentDto> findOutStockByPeriod(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    List<ConsignmentDto> findReturnStockByPeriod(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    List<ConsignmentDto> findTransferInByEndDate(@Param("dateEnd") LocalDate dateEnd);
    List<ConsignmentDto> findTransferOutByEndDate(@Param("dateEnd") LocalDate dateEnd);
    List<ConsignmentDto> findOutStockByEndDate(@Param("dateEnd") LocalDate dateEnd);
    List<ConsignmentDto> findReturnStockByEndDate(@Param("dateEnd") LocalDate dateEnd);
}
