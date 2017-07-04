package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;

/**
 * Created by zhucc on 2017/7/4.
 */
public class AfterSaleStoreAllotDto extends DataDto<AfterSaleStoreAllotDto> {

    private String afterSaleId;
    private String storeName;
    private String productName;
    private String fromStoreName;
    private String toStoreName;
    private String businessId;
    private String outCode;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId;
    }

    public String getStoreName() {

        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFromStoreName() {
        return fromStoreName;
    }

    public void setFromStoreName(String fromStoreName) {
        this.fromStoreName = fromStoreName;
    }

    public String getToStoreName() {
        return toStoreName;
    }

    public void setToStoreName(String toStoreName) {
        this.toStoreName = toStoreName;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }
}
