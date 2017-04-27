package net.myspring.cloud.common.enums;

/**
 * Created by lihx on 2017/2/8.
 */
public enum RetailReportForCostEnum {
    mobile_cost("手机成本","001","主营业务成本"),
    accessory_cost("配件成本","002","主营业务成本"),
    commission_cost("运营商成本-佣金","003","其他业务支出"),
    valueAdd_service_cost("增值业务成本","004","其他业务支出");
    private String fyName;
    private String fyNum;
    private String accName;


    RetailReportForCostEnum(String fyName, String fyNum, String accName) {
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
