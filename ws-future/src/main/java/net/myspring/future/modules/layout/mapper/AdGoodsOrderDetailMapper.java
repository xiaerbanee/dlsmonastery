package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdGoodsOrderDetailMapper extends MyMapper<AdGoodsOrderDetail,String> {

    void deleteById(String adGoodsOrderId);

    List<AdGoodsOrderDetailDto> findByAdGoodsOrderIds(List<String> adGoodsOrderIds);

    List<AdGoodsOrderDetailDto> findByAdGoodsOrderForEdit(@Param("outGroupIds") List<String> outGroupIds,@Param("adGoodsOrderId") String adGoodsOrderId);

    List<AdGoodsOrderDetailDto> findByAdGoodsOrderForNew(@Param("outGroupIds") List<String> outGroupIds);

}
