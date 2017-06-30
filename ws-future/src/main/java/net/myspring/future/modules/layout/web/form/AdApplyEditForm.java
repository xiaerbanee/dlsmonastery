package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.layout.domain.AdApply;

/**
 * Created by zhangyf on 2017/6/30.
 */
public class AdApplyEditForm extends BaseForm<AdApply> {

    private String shopName;
    private String productName;
    private Integer applyQty;
    private Integer confirmQty;
    private Integer billedQty;
    private Integer leftQty;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getApplyQty() {
        return applyQty;
    }

    public void setApplyQty(Integer applyQty) {
        this.applyQty = applyQty;
    }

    public Integer getConfirmQty() {
        return confirmQty;
    }

    public void setConfirmQty(Integer confirmQty) {
        this.confirmQty = confirmQty;
    }

    public Integer getBilledQty() {
        return billedQty;
    }

    public void setBilledQty(Integer billedQty) {
        this.billedQty = billedQty;
    }

    public Integer getLeftQty() {
        return leftQty;
    }

    public void setLeftQty(Integer leftQty) {
        this.leftQty = leftQty;
    }
}
