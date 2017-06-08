package net.myspring.cloud.common.enums;

/**
 * Created by lihx on 2017/6/7.
 */
public enum RetailAccountForManagerExpenses1Enum {
    DEPRECIATION_CHARGE("管理费用-折旧费","6602.001","管理费用"),
    FREIGHT("管理费用-运费","6602.002","管理费用"),
    TRAVEL_EXPENSES("管理费用-差旅费","6602.003","管理费用"),
    SITE_ACTIVITY_FEE("管理费用-场地活动费","6602.004","管理费用"),
    LOW_VALUE_CONSUMABLE_GOODS("管理费用-低值易耗品","6602.005","管理费用"),
    OFFICE_SUPPLIES("管理费用-办公费","6602.006","管理费用"),
    COMMUNICATION_FEE("管理费用-网络通讯费","6602.007","管理费用"),
    WATER_AND_ELECTRICITY_FEE("管理费用-水电费","6602.008","管理费用"),
    BENEFITS_OF_WAGES_PROMOTE("管理费用-工资福利（正促）","6602.009","管理费用"),
    BENEFITS_OF_WAGES_PRO("管理费用-工资福利（临促）","6602.010","管理费用"),
    BENEFITS_OF_WAGES_MANAGEMENT("管理费用-工资福利（管理人员）","6602.011","管理费用"),
    GROUP_CONSTRUCTION_FEE("管理费用-团建费","6602.012","管理费用"),
    RENT("管理费用-房租","6602.013","管理费用"),
    DECORATION_COST("管理费用-装修费","6602.014","管理费用"),
    MAINTENANCE_COST("管理费用-安装维修费","6602.015","管理费用"),
    TRANSFER_FEE("管理费用-转让费","6602.016","管理费用"),
    GUEST_FEE("管理费用-客情费","6602.017","管理费用"),
    TAXATION("管理费用-税费","6602.018","管理费用"),
    PREMIUM_COSTS("管理费用-赠品（礼品）费用","6602.019","管理费用"),
    THE_REBATE("管理费用-保卡返利","6602.020","管理费用"),
    SHARE_SALE_AFTER("管理费用-分摊后台费用","6602.021","管理费用"),
    MOTOR_FARE("管理费用-汽车费","6602.022","管理费用"),
    TRAINING_CONFERENCE_FEE("管理费用-培训会议费","6602.023","管理费用"),
    TRANSFER_OF_PROFITS("管理费用-让利","6602.024","管理费用"),
//    ADVERTISING_FEE("管理费用-广告费","6602.025","管理费用"),
//    x("管理费用-价格返点","6602.026","管理费用"),
//    PRICE_ADJUSTMENT("管理费用-调价","6602.027","管理费用"),
//    SALES_ALLOWANCE("管理费用-销售折让","6602.028" ,"管理费用");
    AUXILIARY_EXPENSES("管理费用-手续费","6602.029","管理费用"),
    PENALTY_FEE("管理费用-罚款费用","6602.030","管理费用"),
    PROPERTY_FEE("管理费用-物业费","6602.031","管理费用"),
    OTHER("管理费用-其他","6602.099" ,"管理费用");

    private String fyName;
    private String fyNum;
    private String accName;

    RetailAccountForManagerExpenses1Enum(String fyName, String fyNum, String accName) {
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
