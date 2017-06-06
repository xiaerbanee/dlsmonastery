package net.myspring.basic.common.enums;

/**
 * Created by liuj on 2016/12/31.
 */
public enum FolderDefaultEnum {
    UMEDITOR("UMEDITOR"),
    HELP_QUESTION("个人提问"),
    DUTY_LEAVE("请假申请"),
    NOTICE("公告列表"),
    AUDIT_FILE("文件审批"),
    ACCOUNT_MESSAGE("私信列表"),
    SHOP_AD("广告申请"),
    SHOP_BUILD("门店建设"),
    PRICE_CHANGE("调价串码"),
    SHOP_IMAGE("乡镇终端");
    private String value;

    private FolderDefaultEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
