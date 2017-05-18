package net.myspring.future.modules.crm.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.util.text.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GoodsOrderForm extends DataForm<GoodsOrder> {
    private String expressOrderId;
    private String shopId;
    private String netType;
    private String shipType;
    private String remarks;
    private String carrierShopId;

    private String id;
    private String storeId;

    private String clientId;



    private String priceSystemId;
    private String priceSystemName;
    private String summary;
    private String status;

    private List<String> netTypeList;
    private List<String> shipTypeList;

    private String contator;
    private String address;
    private String mobilePhone;

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

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCarrierShopId() {
        return carrierShopId;
    }

    public void setCarrierShopId(String carrierShopId) {
        this.carrierShopId = carrierShopId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPriceSystemId() {
        return priceSystemId;
    }

    public void setPriceSystemId(String priceSystemId) {
        this.priceSystemId = priceSystemId;
    }

    public String getPriceSystemName() {
        return priceSystemName;
    }

    public void setPriceSystemName(String priceSystemName) {
        this.priceSystemName = priceSystemName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getContator() {
        return contator;
    }

    public void setContator(String contator) {
        this.contator = contator;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
