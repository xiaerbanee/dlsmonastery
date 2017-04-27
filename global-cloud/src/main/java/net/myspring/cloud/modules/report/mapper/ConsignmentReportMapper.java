package net.myspring.cloud.modules.report.mapper;

import net.myspring.cloud.modules.report.dto.ConsignmentForUnitDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ConsignmentReportMapper {
    List<ConsignmentForUnitDto> findInitialization();
    List<ConsignmentForUnitDto> findTransferInByPeriod(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    List<ConsignmentForUnitDto> findTransferOutByPeriod(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    List<ConsignmentForUnitDto> findOutStockByPeriod(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    List<ConsignmentForUnitDto> findReturnStockByPeriod(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    List<ConsignmentForUnitDto> findTransferInByEndDate(@Param("dateEnd") LocalDate dateEnd);
    List<ConsignmentForUnitDto> findTransferOutByEndDate(@Param("dateEnd") LocalDate dateEnd);
    List<ConsignmentForUnitDto> findOutStockByEndDate(@Param("dateEnd") LocalDate dateEnd);
    List<ConsignmentForUnitDto> findReturnStockByEndDate(@Param("dateEnd") LocalDate dateEnd);
}
