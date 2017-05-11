package net.myspring.cloud.modules.kingdee.domain;

/**
 * Created by lihx on 2017/4/10.
 */
//科目对应核算维度
public class BdFlexItemGroup {
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
