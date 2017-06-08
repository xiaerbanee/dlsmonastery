package net.myspring.cloud.common.enums;

/**
 * Created by lihx on 2017/2/8.
 */
public enum RetailAccountForIncomeEnum {
    mobile_income("手机收入","001","主营业务收入"),
    accessory_income("配件收入","002","主营业务收入"),
    commission_income("佣金收入","003","其他业务收入"),
    valueAdd_service_income("增值业务收入","004","其他业务收入");
    private String fyName;
    private String fyNum;
    private String accName;


    RetailAccountForIncomeEnum(String fyName, String fyNum, String accName) {
        this.fyName = fyName;
        this.fyNum = fyNum;
        this.accName = accName;
    }

    public String getFyName() {
        return fyName;
    }

    public void setFyName(String fyName) {
        this.fyName = fyName;
    }

    public String getFyNum() {
        return fyNum;
    }

    public void setFyNum(String fyNum) {
        this.fyNum = fyNum;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }
}
