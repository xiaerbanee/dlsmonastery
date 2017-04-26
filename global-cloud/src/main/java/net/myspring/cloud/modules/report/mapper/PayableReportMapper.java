package net.myspring.cloud.modules.report.mapper;

import net.myspring.cloud.modules.report.dto.PayableForDetailDto;
import net.myspring.cloud.modules.report.dto.PayableForSummaryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;


@Mapper
public interface PayableReportMapper {

    //详细表
    //付款单
    List<PayableForDetailDto> findFKDByPeriodForEntry(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("supplierId") String supplierId, @Param("departmentId") String departmentId);
    //付款退款单
    List<PayableForDetailDto> findFKTKDByPeriodForEntry(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("supplierId") String supplierId, @Param("departmentId") String departmentId);
    //其他应付单
    List<PayableForDetailDto> findQTYFDByPeriodForEntry(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("supplierId") String supplierId, @Param("departmentId") String departmentId);
    //采购退料单
    List<PayableForDetailDto> findCGTLDByPeriodForEntryF(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("supplierId") String supplierId, @Param("departmentId") String departmentId);
    //采购入库单
    List<PayableForDetailDto> findCGRKDByPeriodForEntryF(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("supplierId") String supplierId, @Param("departmentId") String departmentId);
    //应付单
    List<PayableForDetailDto> findYFDByPeriodForEntry(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("supplierId") String supplierId, @Param("departmentId") String departmentId);
    //采购退料单
    List<PayableForDetailDto> findCGTLDByPeriodForEntryFSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("supplierId") String supplierId, @Param("departmentId") String departmentId);
    //采购入库单
    List<PayableForDetailDto> findCGRKDByPeriodForEntryFSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("supplierId") String supplierId, @Param("departmentId") String departmentId);
    //应付单
    List<PayableForDetailDto> findYFDByPeriodForEntrySum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("supplierId") String supplierId, @Param("departmentId") String departmentId);

    //汇总表
    //期初
    List<PayableForSummaryDto> findByEndDate(@Param("date")LocalDate date);
    List<PayableForSummaryDto> findByEndDateAndIn(@Param("date")LocalDate date, @Param("supplierIdList")List<String> supplierIdList, @Param("departmentIdList")List<String> departmentIdList);

    //付款单
    List<PayableForSummaryDto> findFKDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    //付款退款单
    List<PayableForSummaryDto> findFKTKDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    //其他应付单
    List<PayableForSummaryDto> findQTYFDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    //采购退料单
    List<PayableForSummaryDto> findCGTLDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    //采购入库单
    List<PayableForSummaryDto> findCGRKDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
    //应付单
    List<PayableForSummaryDto> findYFDByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);
}
