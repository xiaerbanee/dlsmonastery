package net.myspring.future.modules.layout.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdGoodsOrderDetailMapper extends MyMapper<AdGoodsOrderDetail,String> {

    List<AdGoodsOrderDetail> findByAdGoodsOrderIds(List<String> adGoodsOrderIds);

}
