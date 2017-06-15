package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.crm.domain.GoodsOrder;

public class GoodsOrderMallForm extends BaseForm<GoodsOrder> {

    private String shopId;
    private String parentShopId;
    private String remark;
    private String netType;
    private String shipType;
    private String carrierShopId;
    private String carrierCodes;
    private String carrierDetails;
    private DepotShop shop;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getParentShopId() {
        return parentShopId;
    }

    public void setParentShopId(String parentShopId) {
        this.parentShopId = parentShopId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getCarrierShopId() {
        return carrierShopId;
    }

    public void setCarrierShopId(String carrierShopId) {
        this.carrierShopId = carrierShopId;
    }

    public String getCarrierCodes() {
        return carrierCodes;
    }

    public void setCarrierCodes(String carrierCodes) {
        this.carrierCodes = carrierCodes;
    }

    public String getCarrierDetails() {
        return carrierDetails;
    }

    public void setCarrierDetails(String carrierDetails) {
        this.carrierDetails = carrierDetails;
    }

    public DepotShop getShop() {
        return shop;
    }

    public void setShop(DepotShop shop) {
        this.shop = shop;
    }
}
