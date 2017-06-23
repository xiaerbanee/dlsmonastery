package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.IdDto;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import net.myspring.util.cahe.annotation.CacheInput;

public class StoreAllotDetailSimpleDto extends IdDto<StoreAllotDetail> {

    private String productId;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    private Integer billQty;
    private Integer cloudQty;

    public Integer getCloudQty() {
        return cloudQty;
    }

    public void setCloudQty(Integer cloudQty) {
        this.cloudQty = cloudQty;
    }

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

}
