package net.myspring.cloud.modules.kingdee.domain;

/**
 * 客户分组
 */
public class BdCustomerGroup {
    //BdCustomer表的FPrimaryGroup的外键
    private String FId;
    private String FNumber;
    private String FName;

    public String getFId() {
        return FId;
    }

    public void setFId(String FId) {
        this.FId = FId;
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
}
