package net.myspring.future.modules.layout.dto;

import java.math.BigDecimal;

public class AdApplyCountDto{

    private Integer totalBilledQty;
    private Integer totalConfirmQty;
    private Integer totalLeftQty;
    private Integer totalApplyQty;

    public Integer getTotalBilledQty() {
        return totalBilledQty;
    }

    public void setTotalBilledQty(Integer totalBilledQty) {
        this.totalBilledQty = totalBilledQty;
    }

    public Integer getTotalConfirmQty() {
        return totalConfirmQty;
    }

    public void setTotalConfirmQty(Integer totalConfirmQty) {
        this.totalConfirmQty = totalConfirmQty;
    }

    public Integer getTotalLeftQty() {
        return totalLeftQty;
    }

    public void setTotalLeftQty(Integer totalLeftQty) {
        this.totalLeftQty = totalLeftQty;
    }

    public Integer getTotalApplyQty() {
        return totalApplyQty;
    }

    public void setTotalApplyQty(Integer totalApplyQty) {
        this.totalApplyQty = totalApplyQty;
    }
}
