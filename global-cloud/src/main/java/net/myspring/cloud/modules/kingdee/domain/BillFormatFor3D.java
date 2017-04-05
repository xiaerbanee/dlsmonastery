package net.myspring.cloud.modules.kingdee.domain;

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

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
