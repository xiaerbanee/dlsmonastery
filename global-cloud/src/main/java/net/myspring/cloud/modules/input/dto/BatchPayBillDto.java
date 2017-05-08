package net.myspring.cloud.modules.input.dto;


import net.myspring.common.constant.CharConstant;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BatchPayBillDto {
    // 客户
    private String supplier;
    //部门
    private String department;
    // 日期
    private LocalDate date;
    //科目
    private String subject;
    // 银行
    private String bank;
    // 结算方式
    private String settleType;
    // 金额
    private BigDecimal amout;
    // 备注
    private String note;

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDate() {
        return String.valueOf(date.getYear()) + CharConstant.MINUS + date.getMonthValue() + CharConstant.MINUS + date.getDayOfMonth();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getSettleType() {
        return settleType;
    }

    public void setSettleType(String settleType) {
        this.settleType = settleType;
    }

    public BigDecimal getAmout() {
        return amout;
    }

    public void setAmout(BigDecimal amout) {
        this.amout = amout;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
