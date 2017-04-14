package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.AdGoodsOrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdGoodsOrderDetailMapper extends MyMapper<AdGoodsOrderDetail,String> {

    List<AdGoodsOrderDetail> findByAdGoodsOrderIds(List<String> adGoodsOrderIds);

}
