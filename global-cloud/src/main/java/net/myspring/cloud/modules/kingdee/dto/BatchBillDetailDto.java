package net.myspring.cloud.modules.kingdee.dto;

import java.math.BigDecimal;

public class BatchBillDetailDto {

    // 价格
    private BigDecimal price;
    // 货品
    private String productFNumber;
    // 备注
    private String remarks;
    //数量
    private Integer qty;


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProductFNumber() {
        return productFNumber;
    }

    public void setProductFNumber(String productFNumber) {
        this.productFNumber = productFNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
