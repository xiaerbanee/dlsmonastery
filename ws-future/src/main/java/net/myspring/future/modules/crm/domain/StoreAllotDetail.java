package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="crm_store_allot_detail")
public class StoreAllotDetail extends IdEntity<StoreAllotDetail> {
    private Integer qty;
    private Integer billQty;
    private Integer shippedQty;

    private String productId;
    private String storeAllotId;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }

    public Integer getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(Integer shippedQty) {
        this.shippedQty = shippedQty;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStoreAllotId() {
        return storeAllotId;
    }

    public void setStoreAllotId(String storeAllotId) {
        this.storeAllotId = storeAllotId;
    }
}
