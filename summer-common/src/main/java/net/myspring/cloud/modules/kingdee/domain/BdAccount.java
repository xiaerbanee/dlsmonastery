package net.myspring.cloud.modules.kingdee.domain;

/**
 * 账户（科目）
 * Created by lihx on 2017/4/11.
 */
public class BdAccount {
    //账户Id
    private String FAcctId;

    private String FNumber;

    private String  FFullName;

    private String FName;

    private String  FItemDetailId;
    //是否为银行账户
    private String FisBank;
    //审核状态
    private String FDocumentStatus;
    //禁用状态
    private String FForbidStatus;

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

    public String getFisBank() {
        return FisBank;
    }

    public void setFisBank(String fisBank) {
        FisBank = fisBank;
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
