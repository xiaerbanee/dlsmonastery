package net.myspring.cloud.common.enums;

/**
 * Created by lihx on 2017/4/26.
 */
public enum  CustomerGroupEnum {
    成品寄售客户("636694");

    private String primaryGroup;

    CustomerGroupEnum(String primaryGroup) {
        this.primaryGroup = primaryGroup;
    }

    public String getPrimaryGroup() {
        return primaryGroup;
    }
}
