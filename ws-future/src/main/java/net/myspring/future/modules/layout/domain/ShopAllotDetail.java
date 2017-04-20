package net.myspring.future.modules.layout.domain;


import net.myspring.common.domain.IdEntity;
import net.myspring.future.modules.basic.domain.Product;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="crm_shop_allot_detail")
public class ShopAllotDetail extends IdEntity<ShopAllotDetail> {
    private Integer qty;
    private BigDecimal salePrice;
    private BigDecimal returnPrice;
    private Product product;
    private String productId;
    private ShopAllot shopAllot;
    private String shopAllotId;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(BigDecimal returnPrice) {
        this.returnPrice = returnPrice;
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

    public ShopAllot getShopAllot() {
        return shopAllot;
    }

    public void setShopAllot(ShopAllot shopAllot) {
        this.shopAllot = shopAllot;
    }

    public String getShopAllotId() {
        return shopAllotId;
    }

    public void setShopAllotId(String shopAllotId) {
        this.shopAllotId = shopAllotId;
    }
}
