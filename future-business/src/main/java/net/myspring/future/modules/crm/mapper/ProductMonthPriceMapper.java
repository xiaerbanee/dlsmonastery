package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ProductMonthPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface ProductMonthPriceMapper extends MyMapper<ProductMonthPrice,String> {

    Page<ProductMonthPrice> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    ProductMonthPrice findByMonth(String month);
}
