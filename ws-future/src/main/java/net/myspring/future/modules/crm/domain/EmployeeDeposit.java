package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="crm_employee_deposit")
public class EmployeeDeposit extends DataEntity<EmployeeDeposit> {
    private BigDecimal depositAmount;
    private BigDecimal reconditionAmount;
    private String status;
    private Integer version = 0;
    private String employeeId;

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getReconditionAmount() {
        return reconditionAmount;
    }

    public void setReconditionAmount(BigDecimal reconditionAmount) {
        this.reconditionAmount = reconditionAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
