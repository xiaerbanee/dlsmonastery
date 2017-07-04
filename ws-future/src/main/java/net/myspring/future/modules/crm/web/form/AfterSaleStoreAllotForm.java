package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.AfterSaleStoreAllot;

/**
 * Created by wangzm on 2017/7/4.
 */
public class AfterSaleStoreAllotForm extends BaseForm<AfterSaleStoreAllot>{
    private String fromStoreId;
    private String toStoreId;
    private String outCode;
    private String checkId;
    private String productId;
    private String afterSaleId;
    private Integer qty=0;

    public String getFromStoreId() {
        return fromStoreId;
    }

    public void setFromStoreId(String fromStoreId) {
        this.fromStoreId = fromStoreId;
    }

    public String getToStoreId() {
        return toStoreId;
    }

    public void setToStoreId(String toStoreId) {
        this.toStoreId = toStoreId;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
