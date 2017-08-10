package net.myspring.cloud.modules.kingdee.domain;

/**
 * 费用项目
 * Created by lihx on 2017/8/9.
 */
public class BdExpense {
    private String FExpId;
    private String FNumber;
    private String FName;

    public String getFExpId() {
        return FExpId;
    }

    public void setFExpId(String FExpId) {
        this.FExpId = FExpId;
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
