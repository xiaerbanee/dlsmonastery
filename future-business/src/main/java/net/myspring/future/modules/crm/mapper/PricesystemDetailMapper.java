package net.myspring.future.modules.crm.mapper;


import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.PricesystemDetail;
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
