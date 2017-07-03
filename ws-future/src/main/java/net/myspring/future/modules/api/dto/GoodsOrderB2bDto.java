package net.myspring.future.modules.api.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.modules.api.domain.CarrierOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.domain.GoodsOrderIme;

import java.util.List;
import java.util.Map;

public class GoodsOrderB2bDto {
    private String id;
    private String businessId;
    private List<B2bOrderDto> b2bOrderList=Lists.newArrayList();
    private List<GoodsOrderIme> goodsOrderImeList= Lists.newArrayList();
    private List<CarrierOrder> carrierOrderList=Lists.newArrayList();
    private List<GoodsOrderDetail> goodsOrderDetailList=Lists.newArrayList();
    private Map<String,Object> extra= Maps.newConcurrentMap();

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public List<GoodsOrderDetail> getGoodsOrderDetailList() {
        return goodsOrderDetailList;
    }

    public void setGoodsOrderDetailList(List<GoodsOrderDetail> goodsOrderDetailList) {
        this.goodsOrderDetailList = goodsOrderDetailList;
    }

    public List<B2bOrderDto> getB2bOrderList() {
        return b2bOrderList;
    }

    public void setB2bOrderList(List<B2bOrderDto> b2bOrderList) {
        this.b2bOrderList = b2bOrderList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public List<GoodsOrderIme> getGoodsOrderImeList() {
        return goodsOrderImeList;
    }

    public void setGoodsOrderImeList(List<GoodsOrderIme> goodsOrderImeList) {
        this.goodsOrderImeList = goodsOrderImeList;
    }

    public List<CarrierOrder> getCarrierOrderList() {
        return carrierOrderList;
    }

    public void setCarrierOrderList(List<CarrierOrder> carrierOrderList) {
        this.carrierOrderList = carrierOrderList;
    }
}
