package net.myspring.cloud.modules.report.mapper;

import net.myspring.cloud.modules.report.domain.Consignment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ConsignmentReportMapper {
    List<Consignment> findInitialization();
    List<Consignment> findTransferInByPeriod(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    List<Consignment> findTransferOutByPeriod(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    List<Consignment> findOutStockByPeriod(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    List<Consignment> findReturnStockByPeriod(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    List<Consignment> findTransferInByEndDate(@Param("dateEnd") LocalDate dateEnd);
    List<Consignment> findTransferOutByEndDate(@Param("dateEnd") LocalDate dateEnd);
    List<Consignment> findOutStockByEndDate(@Param("dateEnd") LocalDate dateEnd);
    List<Consignment> findReturnStockByEndDate(@Param("dateEnd") LocalDate dateEnd);
}
