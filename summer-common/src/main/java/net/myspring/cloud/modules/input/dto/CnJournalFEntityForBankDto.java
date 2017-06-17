package net.myspring.cloud.modules.input.dto;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/6/8.
 */
public class CnJournalFEntityForBankDto {
    //对方科目编码
    private String accountNumber;
    //部门编码
    private String departmentNumber;
    //员工编码
    private String empInfoNumber;
    //其他类编码
    private String otherTypeNumber;
    //费用类编码
    private String expenseTypeNumber;
    //对方客户编码
    private String customerNumberFor;
    //结算方式
    private String settleTypeNumber;
    //银行账户编码
    private String bankAccountNumber;
    //借方金额
    private BigDecimal debitAmount;
    //贷方金额
    private BigDecimal creditAmount;
    //摘要
    private String comment;

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

    public String getEmpInfoNumber() {
        return empInfoNumber;
    }

    public void setEmpInfoNumber(String empInfoNumber) {
        this.empInfoNumber = empInfoNumber;
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

    public String getSettleTypeNumber() {
        return settleTypeNumber;
    }

    public void setSettleTypeNumber(String settleTypeNumber) {
        this.settleTypeNumber = settleTypeNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
