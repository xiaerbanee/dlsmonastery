package net.myspring.future.modules.basic.web.Query;

/**
 * Created by lihx on 2017/4/19.
 */
public class ProductQuery {
    private String name;
    private String type;
    private String hasIme;
    private String allowBill;
    private String productType;
    private String allowOrder;
    private String outGroupName;
    private String netType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHasIme() {
        return hasIme;
    }

    public void setHasIme(String hasIme) {
        this.hasIme = hasIme;
    }

    public String getAllowBill() {
        return allowBill;
    }

    public void setAllowBill(String allowBill) {
        this.allowBill = allowBill;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getAllowOrder() {
        return allowOrder;
    }

    public void setAllowOrder(String allowOrder) {
        this.allowOrder = allowOrder;
    }

    public String getOutGroupName() {
        return outGroupName;
    }

    public void setOutGroupName(String outGroupName) {
        this.outGroupName = outGroupName;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }
}
