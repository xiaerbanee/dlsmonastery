package net.myspring.future.modules.crm.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.GoodsOrder;

import java.util.List;

public class GoodsOrderForm extends BaseForm<GoodsOrder> {
    private String expressOrderId;
    private String shopId;
    private String netType;
    private String shipType;
    private List<String> netTypeList;
    private List<String> shipTypeList;
    private boolean syn;

    public boolean isSyn() {
        return syn;
    }

    public void setSyn(boolean syn) {
        this.syn = syn;
    }

    private List<GoodsOrderDetailForm> goodsOrderDetailFormList = Lists.newArrayList();

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getNetTypeList() {
        return netTypeList;
    }

    public void setNetTypeList(List<String> netTypeList) {
        this.netTypeList = netTypeList;
    }

    public List<String> getShipTypeList() {
        return shipTypeList;
    }

    public void setShipTypeList(List<String> shipTypeList) {
        this.shipTypeList = shipTypeList;
    }

    public List<GoodsOrderDetailForm> getGoodsOrderDetailFormList() {
        return goodsOrderDetailFormList;
    }

    public void setGoodsOrderDetailFormList(List<GoodsOrderDetailForm> goodsOrderDetailFormList) {
        this.goodsOrderDetailFormList = goodsOrderDetailFormList;
    }
}
