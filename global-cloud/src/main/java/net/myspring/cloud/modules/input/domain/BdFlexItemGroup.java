package net.myspring.cloud.modules.input.domain;

/**
 * Created by lihx on 2017/4/10.
 */
//科目对应核算维度
public class BdFlexItemGroup {
    private String fId;
    private String fNumber;
    private String fName;

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
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
}
