package net.myspring.cloud.modules.input.dto;

import java.math.BigDecimal;

/**
 * Created by liuj on 2017/5/11.
 */
public class SalOutStockFEntityDto {
    // 价格
    private BigDecimal price;
    // 物料名称
    private String materialNumber;
    // 备注
    private String entrynote;
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

    public String getEntrynote() {
        return entrynote;
    }

    public void setEntrynote(String entrynote) {
        this.entrynote = entrynote;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
