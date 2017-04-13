package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.model.NameValue;
import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProductImeSaleMapper extends MyMapper<ProductImeSale,String> {

    List<ProductImeSale> findByProductImeId(@Param("productImeId") String productImeId);

    Page<ProductImeSale> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<ProductImeSale> findByEmployeeId(@Param("employeeId") String employeeId, @Param("createdDateStart") LocalDateTime createdDateStart, @Param("createdDateEnd") LocalDateTime createdDateEnd);

    List<NameValue> findByEmployeeIds(@Param("employeeIds") List<String> employeeIds, @Param("createdDateStart") LocalDateTime createdDateStart, @Param("createdDateEnd") LocalDateTime createdDateEnd);
}
