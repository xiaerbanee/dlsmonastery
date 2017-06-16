package net.myspring.general.modules.sys.domain;

import java.time.LocalDate;

public class CnBankAcnt {
    private String FBankAcntId;
    private String FNumber;
    private String FName;
    private LocalDate FModeifyDate;
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

    public LocalDate getFModeifyDate() {
        return FModeifyDate;
    }

    public void setFModeifyDate(LocalDate FModeifyDate) {
        this.FModeifyDate = FModeifyDate;
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
