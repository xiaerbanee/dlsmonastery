package net.myspring.future.modules.basic.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.EmployeePhoneDeposit;

import java.math.BigDecimal;

public class EmployeePhoneDepositForm extends BaseForm<EmployeePhoneDeposit>{
    private String status;
    private String department;
    private BigDecimal amount;
    private String employeeId;
    private String bankId;
    private String productId;
    private String depotId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }
}
