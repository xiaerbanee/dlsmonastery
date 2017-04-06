package net.myspring.cloud.modules.kingdee.domain;

/**
 * Created by lihx on 2017/4/6.
 */
public class StkInventory {
    private String fstockId;
    private String fmaterialId;
    private Integer qty;

    public String getFstockId() {
        return fstockId;
    }

    public void setFstockId(String fstockId) {
        this.fstockId = fstockId;
    }

    public String getFmaterialId() {
        return fmaterialId;
    }

    public void setFmaterialId(String fmaterialId) {
        this.fmaterialId = fmaterialId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
