package net.myspring.future.modules.crm.dto;

import net.myspring.future.common.query.BaseQuery;

import java.math.BigDecimal;

public class ReportScoreDataDto {

    private String officeId;
    private Integer totalSaleQty;
    private BigDecimal totalSaleMoney;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public Integer getTotalSaleQty() {
        return totalSaleQty;
    }

    public void setTotalSaleQty(Integer totalSaleQty) {
        this.totalSaleQty = totalSaleQty;
    }

    public BigDecimal getTotalSaleMoney() {
        return totalSaleMoney;
    }

    public void setTotalSaleMoney(BigDecimal totalSaleMoney) {
        this.totalSaleMoney = totalSaleMoney;
    }
}
