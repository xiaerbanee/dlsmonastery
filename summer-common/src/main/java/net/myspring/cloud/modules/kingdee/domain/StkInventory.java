package net.myspring.cloud.modules.kingdee.domain;

/**
 * 其他出库单
 * Created by lihx on 2017/4/6.
 */
public class StkInventory {
    //仓库--对应业务系统的outId
    private String FStockId;
    //物料--对应业务系统的outId
    private String FMaterialId;
    //库存
    private Integer FBaseQty;

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

    public Integer getFBaseQty() {
        return FBaseQty;
    }

    public void setFBaseQty(Integer FBaseQty) {
        this.FBaseQty = FBaseQty;
    }
}
