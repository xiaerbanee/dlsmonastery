package net.myspring.cloud.common.enums;

/**
 * Created by lihx on 2017/5/12.
 */
public enum SalOutStockBillTypeEnum {
    标准销售出库单("XSCKD01_SYS"),现销出库单("XSCKD06_SYS");
    private String FNumber;

    SalOutStockBillTypeEnum( String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFNumber() {
        return FNumber;
    }
}
