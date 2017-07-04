package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.AfterSaleProductAllot;

/**
 * Created by zhucc on 2017/7/4.
 */
public class AfterSaleProductAllotDto extends DataDto<AfterSaleProductAllot> {

    private String afterSaleId;
    private String storeName;
    private String fromProductName;
    private String toProductName;
    private String toOutCode;
    private String fromOutCode;


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

    public String getFromProductName() {
        return fromProductName;
    }

    public void setFromProductName(String fromProductName) {
        this.fromProductName = fromProductName;
    }

    public String getToProductName() {
        return toProductName;
    }

    public void setToProductName(String toProductName) {
        this.toProductName = toProductName;
    }

    public String getToOutCode() {
        return toOutCode;
    }

    public void setToOutCode(String toOutCode) {
        this.toOutCode = toOutCode;
    }

    public String getFromOutCode() {
        return fromOutCode;
    }

    public void setFromOutCode(String fromOutCode) {
        this.fromOutCode = fromOutCode;
    }
}
