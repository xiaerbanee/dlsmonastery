package net.myspring.common.enums;

/**
 * Created by lihx on 2017/6/26.
 */
public enum SettleTypeEnum {
    现金("JSFS01_SYS"),电汇("JSFS04_SYS");

    private String FNumber;

    SettleTypeEnum( String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFNumber() {
        return FNumber;
    }
}
