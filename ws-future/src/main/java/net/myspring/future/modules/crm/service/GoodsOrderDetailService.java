package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.mapper.GoodsOrderDetailMapper;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderDetailQuery;
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
    @Autowired
    private OfficeClient officeClient;


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

    public void batchSave(String goodsOrderId, List<GoodsOrderDetail> details) {
        goodsOrderDetailMapper.deleteByGoodsOrderId(goodsOrderId);

        if(details==null || details.isEmpty()){
            return;
        }
        details.stream().forEach(each -> each.setGoodsOrderId(goodsOrderId));

        goodsOrderDetailMapper.batchSave(details);

    }

    public List<GoodsOrderDetailForm> getListForNewOrUpdateWithAreaQty(GoodsOrderDetailQuery godq) {


        List<GoodsOrderDetailDto> tmp =  goodsOrderDetailMapper.getListForNewOrUpdateOrBillWithAreaQty(godq);
        cacheUtils.initCacheInput(tmp);

        List<GoodsOrderDetailForm> result = BeanUtil.map(tmp, GoodsOrderDetailForm.class);

        return result;

    }

    public List<GoodsOrderDetailForm> getListForBillWithTodaysAreaBillQty(String goodsOrderId, String pricesystemId, String netType, String officeId) {
        GoodsOrderDetailQuery godq = new GoodsOrderDetailQuery();

        LocalDateTime dateStart = LocalDate.now().atStartOfDay();
        LocalDateTime dateEnd = dateStart.plusDays(1);
        godq.setBillDateStart(dateStart);
        godq.setBillDateEnd(dateEnd);

        godq.setShowAll(Boolean.FALSE);
        godq.setPricesystemId(pricesystemId);
        godq.setGoodsOrderId(goodsOrderId);
        godq.setNetType(netType);
        godq.setOfficeIdList(officeClient.getOfficeIdsOfSameArea(officeId));
//        godq.setAreaId(areaId);
        godq.setCompanyId(RequestUtils.getCompanyId());

        List<String> shipTypeList= Lists.newArrayList();
        shipTypeList.add(ShipTypeEnum.总部发货.name());
        shipTypeList.add(ShipTypeEnum.总部自提.name());
        shipTypeList.add(ShipTypeEnum.地区发货.name());
        shipTypeList.add(ShipTypeEnum.地区自提.name());
        godq.setShipTypeList(shipTypeList);

        List<GoodsOrderDetailDto> tmp =  goodsOrderDetailMapper.getListForNewOrUpdateOrBillWithAreaQty(godq);
        cacheUtils.initCacheInput(tmp);

        List<GoodsOrderDetailForm> result = BeanUtil.map(tmp, GoodsOrderDetailForm.class);

        return result;

    }


}
