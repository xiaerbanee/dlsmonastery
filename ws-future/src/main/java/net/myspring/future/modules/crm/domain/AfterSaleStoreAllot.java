package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="crm_after_sale_store_allot")
public class AfterSaleStoreAllot extends DataEntity<AfterSaleStoreAllot> {
    private String fromStoreId;
    private String toStoreId;
    private String outCode;
    private Integer version = 0;
    private String checkId;
    private String productId;
    private String afterSaleId;

    public AfterSaleStoreAllot() {}

    public AfterSaleStoreAllot(String afterSaleId,String productId,String fromStoreId,String toStoreId) {
        this.afterSaleId = afterSaleId;
        this.productId = productId;
        this.fromStoreId = fromStoreId;
        this.toStoreId = toStoreId;
    }

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
}
