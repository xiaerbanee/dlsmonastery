package net.myspring.cloud.modules.kingdee.domain;

import java.time.LocalDate;

public class BdStock {

    private String FStockId;
    private String FNumber;
    private String FName;
    //分组
    private Long FGroup;
    private String FGroupName;
    private LocalDate FModifyDate;

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
}
