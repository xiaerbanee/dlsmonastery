package net.myspring.cloud.modules.input.domain;

import java.time.LocalDateTime;

public class BdMaterial {
    //id
    private String fMasterId;
    private String fNumber;
    //名称
    private String fName;
    //物料分组
    private Long fMaterialGroup;
    //分组名称
    private String fMaterialGroupName;
    private LocalDateTime fModifyDate;

    public String getfMasterId() {
        return fMasterId;
    }

    public void setfMasterId(String fMasterId) {
        this.fMasterId = fMasterId;
    }

    public String getfNumber() {
        return fNumber;
    }

    public void setfNumber(String fNumber) {
        this.fNumber = fNumber;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public Long getfMaterialGroup() {
        return fMaterialGroup;
    }

    public void setfMaterialGroup(Long fMaterialGroup) {
        this.fMaterialGroup = fMaterialGroup;
    }

    public String getfMaterialGroupName() {
        return fMaterialGroupName;
    }

    public void setfMaterialGroupName(String fMaterialGroupName) {
        this.fMaterialGroupName = fMaterialGroupName;
    }

    public LocalDateTime getfModifyDate() {
        return fModifyDate;
    }

    public void setfModifyDate(LocalDateTime fModifyDate) {
        this.fModifyDate = fModifyDate;
    }

}
