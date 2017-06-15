package net.myspring.future.modules.basic.domain;



import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name="crm_employee_phone")
public class EmployeePhone extends DataEntity<EmployeePhone> {
    private BigDecimal depositAmount;
    private BigDecimal reconditionAmount;
    private BigDecimal jobPrice;
    private BigDecimal retailPrice;
    private LocalDateTime uploadTime;
    private String status;
    private String imeStr;
    private Integer version = 0;
    private String depotId;
    private String employeeId;
    private String productId;

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

    public BigDecimal getJobPrice() {
        return jobPrice;
    }

    public void setJobPrice(BigDecimal jobPrice) {
        this.jobPrice = jobPrice;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImeStr() {
        return imeStr;
    }

    public void setImeStr(String imeStr) {
        this.imeStr = imeStr;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
