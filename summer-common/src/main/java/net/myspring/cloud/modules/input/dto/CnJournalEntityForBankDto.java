package net.myspring.cloud.modules.input.dto;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/6/8.
 */
public class CnJournalEntityForBankDto {
    //科目编码
    private String accountNumberK3;
    //部门编码
    private String departmentNumber;
    //员工编码
    private String empInfoNumberK3;
    //其他类编码
    private String otherTypeNumberK3;
    //费用类编码
    private String expenseTypeNumberK3;
    //客户编码
    private String customerNumberK3;
    //结算方式
    private String settleTypeNumberK3;
    //银行账户编码
    private String bankAccountNumber;
    //借方金额
    private BigDecimal debitAmount;
    //贷方金额
    private BigDecimal creditAmount;
    //摘要
    private String comment;

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public String getAccountNumberK3() {
        return accountNumberK3;
    }

    public void setAccountNumberK3(String accountNumberK3) {
        this.accountNumberK3 = accountNumberK3;
    }

    public String getEmpInfoNumberK3() {
        return empInfoNumberK3;
    }

    public void setEmpInfoNumberK3(String empInfoNumberK3) {
        this.empInfoNumberK3 = empInfoNumberK3;
    }

    public String getOtherTypeNumberK3() {
        return otherTypeNumberK3;
    }

    public void setOtherTypeNumberK3(String otherTypeNumberK3) {
        this.otherTypeNumberK3 = otherTypeNumberK3;
    }

    public String getExpenseTypeNumberK3() {
        return expenseTypeNumberK3;
    }

    public void setExpenseTypeNumberK3(String expenseTypeNumberK3) {
        this.expenseTypeNumberK3 = expenseTypeNumberK3;
    }

    public String getCustomerNumberK3() {
        return customerNumberK3;
    }

    public void setCustomerNumberK3(String customerNumberK3) {
        this.customerNumberK3 = customerNumberK3;
    }

    public String getSettleTypeNumberK3() {
        return settleTypeNumberK3;
    }

    public void setSettleTypeNumberK3(String settleTypeNumberK3) {
        this.settleTypeNumberK3 = settleTypeNumberK3;
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
