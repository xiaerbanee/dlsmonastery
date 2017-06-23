package net.myspring.cloud.modules.input.dto;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/6/20.
 */
public class ArOtherRecAbleFEntityDto {
    // 金额
    private BigDecimal amount;
    // 对方科目
    private String accountNumber;
    //其他类
    private String otherTypeNumber;
    //费用类
    private String expenseTypeNumber;
    //费用承担部门
    private String departmentNumber;
    //员工
    private String empInfoNumber;
    //对方关联客户
    private String customerForNumber;
    // 备注
    private String comment;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public String getCustomerForNumber() {
        return customerForNumber;
    }

    public void setCustomerForNumber(String customerForNumber) {
        this.customerForNumber = customerForNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
