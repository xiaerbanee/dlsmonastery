package net.myspring.cloud.modules.input.domain;

import java.time.LocalDate;

public class CnBank {
    private String fBankAcntId;
    private String fNumber;
    private String fName;
    private LocalDate fModeifyDate;

    public String getfBankAcntId() {
        return fBankAcntId;
    }

    public void setfBankAcntId(String fBankAcntId) {
        this.fBankAcntId = fBankAcntId;
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

    public LocalDate getfModeifyDate() {
        return fModeifyDate;
    }

    public void setfModeifyDate(LocalDate fModeifyDate) {
        this.fModeifyDate = fModeifyDate;
    }
}
