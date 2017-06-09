package net.myspring.cloud.modules.input.dto;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/6/8.
 */
public class CnJournalFEntityForCashDto {
    //对方科目编码
    private String accountNumber;
    //部门编码
    private String departmentNumber;
    //员工编码
    private String staffNumber;
    //其他类编码
    private String otherTypeNumber;
    //费用类编码
    private String expenseTypeNumber;
    //对方客户编码
    private String customerNumberFor;
    //借方金额
    private BigDecimal debitAmount;
    //贷方金额
    private BigDecimal creditAmount;
    //摘要
    private String remarks;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getOtherTypeNumber() {
        return otherTypeNumber;
    }

    public void setOtherTypeNumber(String otherTypeNumber) {
        this.otherTypeNumber = otherTypeNumber;
    }

    public String getExpenseTypeNumber() {
        return expenseTypeNumber;
    }

    public void setExpenseTypeNumber(String expenseTypeNumber) {
        this.expenseTypeNumber = expenseTypeNumber;
    }

    public String getCustomerNumberFor() {
        return customerNumberFor;
    }

    public void setCustomerNumberFor(String customerNumberFor) {
        this.customerNumberFor = customerNumberFor;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
