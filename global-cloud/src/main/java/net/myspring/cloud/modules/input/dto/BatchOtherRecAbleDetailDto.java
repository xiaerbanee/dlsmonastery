package net.myspring.cloud.modules.input.dto;


import java.math.BigDecimal;

/**
 * Created by admin on 2016/10/21.
 */
public class BatchOtherRecAbleDetailDto {
    // 金额
    private BigDecimal amount;
    // 对方科目
    private String subject;
    // 备注
    private String remarks;
    //其他类
    private String otherType;
    //费用类
    private String expenseType;
    //费用承担部门
    private String department;
    //员工
    private String secUser;

    //对方关联客户
    private String F_PAEC_Base;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOtherType() {
        return otherType;
    }

    public void setOtherType(String otherType) {
        this.otherType = otherType;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSecUser() {
        return secUser;
    }

    public void setSecUser(String secUser) {
        this.secUser = secUser;
    }

    public String getF_PAEC_Base() {
        return F_PAEC_Base;
    }

    public void setF_PAEC_Base(String f_PAEC_Base) {
        F_PAEC_Base = f_PAEC_Base;
    }
}
