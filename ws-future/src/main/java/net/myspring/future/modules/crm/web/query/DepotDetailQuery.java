package net.myspring.future.modules.crm.web.query;

import net.myspring.future.common.query.BaseQuery;

public class DepotDetailQuery extends BaseQuery{

    private String shopName;
    private String productName;
    private Boolean hasIme;
    private Boolean isSame;


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Boolean getHasIme() {
        return hasIme;
    }

    public void setHasIme(Boolean hasIme) {
        this.hasIme = hasIme;
    }

    public Boolean getIsSame() {
        return isSame;
    }

    public void setIsSame(Boolean isSame) {
        this.isSame = isSame;
    }
}
