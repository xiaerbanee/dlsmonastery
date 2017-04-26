package net.myspring.cloud.modules.report.dto;

import java.math.BigDecimal;

/**
 * 三维--供应商(客户)，部门，物料
 * Created by lihx on 2016/12/7.
 */
public class BillFormatFor3D {
    private String masterId;
    private String masterName;
    private String departmentId;
    private String departmentName;
    private String materialId;
    private String materialName;
    private BigDecimal quantity;
    private BigDecimal amount;
    private String  loc;

    public BillFormatFor3D(BigDecimal quantity, BigDecimal amount ) {
        this.quantity = quantity;
        this.amount = amount;
    }
    public BillFormatFor3D() {}

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
