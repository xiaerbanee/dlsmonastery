package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.util.text.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GoodsOrderForm extends DataForm<GoodsOrder> {

    private String netType;
    private String businessId;
    private String shopId;
    private String storeId;

    private String clientId;
    private String clientName;

    private String isUseTicket;
    private String shipType;
    private List<String> shipTypeList;
    private String shopType;

    private String priceSystemId;
    private String priceSystemName;
    private String summary;
    private String status;

    private List<GoodsOrderDetailForm> detailFormList = new ArrayList<>();
    private List<String> netTypeList;


    public String getIsUseTicket() {
        return isUseTicket;
    }

    public void setIsUseTicket(String isUseTicket) {
        this.isUseTicket = isUseTicket;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public List<GoodsOrderDetailForm> getDetailFormList() {
        return detailFormList;
    }

    public void setDetailFormList(List<GoodsOrderDetailForm> detailFormList) {
        this.detailFormList = detailFormList;
    }


    public List<String> getShipTypeList() {
        return shipTypeList;
    }

    public void setShipTypeList(List<String> shipTypeList) {
        this.shipTypeList = shipTypeList;
    }



    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public List<String> getNetTypeList() {
        return netTypeList;
    }

    public void setNetTypeList(List<String> netTypeList) {
        this.netTypeList = netTypeList;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
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


    public String getFormatId(){
        if(businessId == null){
            return null;
        }
        return StringUtils.getFormatId(businessId, "XK");
    }



}
