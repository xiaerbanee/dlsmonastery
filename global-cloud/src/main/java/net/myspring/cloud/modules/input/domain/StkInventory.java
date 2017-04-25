package net.myspring.cloud.modules.input.domain;

/**
 * Created by lihx on 2017/4/6.
 */
public class StkInventory {
    private String fStockId;
    private String fMaterialId;
    private Integer qty;

    public String getfStockId() {
        return fStockId;
    }

    public void setfStockId(String fStockId) {
        this.fStockId = fStockId;
    }

    public String getfMaterialId() {
        return fMaterialId;
    }

    public void setfMaterialId(String fMaterialId) {
        this.fMaterialId = fMaterialId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
