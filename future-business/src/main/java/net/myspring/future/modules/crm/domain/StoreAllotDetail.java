package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="crm_store_allot_detail")
public class StoreAllotDetail extends IdEntity<StoreAllotDetail> {
    private Integer qty;
    private Integer billQty;
    private Integer shippedQty;
    private Product product;
    private String productId;
    private StoreAllot storeAllot;
    private String storeAllotId;

    @Transient
    private Integer cloudQty;
    @Transient
    private Integer shipQty;

    public Integer getShipQty() {
        return shipQty;
    }

    public void setShipQty(Integer shipQty) {
        this.shipQty = shipQty;
    }

    public Integer getCloudQty() {
        return cloudQty;
    }

    public void setCloudQty(Integer cloudQty) {
        this.cloudQty = cloudQty;
    }

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public StoreAllot getStoreAllot() {
        return storeAllot;
    }

    public void setStoreAllot(StoreAllot storeAllot) {
        this.storeAllot = storeAllot;
    }

    public String getStoreAllotId() {
        return storeAllotId;
    }

    public void setStoreAllotId(String storeAllotId) {
        this.storeAllotId = storeAllotId;
    }
}
