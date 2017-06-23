package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.IdDto;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;

public class ShopAllotDetailSimpleDto extends IdDto<ShopAllotDetail> {

    private String productId;
    private String productName;
    private Integer qty;

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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

}
