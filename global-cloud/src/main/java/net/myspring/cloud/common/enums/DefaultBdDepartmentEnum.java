package net.myspring.cloud.common.enums;

/**
 * Created by lihx on 2017/7/22.
 */
public enum DefaultBdDepartmentEnum {
    综合("300"), 电教("101"), 省公司("0001");

    private String FNumber;

    DefaultBdDepartmentEnum( String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFNumber() {
        return FNumber;
    }
}
