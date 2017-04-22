package net.myspring.future.common.enums;

/**
 * Created by liuj on 2016/12/31.
 */
public enum DictEnumCategoryEnum {
    DUTY_LEAVE_TYPE("请假类型"),
    SEX("性别"),
    EDUCATION("学历"),
    SHOP_TYPE("店面类型"),
    OPEN_TYPE("卖场目前情况"),
    BUILD_TYPE("项目建设方式"),
    FIXTURE_TYPE("装修类别"),
    PACKAGES_STATUS("包装"),
    BAD_TYPE("坏机类型"),
    TOS_TORE_TYPE("退机类型"),
    MEMORY("内存"),
    SHOP_ATTRIBUTE_TYPE("终端类型"),
    SHOP_MONTH_TOTAL("店月总量");

    private String value;

    DictEnumCategoryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
