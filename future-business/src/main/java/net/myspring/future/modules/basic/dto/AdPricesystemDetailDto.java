package net.myspring.future.modules.basic.dto;

import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.domain.AdPricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/4/17.
 */
public class AdPricesystemDetailDto extends DataDto<AdPricesystemDetail> {
    private BigDecimal price;
    private Integer version;
    private Product product;
    private String productId;
    private AdPricesystem adPricesystem;
    private String adPricesystemId;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public AdPricesystem getAdPricesystem() {
        return adPricesystem;
    }

    public void setAdPricesystem(AdPricesystem adPricesystem) {
        this.adPricesystem = adPricesystem;
    }

    public String getAdPricesystemId() {
        return adPricesystemId;
    }

    public void setAdPricesystemId(String adPricesystemId) {
        this.adPricesystemId = adPricesystemId;
    }
}
