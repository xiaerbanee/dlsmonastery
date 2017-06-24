package net.myspring.cloud.modules.input.dto;

import java.math.BigDecimal;

/**
 * Created by liuj on 2017/5/11.
 */
public class SalReturnStockFEntityDto {
    // 价格
    private BigDecimal price;
    // 物料名称
    private String materialNumber;
    // 备注
    private String entryNote;
    //数量
    private Integer qty;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getEntryNote() {
        return entryNote;
    }

    public void setEntryNote(String entryNote) {
        this.entryNote = entryNote;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
