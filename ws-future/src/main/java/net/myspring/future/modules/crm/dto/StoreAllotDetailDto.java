package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import net.myspring.util.cahe.annotation.CacheInput;

public class StoreAllotDetailDto extends DataDto<StoreAllotDetail> {

    private String productId;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;

    private Integer billQty;
    private Integer qty;
    private Integer shippedQty;
    private String storeAllotId;
    private String outId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(Integer shippedQty) {
        this.shippedQty = shippedQty;
    }

    public String getStoreAllotId() {
        return storeAllotId;
    }

    public void setStoreAllotId(String storeAllotId) {
        this.storeAllotId = storeAllotId;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }


}
