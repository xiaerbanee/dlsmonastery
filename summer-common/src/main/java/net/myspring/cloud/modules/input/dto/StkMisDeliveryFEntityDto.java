package net.myspring.cloud.modules.input.dto;

/**
 * Created by lihx on 2017/7/4.
 */
public class StkMisDeliveryFEntityDto {
    //物料编码
    private String materialNumber;
    //仓库编码
    private String stockNumber;
    //数量
    private Integer qty;
    //备注
    private String FEntryNote;

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(String stockNumber) {
        this.stockNumber = stockNumber;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getFEntryNote() {
        return FEntryNote;
    }

    public void setFEntryNote(String FEntryNote) {
        this.FEntryNote = FEntryNote;
    }
}
