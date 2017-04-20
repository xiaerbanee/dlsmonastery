package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ImeStockReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ImeStockReportMapper extends MyMapper<ImeStockReport,String> {

    List<ImeStockReport> findProductStockDataReport(@Param("p") Map<String, Object> map);

    List<ImeStockReport> findOfficeStockDataReport(@Param("p") Map<String, Object> map);

}
