package net.myspring.cloud.modules.kingdee.domain;

import java.time.LocalDateTime;

/**
 *仓库表
 * Created by lihx on 2017/4/11.
 */
public class BdStock {
    //对应业务系统的outId
    private String FStockId;
    //对应业务系统的outCode
    private String FNumber;
    //
    private String FName;
    //分组
    private String FGroup;
    private String FGroupName;
    //修改时间
    private LocalDateTime FModifyDate;
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

    public String getFGroup() {
        return FGroup;
    }

    public void setFGroup(String FGroup) {
        this.FGroup = FGroup;
    }

    public LocalDateTime getFModifyDate() {
        return FModifyDate;
    }

    public void setFModifyDate(LocalDateTime FModifyDate) {
        this.FModifyDate = FModifyDate;
    }

    public String getFGroupName() {
        return FGroupName;
    }

    public void setFGroupName(String FGroupName) {
        this.FGroupName = FGroupName;
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
