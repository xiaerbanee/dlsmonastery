package net.myspring.cloud.modules.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by liuj on 2017/5/11.
 */
public class CustomerAccountDetailDto {
    //业务类型
    private String billType;
    //单据号
    private String billNo;
    //单据状态
    private String billStatus;
    //单据序列号
    private Integer index;
    //单据日期
    private LocalDate billDate;
    //物料名称
    private String  materialName;
    //总数量
    private Long qty;
    //单价
    private BigDecimal price;
    //预收金额
    private BigDecimal receiveAmount;
    //应收金额
    private BigDecimal shouldGet;
    //期末应收款
    private BigDecimal endShouldGet;
    //备注
    private String remarks;

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(BigDecimal receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public BigDecimal getShouldGet() {
        return shouldGet;
    }

    public void setShouldGet(BigDecimal shouldGet) {
        this.shouldGet = shouldGet;
    }

    public BigDecimal getEndShouldGet() {
        return endShouldGet;
    }

    public void setEndShouldGet(BigDecimal endShouldGet) {
        this.endShouldGet = endShouldGet;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getAmount(){
        if(qty==null || price==null) {
            return null;
        } else {
            return new BigDecimal(qty).multiply(price);
        }
    }
}
