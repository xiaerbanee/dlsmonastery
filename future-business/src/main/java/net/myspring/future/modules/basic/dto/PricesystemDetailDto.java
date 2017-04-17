package net.myspring.future.modules.basic.dto;

import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/4/17.
 */
public class PricesystemDetailDto extends DataDto<PricesystemDetail> {
    private BigDecimal price;
    private Integer sort;
    private Product product;
    private String productId;
    private Pricesystem pricesystem;
    private String pricesystemId;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Pricesystem getPricesystem() {
        return pricesystem;
    }

    public void setPricesystem(Pricesystem pricesystem) {
        this.pricesystem = pricesystem;
    }

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
    }
}
