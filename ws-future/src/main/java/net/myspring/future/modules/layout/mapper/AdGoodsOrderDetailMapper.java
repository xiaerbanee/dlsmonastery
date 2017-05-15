package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdGoodsOrderDetailMapper extends MyMapper<AdGoodsOrderDetail,String> {

    List<AdGoodsOrderDetailDto> findByAdGoodsOrderIds(List<String> adGoodsOrderIds);

    List<AdGoodsOrderDetailDto> findByAdGoodsOrderForEdit(@Param("productType") String productType,@Param("adGoodsOrderId") String adGoodsOrderId);

}
