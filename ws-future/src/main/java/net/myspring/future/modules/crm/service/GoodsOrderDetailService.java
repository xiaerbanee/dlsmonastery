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

    public List<GoodsOrderDetailForm> getFormListForNewWithoutAreaQty(String depotId, String netType, String shipType) {

        Boolean showAll = Boolean.FALSE;
        if("地区发货".equals(shipType) || "地区自提".equals(shipType) || "代理发货".equals(shipType) || "代理自提".equals(shipType) ){
            showAll = Boolean.TRUE;
        }
        //TODO 如果权限够，也是showAll
        // <shiro:hasPermission name="crm:goodsOrder:bill">
//	        			<c:if test="${fns:getAccount().dataScope eq 10}">
//	        				<c:set var="showAll"  value="1"/>
//	        			</c:if>

        List<GoodsOrderDetailDto> tmp =  goodsOrderDetailMapper.getDtoListForNewWithoutAreaQty(depotId, netType,  showAll);


        cacheUtils.initCacheInput(tmp);
        return BeanUtil.map(tmp, GoodsOrderDetailForm.class);

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
