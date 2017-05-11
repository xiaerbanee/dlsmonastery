package net.myspring.cloud.modules.kingdee.domain;

import java.time.LocalDate;

public class BdStock {

    private String fStockId;
    private String fNumber;
    private String fName;
    //分组
    private Long fGroup;
    private String fGroupName;
    private LocalDate fModifyDate;

    public String getfStockId() {
        return fStockId;
    }

    public void setfStockId(String fStockId) {
        this.fStockId = fStockId;
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

    public Long getfGroup() {
        return fGroup;
    }

    public void setfGroup(Long fGroup) {
        this.fGroup = fGroup;
    }

    public String getfGroupName() {
        return fGroupName;
    }

    public void setfGroupName(String fGroupName) {
        this.fGroupName = fGroupName;
    }

    public LocalDate getfModifyDate() {
        return fModifyDate;
    }

    public void setfModifyDate(LocalDate fModifyDate) {
        this.fModifyDate = fModifyDate;
    }
}
