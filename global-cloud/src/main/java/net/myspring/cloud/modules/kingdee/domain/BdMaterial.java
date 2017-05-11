package net.myspring.cloud.modules.kingdee.domain;

import java.time.LocalDateTime;

public class BdMaterial {
    //id
    private String FMasterId;
    private String FNumber;
    //名称
    private String FName;
    //物料分组
    private Long FMaterialGroup;
    //分组名称
    private String FMaterialGroupName;
    private LocalDateTime FModifyDate;

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

}
