package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ProductMonthPriceDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMonthPriceDetailMapper extends MyMapper<ProductMonthPriceDetail,String> {

    List<ProductMonthPriceDetail> findByProductMonthPriceIdList(List<String> productMonthPriceIdList);

    int deleteByProductMonthPriceId(String productMonthPriceId);
}
