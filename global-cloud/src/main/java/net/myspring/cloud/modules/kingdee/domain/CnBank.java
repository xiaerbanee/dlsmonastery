package net.myspring.cloud.modules.kingdee.domain;

import java.time.LocalDate;

public class CnBank {
    private String FBankAcntId;
    private String FNumber;
    private String FName;
    private LocalDate FModeifyDate;

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
}
