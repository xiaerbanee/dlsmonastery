package net.myspring.future.modules.crm.domain;



import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="crm_after_sale_product_allot")
public class AfterSaleProductAllot extends DataEntity<AfterSaleProductAllot> {
    private String storeId;
    private String fromProductId;
    private String toProductId;
    private String fromOutCode;
    private String toOutCode;
    private Integer version = 0;
    private String checkId;
    private String afterSaleId;

    public AfterSaleProductAllot() {}

    public AfterSaleProductAllot(String afterSaleId,String storeId,String fromProductId,String toProductId) {
        this.afterSaleId = afterSaleId;
        this.storeId = storeId;
        this.fromProductId = fromProductId;
        this.toProductId = toProductId;
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

    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId;
    }
}
