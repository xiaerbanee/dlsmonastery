package net.myspring.cloud.modules.kingdee.domain;

/**
 * Created by lihx on 2017/4/11.
 */
public class BdAccount {
    private String FAcctId;

    private String FNumber;

    private String  FFullName;

    private String FName;

    private String  FItemDetailId;

    public String getFAcctId() {
        return FAcctId;
    }

    public void setFAcctId(String FAcctId) {
        this.FAcctId = FAcctId;
    }

    public String getFNumber() {
        return FNumber;
    }

    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFFullName() {
        return FFullName;
    }

    public void setFFullName(String FFullName) {
        this.FFullName = FFullName;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getFItemDetailId() {
        return FItemDetailId;
    }

    public void setFItemDetailId(String FItemDetailId) {
        this.FItemDetailId = FItemDetailId;
    }
}
