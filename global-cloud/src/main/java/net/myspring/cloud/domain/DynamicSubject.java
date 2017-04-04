package net.myspring.cloud.domain;

/**
 * Created by lihx on 2017/2/9.
 */
public class DynamicSubject {
    private String accNumber;
    private String accName;
    private String fyNum;
    private String fyName;

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getFyNum() {
        return fyNum;
    }

    public void setFyNum(String fyNum) {
        this.fyNum = fyNum;
    }

    public String getFyName() {
        return fyName;
    }

    public void setFyName(String fyName) {
        this.fyName = fyName;
    }
}
