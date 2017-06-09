package net.myspring.cloud.modules.kingdee.domain;

import java.time.LocalDate;

public class BdStock {
    private String FStockId;
    private String FNumber;
    private String FName;
    //分组
    private Long FGroup;
    private String FGroupName;
    //修改时间
    private LocalDate FModifyDate;
    //审核状态
    private String FDocumentStatus;
    //禁用状态
    private String FForbidStatus;

    public String getFStockId() {
        return FStockId;
    }

    public void setFStockId(String FStockId) {
        this.FStockId = FStockId;
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

    public Long getFGroup() {
        return FGroup;
    }

    public void setFGroup(Long FGroup) {
        this.FGroup = FGroup;
    }

    public String getFGroupName() {
        return FGroupName;
    }

    public void setFGroupName(String FGroupName) {
        this.FGroupName = FGroupName;
    }

    public LocalDate getFModifyDate() {
        return FModifyDate;
    }

    public void setFModifyDate(LocalDate FModifyDate) {
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
