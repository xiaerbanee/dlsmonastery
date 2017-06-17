package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.IdDto;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;

import javax.persistence.Transient;
import java.math.BigDecimal;

public class ShopAllotDetailDto extends IdDto<ShopAllotDetail> {

    private String shopAllotId;
    private String productId;
    private String productName;
    private Integer qty;
    private BigDecimal returnPrice;
    private BigDecimal salePrice;

    public BigDecimal getSaleAmount(){
        if(qty == null || salePrice == null){
            return null;
        }
        return salePrice.multiply(new BigDecimal(qty));
    }

    public BigDecimal getReturnAmount(){
        if(qty == null || returnPrice == null){
            return null;
        }
        return returnPrice.multiply(new BigDecimal(qty));
    }


    public BigDecimal getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(BigDecimal returnPrice) {
        this.returnPrice = returnPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }


    public String getShopAllotId() {
        return shopAllotId;
    }

    public void setShopAllotId(String shopAllotId) {
        this.shopAllotId = shopAllotId;
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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

}
