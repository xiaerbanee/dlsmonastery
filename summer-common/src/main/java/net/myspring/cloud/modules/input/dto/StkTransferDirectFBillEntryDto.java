package net.myspring.cloud.modules.input.dto;

/**
 * 直接调拨单
 * Created by lihx on 2017/6/22.
 */
public class StkTransferDirectFBillEntryDto {
    private Integer qty;
    private String materialNumber;
    //调出仓库
    private String srcStockNumber;
    //调入仓库
    private String destStockNumber;
    //备注
    private String noteEntry;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getSrcStockNumber() {
        return srcStockNumber;
    }

    public void setSrcStockNumber(String srcStockNumber) {
        this.srcStockNumber = srcStockNumber;
    }

    public String getDestStockNumber() {
        return destStockNumber;
    }

    public void setDestStockNumber(String destStockNumber) {
        this.destStockNumber = destStockNumber;
    }

    public String getNoteEntry() {
        return noteEntry;
    }

    public void setNoteEntry(String noteEntry) {
        this.noteEntry = noteEntry;
    }
}
