package net.myspring.cloud.modules.kingdee.domain;

import java.time.LocalDateTime;

public class BdMaterial {
    //物料id--对应业务系统product的outId
    private String FMasterId;
    //物料编码--对应业务系统product的outCode
    private String FNumber;
    //物料名称
    private String FName;
    //物料分组
    private Long FMaterialGroup;
    //物料分组名称
    private String FMaterialGroupName;
    //
    private LocalDateTime FModifyDate;
    //审核状态
    private String FDocumentStatus;
    //禁用状态
    private String FForbidStatus;

    public String getFMasterId() {
        return FMasterId;
    }

    public void setFMasterId(String FMasterId) {
        this.FMasterId = FMasterId;
    }

    public String getFNumber() {
        return FNumber;
    }

    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public Long getFMaterialGroup() {
        return FMaterialGroup;
    }

    public void setFMaterialGroup(Long FMaterialGroup) {
        this.FMaterialGroup = FMaterialGroup;
    }

    public String getFMaterialGroupName() {
        return FMaterialGroupName;
    }

    public void setFMaterialGroupName(String FMaterialGroupName) {
        this.FMaterialGroupName = FMaterialGroupName;
    }

    public LocalDateTime getFModifyDate() {
        return FModifyDate;
    }

    public void setFModifyDate(LocalDateTime FModifyDate) {
        this.FModifyDate = FModifyDate;
    }

    public String getFDocumentStatus() {
        return FDocumentStatus;
    }

    public void setFDocumentStatus(String FDocumentStatus) {
        this.FDocumentStatus = FDocumentStatus;
    }

    public String getFForbidStatus() {
        return FForbidStatus;
    }

    public void setFForbidStatus(String FForbidStatus) {
        this.FForbidStatus = FForbidStatus;
    }
}
