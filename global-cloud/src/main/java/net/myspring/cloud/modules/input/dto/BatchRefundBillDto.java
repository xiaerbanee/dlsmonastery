package net.myspring.cloud.modules.input.dto;


import net.myspring.common.constant.CharConstant;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BatchRefundBillDto {
    private LocalDate billDate;
    private String customerName;
    private BigDecimal amount;
    private String BankName;
    private String note;
    private String department;
    private String subject;

    public String getBillDate() {
        return String.valueOf(billDate.getYear()) + CharConstant.MINUS + billDate.getMonthValue() + CharConstant.MINUS + billDate.getDayOfMonth();
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
