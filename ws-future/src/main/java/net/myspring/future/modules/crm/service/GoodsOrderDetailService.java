package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.mapper.GoodsOrderDetailMapper;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public List<GoodsOrderDetailForm> getFormListWithTodaysAreaQty(String goodsOrderId) {

        LocalDateTime dateStart = LocalDate.now().atStartOfDay();

        LocalDateTime dateEnd = dateStart.plusDays(1);

        List<GoodsOrderDetailDto> result =  goodsOrderDetailMapper.getDtoListWithAreaQty(goodsOrderId, dateStart, dateEnd);
        cacheUtils.initCacheInput(result);
        return BeanUtil.map(result, GoodsOrderDetailForm.class);

    }

    public GoodsOrderDetailForm getFormWithTodaysAreaQty(String productId, String depotId) {
        LocalDateTime dateStart = LocalDate.now().atStartOfDay();
        LocalDateTime dateEnd = dateStart.plusDays(1);

        GoodsOrderDetailDto godd =  goodsOrderDetailMapper.getDtoWithAreaQty(productId, depotId, dateStart, dateEnd);
        if(godd == null){
            godd =new GoodsOrderDetailDto();
            godd.setProductId(productId);
        }
        cacheUtils.initCacheInput(godd);
        return BeanUtil.map(godd, GoodsOrderDetailForm.class);
    }
}
