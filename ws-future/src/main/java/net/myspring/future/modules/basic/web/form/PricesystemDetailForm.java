package net.myspring.future.modules.basic.web.form;

import net.myspring.future.common.form.DataForm;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;

import java.math.BigDecimal;

/**
 * Created by wangzm on 2017/4/21.
 */
public class PricesystemDetailForm extends DataForm<PricesystemDetail> {
    private Product product;
    private Integer sort;
    private String productId;
    private BigDecimal price;
    private String pricesystemId;

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
