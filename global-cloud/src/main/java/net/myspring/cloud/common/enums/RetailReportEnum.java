package net.myspring.cloud.common.enums;

/**
 * Created by lihx on 2017/2/8.
 */
public enum RetailReportEnum {
    sales_mobile_qty("销售台数","Y01","Y"),
    income_total("收入合计","Y02","Y"),
    cost_total("成本合计","Y03","Y"),
    price_adjustment("管理费用-调价", "6602.027","管理费用"),
    sales_allowance("管理费用-销售折让", "6602.028","管理费用"),
    net_sales_revenue("销售净收入","Y04","Y"),
    total_gross_profit("总毛利润","Y05","Y"),
    mobile_gross_profit("手机毛利润","Y06","Y"),
    accessory_gross_profit("配件毛利润","Y07","Y"),
    operating_commission_gross_profit("运营商毛利润-佣金","Y08","Y"),
    valueAdd_service_profit("增值业务利润","Y09","Y"),
    fee("费用 :","Y10","Y"),
    daily_operating_expenses_total("日常运营费用合计","Y11","Y"),
    advertising_fee("管理费用-广告费","6602.025","管理费用"),
    operation_cost_summary("运营费用汇总","Y13","Y"),
    net_profit("净利润","Y14","Y");
    private String fyName;
    private String fyNum;
    private String accName;

    RetailReportEnum(String fyName, String fyNum, String accName) {
        this.fyName = fyName;
        this.fyNum = fyNum;
        this.accName = accName;
    }

    public String getFyName() {
        return fyName;
    }

    public String getFyNum() {
        return fyNum;
    }

    public String getAccName() {
        return accName;
    }
}
