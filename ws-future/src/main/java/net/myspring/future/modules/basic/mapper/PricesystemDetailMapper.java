package net.myspring.future.modules.basic.mapper;


import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PricesystemDetailMapper extends MyMapper<PricesystemDetail,String> {

    List<PricesystemDetail> findByPricesystemIds(List<String> pricesystemIds);

    PricesystemDetail findByPricesystemIdAndProductId(@Param("pricesystemId") String pricesystemId, @Param("productId") String productId);

    List<PricesystemDetail> findByProductIdList(List<String> productIdList);

    List<PricesystemDetail> findByPricesystemId(String pricesystemId);
}
