package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.mapper.GoodsOrderDetailMapper;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsOrderDetailService {

    @Autowired
    private GoodsOrderDetailMapper goodsOrderDetailMapper;

    @Autowired
    private CacheUtils cacheUtils;


    public List<GoodsOrderDetail> findByGoodsOrderId(String goodsOrderId){
        return goodsOrderDetailMapper.findByGoodsOrderId(goodsOrderId);
    }

    public GoodsOrderDetail findOne(String id){
        return goodsOrderDetailMapper.findOne(id);
    }

    public List<GoodsOrderDetailForm> getGoodsOrderDetailListForEdit(String goodsOrderId, String depotId) {

        List<GoodsOrderDetailDto> result =  goodsOrderDetailMapper.getGoodsOrderDetailListForEdit(goodsOrderId, depotId);
        cacheUtils.initCacheInput(result);

        return BeanUtil.map(result, GoodsOrderDetailForm.class);


    }
}
