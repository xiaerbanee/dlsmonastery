package net.myspring.cloud.modules.report.mapper;

import net.myspring.cloud.modules.report.dto.ReceivableForDetailDto;
import net.myspring.cloud.modules.report.dto.ReceivableForSummaryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReceivableReportMapper {
    //其他应收单
    List<ReceivableForDetailDto> findQTYSDByPeriodForEntry(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);
    //销售退货单
    List<ReceivableForDetailDto> findXSTHDByPeriodForEntryF(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);
    //销售出库单
    List<ReceivableForDetailDto> findXSCKDByPeriodForEntryF(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);
    //现销退货单
    List<ReceivableForDetailDto> findXXTHDByPeriodForEntryF(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);
    //现销出库单
    List<ReceivableForDetailDto> findXXCKDByPeriodForEntryF(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);

    //收款单
    List<ReceivableForDetailDto> findSKDByPeriodForEntry(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);
    //收款退款单
    List<ReceivableForDetailDto> findSKTKDByPeriodForEntry(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);

    //销售退货单
    List<ReceivableForDetailDto> findXSTHDByPeriodForEntryFSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);
    //销售出库单
    List<ReceivableForDetailDto> findXSCKDByPeriodForEntryFSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);
    //现销退货单
    List<ReceivableForDetailDto> findXXTHDByPeriodForEntryFSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);
    //现销出库单
    List<ReceivableForDetailDto> findXXCKDByPeriodForEntryFSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);
    //其他应收单
    List<ReceivableForDetailDto> findQTYSDByPeriodForEntrySum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);
    //收款单
    List<ReceivableForDetailDto> findSKDByPeriodForEntrySum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);
    //收款退款单
    List<ReceivableForDetailDto> findSKTKDByPeriodForEntrySum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);

    //其他应收单
    List<ReceivableForSummaryDto> findQTYSDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    //销售退货单
    List<ReceivableForSummaryDto>   findXSTHDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    //销售出库单
    List<ReceivableForSummaryDto> findXSCKDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    //现销退货单
    List<ReceivableForSummaryDto>   findXXTHDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    //现销出库单
    List<ReceivableForSummaryDto> findXXCKDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    //收款单
    List<ReceivableForSummaryDto> findSKDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    //收款退款单
    List<ReceivableForSummaryDto> findSKTKDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    //期初
    List<ReceivableForSummaryDto> findByEndDate(@Param("dateEnd") LocalDate dateEnd);
    List<ReceivableForSummaryDto> findByEndDateAndIn(@Param("dateEnd") LocalDate dateEnd, @Param("customerIdList") List<String> customerIdList);
}
