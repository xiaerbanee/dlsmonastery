package net.myspring.future.modules.basic.web.form;

import net.myspring.future.common.form.DataForm;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.ProductDto;

import java.math.BigDecimal;

/**
 * Created by wangzm on 2017/4/21.
 */
public class PricesystemDetailForm extends DataForm<PricesystemDetail> {

    private Integer sort;
    private String productId;
    private BigDecimal price;
    private String pricesystemId;
    private String productName;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
