package net.myspring.future.modules.basic.web.form;

public class PrintConfigForm {
    private String orderType;
    private Integer qty;
    private String storeNames;
    private String orderIds;
    private String pickUpType;
    private String printDate;
    private String version;

    public enum PrintPickUpType {
        自提,非自提,全部
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getStoreNames() {
        return storeNames;
    }

    public void setStoreNames(String storeNames) {
        this.storeNames = storeNames;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public String getPickUpType() {
        return pickUpType;
    }

    public void setPickUpType(String pickUpType) {
        this.pickUpType = pickUpType;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
