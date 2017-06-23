package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;

import java.math.BigDecimal;


public class ShopAllotDetailAuditForm extends BaseForm<ShopAllotDetail> {

    private BigDecimal returnPrice;
    private BigDecimal salePrice;

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

}
