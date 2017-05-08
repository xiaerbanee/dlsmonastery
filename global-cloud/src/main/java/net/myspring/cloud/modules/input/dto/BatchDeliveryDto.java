package net.myspring.cloud.modules.input.dto;

import net.myspring.common.constant.CharConstant;

import java.time.LocalDate;

public class BatchDeliveryDto {
    private String misDeliveryType;
    private LocalDate billDate;
    private String department;
    private String product;
    private Integer qty;
    private String depot;
    private String remark;

    public String getMisDeliveryType() {
        return misDeliveryType;
    }

    public void setMisDeliveryType(String misDeliveryType) {
        this.misDeliveryType = misDeliveryType;
    }

    public String getBillDate() {
        return String.valueOf(billDate.getYear()) + CharConstant.MINUS + billDate.getMonthValue() + CharConstant.MINUS + billDate.getDayOfMonth();
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}