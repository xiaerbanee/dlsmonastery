package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.PriceChangeProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PriceChangeProductMapper extends MyMapper<PriceChangeProduct,String> {

    List<PriceChangeProduct> findByPriceChangeId(String priceChangeId);

}
