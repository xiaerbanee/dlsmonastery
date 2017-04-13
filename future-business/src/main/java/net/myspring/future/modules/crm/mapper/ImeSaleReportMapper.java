package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ImeSaleReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ImeSaleReportMapper extends MyMapper<ImeSaleReport,String> {

    List<ImeSaleReport> findProductSaleDataReport(@Param("p") Map<String, Object> map);

    List<ImeSaleReport> findOfficeSaleDataReport(@Param("p") Map<String, Object> map);

}
