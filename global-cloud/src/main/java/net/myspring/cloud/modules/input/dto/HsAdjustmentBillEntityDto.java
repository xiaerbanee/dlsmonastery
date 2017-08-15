package net.myspring.cloud.modules.input.dto;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/8/15.
 */
public class HsAdjustmentBillEntityDto {
    private String materialNumber;
    private BigDecimal adjustmentAmount;
    private String stockNumber;

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public BigDecimal getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(BigDecimal adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public String getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(String stockNumber) {
        this.stockNumber = stockNumber;
    }
}
