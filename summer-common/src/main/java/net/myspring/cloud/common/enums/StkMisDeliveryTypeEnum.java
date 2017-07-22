package net.myspring.cloud.common.enums;

/**
 * Created by lihx on 2017/5/17.
 */
public enum StkMisDeliveryTypeEnum {
    退货("RETURN"), 出库("GENERAL");

    private String FNumber;

    StkMisDeliveryTypeEnum( String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFNumber() {
        return FNumber;
    }
}
