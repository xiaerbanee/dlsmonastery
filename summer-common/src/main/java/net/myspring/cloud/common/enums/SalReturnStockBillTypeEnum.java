package net.myspring.cloud.common.enums;

/**
 * Created by lihx on 2017/5/12.
 */
public enum SalReturnStockBillTypeEnum {
    标准销售退货单("XSTHD01_SYS"),现销退货单("XSTHD06_SYS");
    private String FNumber;

    SalReturnStockBillTypeEnum( String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFNumber() {
        return FNumber;
    }
}
