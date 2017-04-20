package net.myspring.future.modules.basic.web.form;

import net.myspring.future.common.form.DataForm;
import net.myspring.future.modules.basic.domain.ShopAdType;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/4/19.
 */
public class ShopAdTypeForm extends DataForm<ShopAdType> {
    private String name;
    private String totalPriceType;
    private BigDecimal price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalPriceType() {
        return totalPriceType;
    }

    public void setTotalPriceType(String totalPriceType) {
        this.totalPriceType = totalPriceType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
