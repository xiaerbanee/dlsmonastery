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
    private String parentShopId;
    private String parentShopName;
    private Boolean isUseTicket;
    private String shipType;
    private List<String> shipTypeList;
    private String carrierShopId;
    private String carrierCodes;
    private String carrierDetails;
    private String shopType;
    private String priceSystemName;
    private String summary;

    private List<GoodsOrderDetailForm> detailFormList = new ArrayList<>();
    private List<String> netTypeList;


    public List<GoodsOrderDetailForm> getDetailFormList() {
        return detailFormList;
    }

    public void setDetailFormList(List<GoodsOrderDetailForm> detailFormList) {
        this.detailFormList = detailFormList;
    }

    public String getParentShopId() {
        return parentShopId;
    }

    public void setParentShopId(String parentShopId) {
        this.parentShopId = parentShopId;
    }

    public String getParentShopName() {
        return parentShopName;
    }

    public void setParentShopName(String parentShopName) {
        this.parentShopName = parentShopName;
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

    public Boolean getUseTicket() {
        return isUseTicket;
    }

    public void setUseTicket(Boolean useTicket) {
        isUseTicket = useTicket;
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
