package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.mapper.GoodsOrderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsOrderDetailService {

    @Autowired
    private GoodsOrderDetailMapper goodsOrderDetailMapper;

    public List<GoodsOrderDetail> findByGoodsOrderId(String goodsOrderId){
        return goodsOrderDetailMapper.findByGoodsOrderId(goodsOrderId);
    }
    public GoodsOrderDetail findOne(String id){
        return goodsOrderDetailMapper.findOne(id);
    }

}
