package net.myspring.cloud.common.enums;

/**
 * Created by lihx on 2017/6/7.
 */
public enum RetailAccountForManagerExpenses2Enum {
    TRANSFER_OF_PROFITS_2("管理费用-让利2","6602.032","管理费用"),
    TRAVEL_EXPENSES_2("管理费用-差旅费2","6602.033","管理费用"),
    SITE_ACTIVITY_FEE_2("管理费用-场地活动费2","6602.034","管理费用"),
    DELIVERY_FREIGHT_FEE_2("管理费用-收发货运费2","6602.035","管理费用"),
    BENEFITS_OF_WAGES_PROMOTE_2("管理费用-工资福利（正促）2","6602.036","管理费用"),
    BENEFITS_OF_WAGES_PRO_2("管理费用-工资福利（临促）2","6602.037","管理费用"),
    BENEFITS_OF_WAGES_MANAGEMENT_2("管理费用-工资福利（管理人员）2","6602.038","管理费用"),
    ADVERTISING_FEE_2("管理费用-广告费2","6602.039","管理费用");

    private String fyName;
    private String fyNum;
    private String accName;

    RetailAccountForManagerExpenses2Enum(String fyName, String fyNum, String accName) {
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
