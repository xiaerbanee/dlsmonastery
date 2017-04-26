package net.myspring.cloud.modules.input.domain;

import java.time.LocalDate;

public class BdCustomer {

    private String fCustId;
    private String fNumber;
    private String fName;
    //客户分组
    private String fPrimaryGroup;
    private String fPrimaryGroupName;
    private LocalDate fModifyDate;

    public String getfCustId() {
        return fCustId;
    }

    public void setfCustId(String fCustId) {
        this.fCustId = fCustId;
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

    public String getfPrimaryGroup() {
        return fPrimaryGroup;
    }

    public void setfPrimaryGroup(String fPrimaryGroup) {
        this.fPrimaryGroup = fPrimaryGroup;
    }

    public String getfPrimaryGroupName() {
        return fPrimaryGroupName;
    }

    public void setfPrimaryGroupName(String fPrimaryGroupName) {
        this.fPrimaryGroupName = fPrimaryGroupName;
    }

    public LocalDate getfModifyDate() {
        return fModifyDate;
    }

    public void setfModifyDate(LocalDate fModifyDate) {
        this.fModifyDate = fModifyDate;
    }
}