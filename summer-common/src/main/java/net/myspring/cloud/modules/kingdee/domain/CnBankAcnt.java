package net.myspring.cloud.modules.kingdee.domain;

import java.time.LocalDateTime;

public class CnBankAcnt {
    //对应业务系统的outId
    private String FBankAcntId;
    //对应业务系统的outCode
    private String FNumber;
    //
    private String FName;
    //
    private LocalDateTime FModifyDate;
    //审核状态
    private String FDocumentStatus;
    //禁用状态
    private String FForbidStatus;

    public String getFBankAcntId() {
        return FBankAcntId;
    }

    public void setFBankAcntId(String FBankAcntId) {
        this.FBankAcntId = FBankAcntId;
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
