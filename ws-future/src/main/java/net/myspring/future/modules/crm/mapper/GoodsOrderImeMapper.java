package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.GoodsOrderIme;
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsOrderImeMapper extends MyMapper<GoodsOrderIme,String> {

    List<GoodsOrderIme> findByProductImeId(@Param("productImeId") String productImeId);

    List<GoodsOrderIme> findByGoodsOrderIds(List<String> goodsOrderIds);

    List<GoodsOrderImeDto> findDtoListByGoodsOrderId(@Param("goodsOrderId")  String goodsOrderId);
}
