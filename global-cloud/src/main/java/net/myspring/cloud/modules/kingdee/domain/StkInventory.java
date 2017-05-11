package net.myspring.cloud.modules.kingdee.domain;

/**
 * Created by lihx on 2017/4/6.
 */
public class StkInventory {
    private String FStockId;
    private String FMaterialId;
    private Integer qty;

    public String getFStockId() {
        return FStockId;
    }

    public void setFStockId(String FStockId) {
        this.FStockId = FStockId;
    }

    public String getFMaterialId() {
        return FMaterialId;
    }

    public void setFMaterialId(String FMaterialId) {
        this.FMaterialId = FMaterialId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
