package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.AfterSaleProductAllot;

/**
 * Created by wangzm on 2017/7/4.
 */
public class AfterSaleProductAllotForm extends BaseForm<AfterSaleProductAllot> {
    private String storeId;
    private String fromProductId;
    private String toProductId;
    private String fromOutCode;
    private String toOutCode;
    private Integer fromQty = 0;
    private Integer toQty=0;
    private String checkId;
    private String afterSaleId;

    public Integer getToQty() {
        return toQty;
    }

    public void setToQty(Integer toQty) {
        this.toQty = toQty;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getFromProductId() {
        return fromProductId;
    }

    public void setFromProductId(String fromProductId) {
        this.fromProductId = fromProductId;
    }

    public String getToProductId() {
        return toProductId;
    }

    public void setToProductId(String toProductId) {
        this.toProductId = toProductId;
    }

    public String getFromOutCode() {
        return fromOutCode;
    }

    public void setFromOutCode(String fromOutCode) {
        this.fromOutCode = fromOutCode;
    }

    public String getToOutCode() {
        return toOutCode;
    }

    public void setToOutCode(String toOutCode) {
        this.toOutCode = toOutCode;
    }

    public Integer getFromQty() {
        return fromQty;
    }

    public void setFromQty(Integer fromQty) {
        this.fromQty = fromQty;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId;
    }
}
