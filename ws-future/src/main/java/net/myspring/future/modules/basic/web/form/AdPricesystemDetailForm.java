package net.myspring.future.modules.basic.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.AdPricesystemDetail;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/4/17.
 */
public class AdPricesystemDetailForm extends BaseForm<AdPricesystemDetail> {

    private BigDecimal price;
    private String productId;
    private String adPricesystemId;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAdPricesystemId() {
        return adPricesystemId;
    }

    public void setAdPricesystemId(String adPricesystemId) {
        this.adPricesystemId = adPricesystemId;
    }
}
