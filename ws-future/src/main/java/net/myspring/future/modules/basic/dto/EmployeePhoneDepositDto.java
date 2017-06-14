package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.EmployeePhoneDeposit;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by lihx on 2017/4/14.
 */
public class EmployeePhoneDepositDto extends DataDto<EmployeePhoneDeposit> {
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "name")
    private String employeeName;
    private String employeeId;
    private String depotName;
    private String areaName;
    private String department;
    private String bankName;
    private BigDecimal amount;
    private String outCode;
    private LocalDate billDate;
    private String productName;
    private String status;
    private String locked;
    private String depotOutId;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepotOutId() {
        return depotOutId;
    }

    public void setDepotOutId(String depotOutId) {
        this.depotOutId = depotOutId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }
}
